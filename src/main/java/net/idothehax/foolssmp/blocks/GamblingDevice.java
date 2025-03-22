package net.idothehax.foolssmp.blocks;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.idothehax.foolssmp.Foolssmp;
import net.idothehax.foolssmp.FoolsSounds;
import net.idothehax.foolssmp.events.GravityGlitch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class GamblingDevice extends Block implements PolymerTexturedBlock {
    private static final Random RANDOM = new Random();
    private final BlockState polymerBlockState;
    private final PolymerBlockModel model;
    private static final int DELAY_TICKS = 30; // 1.5 seconds

    public GamblingDevice(Settings settings) {
        super(settings);
        this.model = PolymerBlockModel.of(Identifier.of("foolssmp", "block/gambling_device"));
        this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, this.model);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return this.polymerBlockState;
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, ServerPlayerEntity player) {
        return Blocks.JUKEBOX.getDefaultState();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient || !(player instanceof ServerPlayerEntity)) {
            return ActionResult.PASS;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ItemStack heldItem = player.getMainHandStack();

        if (heldItem.getItem() != Items.DIAMOND) {
            serverPlayer.sendMessage(Text.of("Insert a diamond to gamble!"), true);
            return ActionResult.FAIL;
        }

        heldItem.decrement(1);

        // Play the spin sound immediately
        Foolssmp.LOGGER.info("Playing sound foolssmp:block.gambling.GAMBLING_SPIN at " + pos);
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, pos, FoolsSounds.GAMBLING_SPIN, SoundCategory.BLOCKS, 1.0f, 1.0f);

            // Schedule the roll outcome after 30 ticks
            serverWorld.getServer().execute(() -> {
                serverWorld.scheduleBlockTick(pos, this, DELAY_TICKS);
            });
            serverPlayer.sendMessage(Text.of("Spinning...").copy().formatted(Formatting.GRAY), true);
        } else {
            Foolssmp.LOGGER.warn("World is not ServerWorld, sound and delay may not work: " + world);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        // Find the nearest player to send messages/play sounds to
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10.0, false);
        if (serverPlayer == null) {
            Foolssmp.LOGGER.warn("No player found near " + pos + " for gambling outcome");
            return;
        }

        serverPlayer.sendMessage(Text.of("You lost your diamond, sucker!").copy().formatted(Formatting.RED), true);
        world.playSound(null, pos, FoolsSounds.AW_DANGIT, SoundCategory.BLOCKS, 1.0f, 1.0f);

        //float roll = RANDOM.nextFloat();
        //if (roll < 0.50f) {
        //    giveReward(serverPlayer);
        //} else if (roll < 0.80f) {
        //    serverPlayer.sendMessage(Text.of("You lost your diamond, sucker!").copy().formatted(Formatting.RED), true);
        //    world.playSound(null, pos, FoolsSounds.AW_DANGIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        //} else {
        //    serverPlayer.sendMessage(Text.of("You lost your diamond, sucker!").copy().formatted(Formatting.RED), true);
        //    world.playSound(null, pos, FoolsSounds.AW_DANGIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        //    //GravityGlitch.startGlitch(serverPlayer);
        //    //serverPlayer.sendMessage(Text.of("Gravity prank activated!").copy().formatted(Formatting.YELLOW), true);
        //}
        super.scheduledTick(state, world, pos, random);
    }

    private void giveReward(ServerPlayerEntity player) {
        Item reward;
        float rewardRoll = RANDOM.nextFloat();
        if (rewardRoll < 0.60f) {
            reward = Items.IRON_INGOT;
            player.getInventory().insertStack(new ItemStack(reward, 5));
        } else if (rewardRoll < 0.95f) {
            reward = Items.EMERALD;
            player.getInventory().insertStack(new ItemStack(reward, 2));
        } else {
            reward = Items.NETHER_STAR;
            player.getInventory().insertStack(new ItemStack(reward, 1));
        }
        player.sendMessage(Text.of("You won some " + reward.getName().getString() + "!").copy().formatted(Formatting.GREEN), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        tooltip.add(Text.literal("Gamble a diamond for riches... or chaos!").formatted(Formatting.GOLD));
    }
}