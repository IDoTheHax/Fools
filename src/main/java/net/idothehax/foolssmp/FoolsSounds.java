package net.idothehax.foolssmp;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class FoolsSounds {
    public static final Identifier GAMBLING_SPIN_ID = Identifier.of("foolssmp", "block.gambling.gamble");
    public static final SoundEvent GAMBLING_SPIN = SoundEvent.of(GAMBLING_SPIN_ID);

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, Identifier.of("foolssmp", "block.gambling.gamble"), GAMBLING_SPIN);    }
}
