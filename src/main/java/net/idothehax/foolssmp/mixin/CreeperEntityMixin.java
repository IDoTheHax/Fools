package net.idothehax.foolssmp.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {

    @Shadow
    private int explosionRadius = 3;

    @ModifyVariable(method = "explode()V", at = @At("STORE"), ordinal = 0)
    private float modifyExplosionSize(float originalSize) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;
        if (creeper.hasCustomName() && creeper.getCustomName().getString().equals("Nuclear")) {
            return originalSize * 4.0f; // Quadruple the explosion size
        }
        return originalSize;
    }
}
