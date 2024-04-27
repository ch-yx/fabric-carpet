package carpet.mixins;

import carpet.CarpetSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(targets = "net.minecraft.world.item.ItemStack$1")
public class ItemStack_stackableShulkerBoxesEncodeMixin
{
    @ModifyVariable(method = "encode", at = @At("HEAD"),  ordinal = 0)
    private ItemStack getCMMAxStackSize(ItemStack itemstack)
    {
        if (CarpetSettings.shulkerBoxStackSize > 1
                && itemstack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ShulkerBoxBlock
        ) {
            itemstack=itemstack.copy();
            itemstack.set(DataComponents.MAX_STACK_SIZE,itemstack.getMaxStackSize());
        }
        return itemstack;
    }
}
