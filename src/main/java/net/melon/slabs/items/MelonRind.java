package net.melon.slabs.items;

import net.melon.slabs.blocks.MelonSlabsBlocks;
import net.melon.slabs.text.MelonSlabsText;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import org.jetbrains.annotations.Nullable;

import java.util.List;

//this class exists to handle the intricacies of wearing a melon rind as a hard hat

public class MelonRind extends BlockItem implements Equipment{

    public MelonRind(Settings settings) {
        super(MelonSlabsBlocks.MELON_RIND, settings);
    }

    public EquipmentSlot getSlotType() {
        return(EquipmentSlot.HEAD);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("melonslabs.melon_rind_tooltip_1").setStyle(MelonSlabsText.ANNOTATION_STYLE));
        tooltip.add(Text.translatable("melonslabs.melon_rind_tooltip_2").setStyle(MelonSlabsText.ANNOTATION_STYLE));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
