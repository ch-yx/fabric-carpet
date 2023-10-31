package carpet.mixins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@Mixin(net.minecraft.world.level.block.entity.CrafterBlockEntity.class)
public interface CrafterAccessMixin {
    @Invoker("createMenu")
    AbstractContainerMenu InvokerCreateMenu(int i, Inventory inventory) ;

}