package net.idothehax.foolssmp.events.achievements;

import net.idothehax.foolssmp.ComponentInitializer;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class LavaDeathTracker implements Component, AutoSyncedComponent {
    public int lavaDeaths = 0;

    public void incrementLavaDeaths(ServerPlayerEntity player) {
        lavaDeaths++;
        if (lavaDeaths == 5) {
            grantHotTubAchievement(player);
            player.sendMessage(Text.of("test"), false);
        }
        ComponentInitializer.LAVA_DEATH_TRACKER.sync(player);
    }


    private void grantHotTubAchievement(ServerPlayerEntity player) {
        Identifier achievementId = Identifier.of("foolssmp", "fools/hot_tub_time_machine");
        AdvancementEntry advancementEntry = player.getServer().getAdvancementLoader().get(achievementId);
        if (advancementEntry != null) {
            player.getAdvancementTracker().grantCriterion(advancementEntry, "hot_tub");
        } else {
            player.sendMessage(Text.of("Advancement not found: " + achievementId), false);
        }
    }


    public int getLavaDeaths() {
        return lavaDeaths;
    }

    public void setLavaDeaths(int deaths) {
        this.lavaDeaths = deaths;
    }


    @Override
    public boolean isRequiredOnClient() {
        return false;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        lavaDeaths = nbtCompound.getInt("lavaDeaths");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("lavaDeaths", lavaDeaths);
    }
}
