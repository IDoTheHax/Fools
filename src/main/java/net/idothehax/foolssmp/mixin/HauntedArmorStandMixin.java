package net.idothehax.foolssmp.mixin;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntity.class)
public class HauntedArmorStandMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ArmorStandEntity armorStand = (ArmorStandEntity) (Object) this;

        // Check if a player is nearby
        armorStand.getWorld().getEntitiesByClass(PlayerEntity.class, Box.of(armorStand.getPos(), 10, 10, 10), player -> true)
                .forEach(player -> {
                    // Move towards the player
                    Vec3d direction = player.getPos().subtract(armorStand.getPos());
                    armorStand.setVelocity(direction.normalize().multiply(0.5));
                });
    }
}
