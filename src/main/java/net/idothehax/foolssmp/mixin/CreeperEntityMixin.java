package net.idothehax.foolssmp.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
    @ModifyVariable(method = "explode()V", at = @At("STORE"), ordinal = 0)
    private float modifyExplosionSize(float originalSize) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;
        if (creeper.hasCustomName() && creeper.getCustomName().getString().equals("Nuclear")) {
            return originalSize * 4.0f; // Quadruple the explosion size
        }
        return originalSize;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyCreeperAttributes(CallbackInfo ci) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;
        if (creeper.hasCustomName() && creeper.getCustomName().getString().equals("Nuclear")) {
            // Reduce max health to make it less immune to damage
            creeper.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(40.0); // Default is 20.0
            creeper.setHealth(40.0f);
        }
    }
}
