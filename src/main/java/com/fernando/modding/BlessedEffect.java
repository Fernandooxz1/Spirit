package com.fernando.modding;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class BlessedEffect extends MobEffect {

    public static final int BLESSED_COLOR = 0xFFD700; // Gold

    public BlessedEffect() {
        super(MobEffectCategory.BENEFICIAL, BLESSED_COLOR);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Sincroniza sub-efectos cada 20 ticks (1 segundo)
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            coordinateSubEffects(entity, amplifier);
        }
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        if (!entity.level().isClientSide()) {
            coordinateSubEffects(entity, amplifier);
        }
    }

    /**
     * Coordina los sub-efectos invisibles según los pasajes bíblicos descubiertos por el jugador.
     */
    public static void coordinateSubEffects(LivingEntity entity, int blessedAmplifier) {
        if (entity == null || entity.level().isClientSide()) return;

        MobEffectInstance blessedInst = entity.getEffect(Spirit.BLESSED);
        int remainingDuration = (blessedInst != null) ? blessedInst.getDuration() : 200;

        int targetSubAmplifier = Math.max(0, blessedAmplifier);
        if (entity instanceof Player player) {
            int devotionTier = PlayerDevotionData.getDevotionTier(player);
            if (devotionTier >= 2 && targetSubAmplifier < 1) {
                targetSubAmplifier = 1; // Escala automáticamente a Nivel II si el jugador es Devoto
                if (blessedInst != null && blessedInst.getAmplifier() < 1) {
                    player.addEffect(new MobEffectInstance(
                            Spirit.BLESSED,
                            blessedInst.getDuration(),
                            1,
                            blessedInst.isAmbient(),
                            blessedInst.isVisible(),
                            blessedInst.showIcon()
                    ));
                }
            }
        }

        List<Holder<MobEffect>> requiredEffects = getSubEffectsFor(entity);

        for (Holder<MobEffect> effectHolder : requiredEffects) {
            MobEffectInstance current = entity.getEffect(effectHolder);

            boolean needsUpdate = false;
            if (current == null) {
                needsUpdate = true;
            } else if (current.getAmplifier() != targetSubAmplifier) {
                needsUpdate = true;
            } else if (current.getDuration() < remainingDuration - 20) {
                needsUpdate = true;
            } else if (current.isVisible() || current.showIcon()) {
                // El efecto existe pero es visible — necesita ser reemplazado por invisible
                needsUpdate = true;
            }

            if (needsUpdate) {
                // IMPORTANTE: Remover primero para que addEffect no haga merge con la versión visible
                if (current != null) {
                    entity.removeEffect(effectHolder);
                }
                MobEffectInstance subInst = new MobEffectInstance(
                        effectHolder,
                        remainingDuration,
                        targetSubAmplifier,
                        true,  // ambient
                        false, // visible: false (sin partículas molestas)
                        false  // showIcon: false (interfaz limpia)
                );
                entity.addEffect(subInst);
            }
        }
    }

    public static List<Holder<MobEffect>> getSubEffectsFor(LivingEntity entity) {
        List<Holder<MobEffect>> list = new ArrayList<>();
        if (entity instanceof Player player) {
            Set<String> activeBlessings = PlayerDevotionData.getActiveBlessings(player);
            for (String id : activeBlessings) {
                var epiphanyOpt = EpiphanyRegistry.findById(id);
                if (epiphanyOpt.isPresent()) {
                    Holder<MobEffect> eff = epiphanyOpt.get().effect();
                    if (!list.contains(eff)) {
                        list.add(eff);
                    }
                }
            }
        }

        if (list.isEmpty()) {
            // Efecto base por defecto si no tiene versículos descubiertos
            list.add(MobEffects.REGENERATION);
        }
        return list;
    }

    public static void removeSubEffects(LivingEntity entity) {
        if (entity == null || entity.level().isClientSide()) return;
        for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
            Holder<MobEffect> effectHolder = epiphany.effect();
            MobEffectInstance inst = entity.getEffect(effectHolder);
            // Remueve solo si fue aplicado invisiblemente
            if (inst != null && !inst.isVisible() && !inst.showIcon()) {
                entity.removeEffect(effectHolder);
            }
        }
    }

    public static void applyOrExtendBlessed(Player player, int addedDurationTicks) {
        if (player == null || player.level().isClientSide()) return;

        int tier = PlayerDevotionData.getDevotionTier(player);
        int targetAmplifier = (tier >= 2) ? 1 : 0;

        MobEffectInstance existing = player.getEffect(Spirit.BLESSED);
        int newDuration = (existing != null) ? existing.getDuration() + addedDurationTicks : addedDurationTicks;

        player.addEffect(new MobEffectInstance(
                Spirit.BLESSED,
                newDuration,
                targetAmplifier,
                false, // ambient
                true,  // visible
                true   // showIcon
        ));
    }
}
