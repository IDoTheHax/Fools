package net.idothehax.foolssmp.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class LuckyBlock extends Block implements PolymerBlock {
    public LuckyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) { // Run only on server
            triggerLuckyEffect(world, pos);
            world.breakBlock(pos, false); // Remove the block after triggering
        }
        return ActionResult.SUCCESS;
    }

    private void triggerLuckyEffect(World world, BlockPos pos) {
        Random random = new Random();
        int outcome = random.nextInt(3); // 3 possible outcomes for simplicity

        switch (outcome) {
            case 0: // Drop diamonds
                Block.dropStack(world, pos, new ItemStack(net.minecraft.item.Items.DIAMOND, 3));
                break;
            case 1: // Spawn a creeper
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                net.minecraft.entity.mob.CreeperEntity creeper = new net.minecraft.entity.mob.CreeperEntity(net.minecraft.entity.EntityType.CREEPER, world);
                creeper.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                world.spawnEntity(creeper);
                break;
            case 2: // Explosion
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.0f, false, World.ExplosionSourceType.BLOCK);
                break;
        }
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState) {
        return Blocks.DISPENSER.getDefaultState(); // Makes it look like a dispenser on the client
    }
}