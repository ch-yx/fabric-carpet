package carpet.mixins;

import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImpl_scarpetCrafterScreen
{

    @Shadow
    public ServerPlayer player;

    @WrapOperation(method = "handleContainerSlotStateChanged(Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;)V", constant = @Constant(classValue = CrafterBlockEntity.class))
    private boolean injected(Object container, Operation<Boolean> org){//, ServerboundContainerSlotStateChangedPacket serverboundContainerSlotStateChangedPacket){//,@Local(ordinal = 0) CrafterMenu cm) {
        //System.out.println(cm ==(CrafterMenu)this.player.containerMenu);
/*         CrafterMenu cm = (CrafterMenu)this.player.containerMenu;
        if(!(org.call(container))){
            cm.setSlotState(1,false);
        }; */
        return org.call(container);
    }
}
