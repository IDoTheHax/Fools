package net.idothehax.foolssmp.events;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;

import java.util.Random;

public class RandomDropFool {
    private static final Random random = new Random();
    public static final int DROP_INTERVAL_TICKS = 600; // Every 30 seconds an item drops has a chance to drop

    public static void tickDrops(Iterable<ServerWorld> worlds) {
        for (ServerWorld world : worlds) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (random.nextInt(100) < 25) { // 25% chance for testing
                    dropRandomItem(player);
                }
            }
        }
    }

    private static void dropRandomItem(ServerPlayerEntity player) {
        DefaultedList<ItemStack> inventory = player.getInventory().main;
        if (!inventory.isEmpty()) {
            int randomSlot = random.nextInt(inventory.size());
            ItemStack itemStack = inventory.get(randomSlot);
            if (!itemStack.isEmpty()) {
                ItemStack droppedItem = itemStack.split(1);
                if (!droppedItem.isEmpty()) {
                    player.dropItem(droppedItem, true, false);
                }
            }
        }
    }
}
