package net.idothehax.foolssmp.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageTaken(float amount) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof CreeperEntity && entity.hasCustomName() && entity.getCustomName().getString().equals("Nuclear")) {
            return amount * 1.5f; // Increase damage taken by 50%
        }
        return amount;
    }
}
