package carpet.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip.class)
public class BundleMixin2
{
    @Shadow
    private BundleContents contents;
    @Shadow
    private static boolean shouldRenderSurplusText(boolean bl, int i, int j) {
        return (Boolean)null;
    }
    @Shadow
    private List<ItemStack> getShownItems(int i) {
        return null;
    }
    @Shadow
    private int getContentXOffset(int i) {
        return (Integer)null;
    }
    @Shadow
    private int gridSizeY() {
        return (Integer)null;
    }

    @Shadow
    private void renderSlot(int i, int j, int k, List<ItemStack> list, int l, Font font, GuiGraphics guiGraphics) {
    }
    @Shadow
    private void drawSelectedItemTooltip(Font font, GuiGraphics guiGraphics, int i, int j, int k) {

    }
    @Shadow
    private int itemGridHeight() {
        return (Integer)null;
    }
    @Shadow
    private void drawProgressbar(int i, int j, Font font, GuiGraphics guiGraphics) {

    }
    @ModifyReturnValue(method = "slotCount",at = @At("RETURN"))
    private int x(int ori) {
        return contents.size();
    }
    @ModifyReturnValue(method = "getHeight",at = @At("RETURN"))
    private int fakeH(int ori) {
        return 145;
    }
    @ModifyReturnValue(method = "getWidth",at = @At("RETURN"))
    private int fakeW(int ori) {
        return ori;
    }

//    @ModifyReturnValue(method = "gridSizeY",at = @At("RETURN"))
//    private int xx(int ori) {
//        return ori+1;
//    }
    @WrapOperation(method = "renderBundleWithItemsTooltip",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BundleContents;size()I"))
    private int xxx(BundleContents instance, Operation<Integer> original){
        return -1;
    }
    @Inject(method = "renderBundleWithItemsTooltip",at=@At("HEAD"),cancellable = true)
    void xxx(Font font, int i, int j, int k, int l, GuiGraphics guiGraphics, CallbackInfo ci){
        List<ItemStack> list = this.getShownItems(this.contents.getNumberOfItemsToShow());
        this.drawSelectedItemTooltip(font, guiGraphics, i, j, k);
        this.drawProgressbar(i + this.getContentXOffset(k), j -5, font, guiGraphics);
        int o = 1;
        while (true){
            if (o>list.size()) break;
            double bl = list.size() - o - Math.max(this.contents.getSelectedItem(),0);
            bl/=list.size();
            bl+=bl<0?1:0;
            bl=Math.acos((0.5-bl)*2);
            var r=i+(int)(Math.sin(bl*2)*30)+25;
            var d=-(int)(Math.cos(bl*2)*96);
            if (d>0){d/=4;}
            var s=j+d+100;
            this.renderSlot(o, r, s, list, o, font, guiGraphics);
            ++o;
        }
        ci.cancel();
    }

}
