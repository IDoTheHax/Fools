package net.idothehax.foolssmp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.idothehax.foolssmp.blocks.LuckyBlock;
import net.idothehax.foolssmp.commands.FoolsCommands;
import net.idothehax.foolssmp.events.GravityGlitch;
import net.idothehax.foolssmp.events.RandomDropFool;
import net.idothehax.foolssmp.events.SoundsFool;
import net.idothehax.foolssmp.events.achievements.LavaDeathTracker;
import net.idothehax.foolssmp.events.achievements.NuclearCreeper;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foolssmp implements ModInitializer {
    public static String MOD_ID = "foolssmp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block LUCKY_BLOCK = registerBlock("lucky_block", new LuckyBlock(AbstractBlock.Settings.create()
            .strength(1.0F, 1200.0F)
            .sounds(BlockSoundGroup.COPPER)
            .emissiveLighting(Blocks::always)));

    @Override
    public void onInitialize() {
        LOGGER.info("Tomfoolery is Initializing");
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % RandomDropFool.DROP_INTERVAL_TICKS == 0) {
                RandomDropFool.tickDrops(server.getWorlds());
            }
            if (server.getTicks() % SoundsFool.SOUND_INTERVAL_TICKS == 0) {
                SoundsFool.playRandomSounds(server.getWorlds());
            }
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof ServerPlayerEntity player && damageSource.isOf(DamageTypes.LAVA)) {
                LavaDeathTracker tracker = ComponentInitializer.LAVA_DEATH_TRACKER.get(player);
                tracker.incrementLavaDeaths(player);
            }
            return true;
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Identifier achievementId = Identifier.of("foolssmp", "advancement/hot_tub_time_machine");
            AdvancementEntry advancementEntry = server.getAdvancementLoader().get(achievementId);
            if (advancementEntry != null) {
                LOGGER.info("Hot Tub Time Machine advancement loaded successfully");
            } else {
                LOGGER.error("Failed to load Hot Tub Time Machine advancement");
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(GravityGlitch::tick);

        CommandRegistrationCallback.EVENT.register(FoolsCommands::register);

        NuclearCreeper.register();
    }

    private static <T extends Item> T registerItem(String id, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), item);
    }

    private static <T extends Block> T registerBlock(String id, T block) {
        return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, id), block);
    }

}
