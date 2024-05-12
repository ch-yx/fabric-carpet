package carpet.mixins;

import carpet.CarpetSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.component.CustomData;

@Mixin(targets = "net.minecraft.world.item.ItemStack$1")
public class ItemStack_stackableShulkerBoxesEncodeMixin {
    private boolean hasCustomMaxStackSize(ItemStack i) {// maybe it is by some command block player
        int protosize = i.getPrototype().getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
        int thissize = i.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
        return protosize != thissize;
    }

    static private final String BoringMarker = "__carpet_data!!the_max_stack_size_tag_is_fake!__";

    @ModifyVariable(method = "encode", at = @At("HEAD"), ordinal = 0)
    private ItemStack getCMMAxStackSize(ItemStack itemstack) {
        if (itemstack.getOrDefault(DataComponents.MAX_STACK_SIZE, 1) < itemstack.getCount() && !hasCustomMaxStackSize(itemstack)) {
            itemstack = itemstack.copy();
            itemstack.set(DataComponents.MAX_STACK_SIZE, itemstack.getMaxStackSize());
            CustomData.update(DataComponents.CUSTOM_DATA, itemstack,
                    compoundTag -> compoundTag.putBoolean(BoringMarker, true));
        }
        return itemstack;
    }

    @Inject(method = "decode", at = @At("RETURN"), cancellable = true)
    private void getCMMAxStackSize(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemstack = cir.getReturnValue();
        if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(BoringMarker)) {
            itemstack = cir.getReturnValue().copy();
            itemstack.set(DataComponents.MAX_STACK_SIZE,itemstack.getPrototype().get(DataComponents.MAX_STACK_SIZE));
            CustomData.update(DataComponents.CUSTOM_DATA, itemstack, compoundTag -> compoundTag.remove(BoringMarker));
            itemstack.limitSize(itemstack.getMaxStackSize());
            cir.setReturnValue(itemstack);
        }
    }
}
