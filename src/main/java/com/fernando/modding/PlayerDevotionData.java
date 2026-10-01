package com.fernando.modding;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles player devotion persistence using the player's persistent NBT compound.
 * Tracks cumulative reading time, discovered epiphanies (verses), devotion tier, and active blessings.
 */
public class PlayerDevotionData {

    public static final String ROOT_KEY = Player.PERSISTED_NBT_TAG; // "PlayerPersisted"
    public static final String DEVOTION_KEY = "spirit_devotion";

    // NBT Keys
    public static final String KEY_DISCOVERED_VERSES = "discoveredVerses";
    public static final String KEY_TOTAL_READING_SECONDS = "totalReadingSeconds";
    public static final String KEY_DEVOTION_TIER = "devotionTier";
    public static final String KEY_ACTIVE_BLESSINGS = "activeBlessings";

    // Devotion Tier 2 Requirements: 1800s (30 minutes) total reading + at least 5 discovered verses
    public static final int TIER_2_REQUIRED_SECONDS = 1800;
    public static final int TIER_2_REQUIRED_VERSES = 5;

    // Non-persistent session reading ticks (resets on book close or disconnect)
    private static final Map<UUID, Integer> SESSION_READING_TICKS = new ConcurrentHashMap<>();

    /**
     * Retrieves the mod's devotion CompoundTag from the player's persistent data.
     */
    public static CompoundTag getDevotionTag(Player player) {
        if (player == null) {
            return new CompoundTag();
        }
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag persistedRoot;
        if (persistentData.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            persistedRoot = persistentData.getCompound(ROOT_KEY);
        } else {
            persistedRoot = new CompoundTag();
            persistentData.put(ROOT_KEY, persistedRoot);
        }

        if (persistedRoot.contains(DEVOTION_KEY, Tag.TAG_COMPOUND)) {
            return persistedRoot.getCompound(DEVOTION_KEY);
        } else {
            CompoundTag devotionTag = new CompoundTag();
            persistedRoot.put(DEVOTION_KEY, devotionTag);
            return devotionTag;
        }
    }

    /**
     * Discovers a new verse for the player.
     * @return true if the verse was newly discovered, false if it was already known.
     */
    public static boolean addDiscoveredVerse(Player player, String verseId) {
        if (player == null || verseId == null || verseId.isBlank()) return false;

        CompoundTag tag = getDevotionTag(player);
        ListTag list;
        if (tag.contains(KEY_DISCOVERED_VERSES, Tag.TAG_LIST)) {
            list = tag.getList(KEY_DISCOVERED_VERSES, Tag.TAG_STRING);
        } else {
            list = new ListTag();
            tag.put(KEY_DISCOVERED_VERSES, list);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equalsIgnoreCase(verseId)) {
                return false; // Already discovered
            }
        }

