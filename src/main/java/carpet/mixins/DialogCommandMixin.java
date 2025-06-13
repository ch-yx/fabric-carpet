package carpet.mixins;

import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.action.*;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Optional;

@Mixin(ServerCommonPacketListenerImpl.class)
public class DialogCommandMixin {
    @Shadow @Final protected Connection connection;

    @Inject(method = "handleCustomClickAction", at= @At("RETURN"),cancellable = true)
    void ttt(ServerboundCustomClickActionPacket serverboundCustomClickActionPacket, CallbackInfo ci){
        ResourceLocation resourceLocation= serverboundCustomClickActionPacket.id();
        Optional<Tag> optional=serverboundCustomClickActionPacket.payload();
        if (!resourceLocation.toString().equals("chyx:command2run")){
            return;
        }
        var m = new HashMap<String,String>();
        optional.get().asCompound().get().forEach((k,v)->{
            if(v.asString().isPresent())
                m.put(k,v.asString().get());
        });
        var t = optional.get().asCompound().get().read("temp",ParsedTemplate.CODEC);
        ParsedTemplate parsedTemplate = t.get();
        parsedTemplate.instantiate(m);
        System.out.println("\n\n\n\n"+resourceLocation+"\n"+optional.orElse(null)+"\n"+parsedTemplate.instantiate(m));
        ci.cancel();
        var p =ServerPlayerConnection.class.cast(this).getPlayer();
        var commandSource =p.createCommandSourceStack().withPermission(2).withSuppressedOutput() ;
        p.getServer().getCommands().performPrefixedCommand(commandSource,parsedTemplate.instantiate(m));
    }

}
