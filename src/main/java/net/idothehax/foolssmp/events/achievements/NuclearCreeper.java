package net.idothehax.foolssmp.events.achievements;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.idothehax.foolssmp.Foolssmp;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.NameTagItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NuclearCreeper {
    private static final Map<UUID, ServerPlayerEntity> nuclearCreeperCreators = new HashMap<>();

    public static void register() {
        // Register the event for naming a Creeper
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            if (entity instanceof CreeperEntity creeper && player instanceof ServerPlayerEntity serverPlayer) {
                if (player.getStackInHand(hand).getItem() instanceof NameTagItem) {
                    String name = player.getStackInHand(hand).getName().getString();
                    if (name.equals("Nuclear")) {
                        nuclearCreeperCreators.put(creeper.getUuid(), serverPlayer);
                    }
                }
            }

            return ActionResult.PASS;
        });

        // Register the event for Creeper explosion
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof CreeperEntity creeper) {
                creeper.setFuseSpeed(1); // Make the Creeper explode instantly when ignited
            }
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof CreeperEntity creeper && creeper.hasCustomName() && creeper.getCustomName().getString().equals("Nuclear")) {
                ServerPlayerEntity creator = nuclearCreeperCreators.remove(creeper.getUuid());
                if (creator != null) {
                    checkAndGrantAdvancement(creator);
                }
            }
        });
    }

    private static void checkAndGrantAdvancement(ServerPlayerEntity player) {
        Identifier advancementId = Identifier.of(Foolssmp.MOD_ID, "fools/nuclear_creeper");
        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(advancementId);
        if (advancement != null) {
            player.getAdvancementTracker().grantCriterion(advancement, "nuclear_creeper");
        }
    }
}
