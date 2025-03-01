package net.idothehax.foolssmp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.idothehax.foolssmp.events.RandomDropFool;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
        });
    }
}
