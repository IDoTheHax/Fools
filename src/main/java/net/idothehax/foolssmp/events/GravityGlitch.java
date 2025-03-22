package net.idothehax.foolssmp.events;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GravityGlitch {
    private static final Random RANDOM = new Random();
    private static final float GRAVITY_CHANCE = 0.005f; // 0.5% chance per jump
    private static final int GLITCH_DURATION = 80; // 4 seconds (20 ticks/second)
    private static final Map<UUID, Integer> activeGlitches = new HashMap<>(); // Player UUID -> ticks remaining

    public static void tick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID playerId = player.getUuid();

            // Check if player is mid-glitch
            if (activeGlitches.containsKey(playerId)) {
                int ticksLeft = activeGlitches.get(playerId) - 1;
                if (ticksLeft <= 0) {
                    endGlitch(player);
                } else {
                    activeGlitches.put(playerId, ticksLeft);
                    applyGravity(player);
                }
            } else {
                // Check for jump and trigger glitch
                // Trigger when player jumps (leaves ground after being on it)
                if (!player.isOnGround() && player.getVelocity().y > 0 && RANDOM.nextFloat() < GRAVITY_CHANCE) {
                    startGlitch(player);
                }
            }
        }
    }

    public static void startGlitch(ServerPlayerEntity player) {
        activeGlitches.put(player.getUuid(), GLITCH_DURATION);
        player.sendMessage(Text.of("Gravity glitch activated!"), true);
        applyGravity(player);
    }

    private static void applyGravity(ServerPlayerEntity player) {
        // Set upward motion (negative Y velocity = up in Minecraft)
        player.getServer().setFlightEnabled(true);
        player.setVelocity(player.getVelocity().x, +1.5, player.getVelocity().z);
        player.velocityModified = true; // Sync to client
    }

    private static void endGlitch(ServerPlayerEntity player) {
        activeGlitches.remove(player.getUuid());
        player.sendMessage(Text.of("Gravity restored! Oooops"), true);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0, false, false));
        player.setVelocity(player.getVelocity().x, -1.5, player.getVelocity().z);
        player.velocityModified = true;
        player.getServer().setFlightEnabled(false);
    }
}