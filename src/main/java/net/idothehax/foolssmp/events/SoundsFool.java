package net.idothehax.foolssmp.events;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class SoundsFool {
    private static final Random random = new Random();
    public static final int SOUND_INTERVAL_TICKS = 1200; // Every minute (20 ticks * 60 seconds)

    private static final SoundEvent[] SOUNDS = {
            SoundEvents.BLOCK_ANVIL_LAND,
            SoundEvents.BLOCK_PORTAL_AMBIENT,
            SoundEvents.ENTITY_ZOMBIE_AMBIENT,
            SoundEvents.ENTITY_GHAST_SCREAM,
            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
            SoundEvents.BLOCK_CHEST_OPEN,
            SoundEvents.BLOCK_DEEPSLATE_BREAK,
            SoundEvents.BLOCK_STONE_BREAK,
            SoundEvents.BLOCK_DEEPSLATE_HIT,
            SoundEvents.BLOCK_STONE_HIT,
            SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
            SoundEvents.ENTITY_PLAYER_SMALL_FALL
    };

    public static void playRandomSounds(Iterable<ServerWorld> worlds) {
        for (ServerWorld world : worlds) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (random.nextInt(100) < 20) { // 20% chance to play a sound for each player
                    playRandomSound(world, player.getBlockPos());
                }
            }
        }
    }

    private static void playRandomSound(ServerWorld world, BlockPos pos) {
        SoundEvent soundEvent = SOUNDS[random.nextInt(SOUNDS.length)];
        world.playSound(null, pos, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
    }
}
