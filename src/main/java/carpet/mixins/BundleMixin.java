package carpet.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(net.minecraft.world.item.component.BundleContents.class)
public class BundleMixin
{
    @ModifyReturnValue(method = "getNumberOfItemsToShow",at = @At("RETURN"))
    private int xx(int ori) {
       return ((net.minecraft.world.item.component.BundleContents)(Object)this).size();
    }
}
