package net.idothehax.foolssmp.blocks;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.idothehax.foolssmp.events.GravityGlitch; // Import for prank trigger
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
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
        return Blocks.JUKEBOX.getDefaultState(); // Looks like jukebox when broken
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient || !(player instanceof ServerPlayerEntity)) {
            return ActionResult.PASS; // Server-side only
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ItemStack heldItem = player.getMainHandStack(); // Assume main hand for simplicity

        // Check if player is holding a diamond
        if (heldItem.getItem() != Items.DIAMOND) {
            serverPlayer.sendMessage(Text.of("Insert a diamond to gamble!"), true);
            return ActionResult.FAIL;
        }

        // Consume the diamond
        heldItem.decrement(1);

        // Roll the dice
        float roll = RANDOM.nextFloat();
        if (roll < 0.50f) { // 50% chance for reward
            giveReward(serverPlayer);
        } else if (roll < 0.80f) { // 30% chance for loss
            serverPlayer.sendMessage(Text.of("You lost your diamond, sucker!").copy().formatted(Formatting.RED), true);
        } else { // 20% chance for prank
            GravityGlitch.startGlitch(serverPlayer);
            serverPlayer.sendMessage(Text.of("Gravity prank activated!").copy().formatted(Formatting.YELLOW), true);
        }

        return ActionResult.SUCCESS;
    }

    private void giveReward(ServerPlayerEntity player) {
        Item reward;
        float rewardRoll = RANDOM.nextFloat();
        if (rewardRoll < 0.60f) { // 60% of reward cases = common
            reward = Items.IRON_INGOT;
            player.getInventory().insertStack(new ItemStack(reward, 5));
        } else if (rewardRoll < 0.95f) { // 35% = uncommon
            reward = Items.EMERALD;
            player.getInventory().insertStack(new ItemStack(reward, 2));
        } else { // 5% = rare
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
