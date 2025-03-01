package net.idothehax.foolssmp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.idothehax.foolssmp.commands.FoolsCommands;
import net.idothehax.foolssmp.events.RandomDropFool;
import net.idothehax.foolssmp.events.SoundsFool;
import net.idothehax.foolssmp.events.achievements.LavaDeathTracker;
import net.idothehax.foolssmp.events.achievements.NuclearCreeper;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foolssmp implements ModInitializer {
    public static String MOD_ID = "foolssmp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
            Identifier achievementId = Identifier.of("foolssmp", "hot_tub_time_machine");
            AdvancementEntry advancementEntry = server.getAdvancementLoader().get(achievementId);
            if (advancementEntry != null) {
                LOGGER.info("Hot Tub Time Machine advancement loaded successfully");
            } else {
                LOGGER.error("Failed to load Hot Tub Time Machine advancement");
            }
        });

        CommandRegistrationCallback.EVENT.register(FoolsCommands::register);

        NuclearCreeper.register();
    }
}
