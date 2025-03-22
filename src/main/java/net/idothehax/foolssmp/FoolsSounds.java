package net.idothehax.foolssmp;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class FoolsSounds {
    public static final SoundEvent GAMBLING_SPIN = new PolymerSoundEvent(
            PolymerResourcePackUtils.getMainUuid(), // Unique UUID for Polymer
            Identifier.of("foolssmp", "block.gambling.gamble"), // Sound ID
            16.0f, // Range
            true, // Static (preloaded)
            SoundEvents.BLOCK_BELL_USE // Fallback sound (client-side if no resource pack)
    );

    public static final SoundEvent AW_DANGIT = new PolymerSoundEvent(
            PolymerResourcePackUtils.getMainUuid(), // Unique UUID for Polymer
            Identifier.of("foolssmp", "block.gambling.aw_dangit"), // Sound ID
            16.0f, // Range
            true, // Static (preloaded)
            SoundEvents.ENTITY_VILLAGER_NO // Fallback sound (client-side if no resource pack)
    );


    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, Identifier.of("foolssmp", "block.gambling.gamble"), GAMBLING_SPIN);
        Registry.register(Registries.SOUND_EVENT, Identifier.of("foolssmp", "block.gambling.aw_dangit"), AW_DANGIT);
    }
}