        list.add(StringTag.valueOf(verseId));
        // Also automatically add to active blessings
        addActiveBlessing(player, verseId);
        // Refresh tier in NBT
        tag.putInt(KEY_DEVOTION_TIER, calculateDevotionTier(player));
        return true;
    }

    /**
     * Checks if a verse is already discovered by the player.
     */
    public static boolean isVerseDiscovered(Player player, String verseId) {
        if (player == null || verseId == null || verseId.isBlank()) return false;

        CompoundTag tag = getDevotionTag(player);
        if (tag.contains(KEY_DISCOVERED_VERSES, Tag.TAG_LIST)) {
            ListTag list = tag.getList(KEY_DISCOVERED_VERSES, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                if (list.getString(i).equalsIgnoreCase(verseId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an unmodifiable set of all discovered verse IDs.
     */
    public static Set<String> getDiscoveredVerses(Player player) {
        Set<String> verses = new LinkedHashSet<>();
        if (player == null) return verses;

        CompoundTag tag = getDevotionTag(player);
        if (tag.contains(KEY_DISCOVERED_VERSES, Tag.TAG_LIST)) {
            ListTag list = tag.getList(KEY_DISCOVERED_VERSES, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                verses.add(list.getString(i));
            }
        }
        return Collections.unmodifiableSet(verses);
    }

    /**
     * Adds cumulative reading time in seconds to the player's permanent record.
     */
    public static void addReadingTime(Player player, int seconds) {
        if (player == null || seconds <= 0) return;

        CompoundTag tag = getDevotionTag(player);
        long current = tag.getLong(KEY_TOTAL_READING_SECONDS);
        long updated = current + seconds;
        tag.putLong(KEY_TOTAL_READING_SECONDS, updated);
        tag.putInt(KEY_DEVOTION_TIER, calculateDevotionTier(player));
    }

    /**
     * Gets the total reading seconds accumulated by the player.
     */
    public static long getTotalReadingSeconds(Player player) {
        if (player == null) return 0L;
        CompoundTag tag = getDevotionTag(player);
        return tag.getLong(KEY_TOTAL_READING_SECONDS);
    }

    /**
     * Sets the total reading seconds accumulated by the player.
     */
    public static void setTotalReadingSeconds(Player player, long seconds) {
        if (player == null) return;
        CompoundTag tag = getDevotionTag(player);
        tag.putLong(KEY_TOTAL_READING_SECONDS, Math.max(0L, seconds));
        tag.putInt(KEY_DEVOTION_TIER, calculateDevotionTier(player));
    }

    /**
     * Calculates the player's devotion tier:
     * - Tier 1: Initial (Blessed I)
     * - Tier 2: 1800+ seconds (30m) reading + at least 5 discovered verses (Blessed II)
     */
    public static int calculateDevotionTier(Player player) {
        if (player == null) return 1;
        long totalSeconds = getTotalReadingSeconds(player);
        int discoveredCount = getDiscoveredVerses(player).size();
        if (totalSeconds >= TIER_2_REQUIRED_SECONDS && discoveredCount >= TIER_2_REQUIRED_VERSES) {
            return 2;
        }
        return 1;
    }

    /**
     * Gets the player's devotion tier (1 or 2).
     */
    public static int getDevotionTier(Player player) {
        return calculateDevotionTier(player);
    }

    // --- Active Blessings Management ---

    public static Set<String> getActiveBlessings(Player player) {
        Set<String> blessings = new LinkedHashSet<>();
        if (player == null) return blessings;

        CompoundTag tag = getDevotionTag(player);
        if (tag.contains(KEY_ACTIVE_BLESSINGS, Tag.TAG_LIST)) {
            ListTag list = tag.getList(KEY_ACTIVE_BLESSINGS, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                blessings.add(list.getString(i));
            }
        }
        return Collections.unmodifiableSet(blessings);
    }

    public static boolean addActiveBlessing(Player player, String blessingId) {
        if (player == null || blessingId == null || blessingId.isBlank()) return false;

        CompoundTag tag = getDevotionTag(player);
        ListTag list;
        if (tag.contains(KEY_ACTIVE_BLESSINGS, Tag.TAG_LIST)) {
            list = tag.getList(KEY_ACTIVE_BLESSINGS, Tag.TAG_STRING);
        } else {
            list = new ListTag();
            tag.put(KEY_ACTIVE_BLESSINGS, list);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equalsIgnoreCase(blessingId)) {
                return false;
            }
        }

        list.add(StringTag.valueOf(blessingId));
        return true;
    }

    public static boolean removeActiveBlessing(Player player, String blessingId) {
        if (player == null || blessingId == null || blessingId.isBlank()) return false;

        CompoundTag tag = getDevotionTag(player);
        if (!tag.contains(KEY_ACTIVE_BLESSINGS, Tag.TAG_LIST)) return false;

        ListTag list = tag.getList(KEY_ACTIVE_BLESSINGS, Tag.TAG_STRING);
        ListTag updated = new ListTag();
        boolean removed = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equalsIgnoreCase(blessingId)) {
                removed = true;
            } else {
                updated.add(list.get(i));
            }
        }
        if (removed) {
            tag.put(KEY_ACTIVE_BLESSINGS, updated);
        }
        return removed;
    }

    public static void clearActiveBlessings(Player player) {
        if (player == null) return;
        CompoundTag tag = getDevotionTag(player);
        tag.remove(KEY_ACTIVE_BLESSINGS);
    }

    // --- Non-persistent Session Reading Ticks ---

    public static int getSessionReadingTicks(Player player) {
        if (player == null) return 0;
        return SESSION_READING_TICKS.getOrDefault(player.getUUID(), 0);
    }

    public static void setSessionReadingTicks(Player player, int ticks) {
        if (player == null) return;
        SESSION_READING_TICKS.put(player.getUUID(), Math.max(0, ticks));
    }

    public static int incrementSessionReadingTicks(Player player) {
        if (player == null) return 0;
        return SESSION_READING_TICKS.compute(player.getUUID(), (uuid, val) -> (val == null ? 1 : val + 1));
    }

    public static void resetSessionReading(Player player) {
        if (player == null) return;
        SESSION_READING_TICKS.remove(player.getUUID());
    }

    // --- Cloning Persistence (Player death/respawn) ---

    public static void copyFrom(Player source, Player target) {
        if (source == null || target == null) return;
        CompoundTag sourceDevotion = getDevotionTag(source);
        CompoundTag targetDevotion = getDevotionTag(target);
        targetDevotion.merge(sourceDevotion.copy());
    }
}
