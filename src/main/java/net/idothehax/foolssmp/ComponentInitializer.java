package net.idothehax.foolssmp;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.idothehax.foolssmp.events.achievements.LavaDeathTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ComponentInitializer implements EntityComponentInitializer {
    public static final ComponentKey<LavaDeathTracker> LAVA_DEATH_TRACKER =
            ComponentRegistry.getOrCreate(Identifier.of("foolssmp", "lava_death_tracker"), LavaDeathTracker.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(LAVA_DEATH_TRACKER, player -> new LavaDeathTracker(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
