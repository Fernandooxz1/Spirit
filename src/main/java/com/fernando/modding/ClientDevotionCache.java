package com.fernando.modding;

import java.util.*;

/**
 * Cache del lado cliente para los datos de devoción sincronizados desde el servidor.
 * NotebookScreen y BibleScreen leen de aquí en vez de PlayerDevotionData (que usa NBT del servidor).
 */
public class ClientDevotionCache {

    private static Set<String> discoveredVerses = new LinkedHashSet<>();
    private static long totalReadingSeconds = 0;
    private static int devotionTier = 1;

    /**
     * Actualiza el cache con datos recibidos del servidor vía DevotionSyncPayload.
     */
    public static void update(List<String> verses, long readingSeconds, int tier) {
        discoveredVerses = new LinkedHashSet<>(verses);
        totalReadingSeconds = readingSeconds;
        devotionTier = tier;
    }

    public static Set<String> getDiscoveredVerses() {
        return Collections.unmodifiableSet(discoveredVerses);
    }

    public static boolean isVerseDiscovered(String verseId) {
        return discoveredVerses.contains(verseId);
    }

    public static long getTotalReadingSeconds() {
        return totalReadingSeconds;
    }

    public static int getDevotionTier() {
        return devotionTier;
    }

    public static int getDiscoveredCount() {
        return discoveredVerses.size();
    }

    /**
     * Limpia el cache (al desconectarse del servidor).
     */
    public static void clear() {
        discoveredVerses.clear();
        totalReadingSeconds = 0;
        devotionTier = 1;
    }
}
