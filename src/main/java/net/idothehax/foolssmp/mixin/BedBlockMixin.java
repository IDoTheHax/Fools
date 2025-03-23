package net.idothehax.foolssmp.mixin;

import net.idothehax.foolssmp.Foolssmp;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin{
    private static final Random RANDOM = new Random();
    private static final float EXPLOSION_CHANCE = 0.05f; // 5% chance for explosion
    private static final float EXPLOSION_POWER = 3f;

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onBedUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit,
                          CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            return; // Only handle on server side
        }

        Foolssmp.LOGGER.warn("BedBlockMixin.onuse");

        // Check for explosion chance
        if (RANDOM.nextFloat() < EXPLOSION_CHANCE) {
            Foolssmp.LOGGER.warn("BedBlockMixin.exploding");
            // Get the correct bed position (head part)
            BlockPos headPos = pos;
            if (state.get(BedBlock.PART) != net.minecraft.block.enums.BedPart.HEAD) {
                headPos = pos.offset(state.get(BedBlock.FACING));
            }

            // Create explosion
            Vec3d explosionPos = headPos.toCenterPos();
            world.createExplosion(null, world.getDamageSources().badRespawnPoint(explosionPos),
                    null, explosionPos, EXPLOSION_POWER, true, World.ExplosionSourceType.BLOCK);

            // Return success to cancel original method
            cir.setReturnValue(ActionResult.SUCCESS);
        }
        // If no explosion, original method will continue
    }
}