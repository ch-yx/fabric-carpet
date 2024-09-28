package carpet.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.item.component.BundleContents;

@Mixin(net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip.class)
public class BundleMixin2
{
    @Shadow
    private BundleContents contents;
    
    @ModifyReturnValue(method = "slotCount",at = @At("RETURN"))
    private int xx(int ori) {
        return contents.size();
    }
}
