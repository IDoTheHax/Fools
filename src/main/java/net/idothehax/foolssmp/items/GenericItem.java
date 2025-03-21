package net.idothehax.foolssmp.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.idothehax.foolssmp.Foolssmp;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GenericItem extends BlockItem implements PolymerItem {
    private final PolymerModelData polymerModel;

    public GenericItem(Settings settings, Block block, String modelId) {
        super(block, settings);
        this.polymerModel = PolymerResourcePackUtils.requestModel(Items.BARRIER, Identifier.of(Foolssmp.MOD_ID, modelId));

    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerModel.item();
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerModel.value();
    }
}
