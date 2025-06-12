package carpet.mixins;

import carpet.fakes.DefaultRedstoneWireEvaluatorInferface;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.action.CommandTemplate;
import net.minecraft.server.dialog.action.CustomAll;
import net.minecraft.server.dialog.action.ParsedTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CommandTemplate.class)
/*public abstract class DialogCommandMixin
{
    private static final MapCodec<CommandTemplate> MAP_CODEC2 = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(ParsedTemplate.CODEC.fieldOf("template").forGetter(CommandTemplate::template)).apply(instance, CommandTemplate::new);
    });
    private static CommandTemplate new2(ParsedTemplate parsedTemplate){
        System.out.println("\n\n\n============chyx chyx==========\n"+parsedTemplate);
        return new CommandTemplate(parsedTemplate);
    }
    @Inject(method = "codec", cancellable = true,at = @At("RETURN"))
    private void injected(CallbackInfoReturnable<MapCodec> cir) {
        cir.setReturnValue(MAP_CODEC2);
    }
}*/

public class DialogCommandMixin {


    @Shadow @Final private ParsedTemplate template;

    private static CommandTemplate new2(ParsedTemplate parsedTemplate){
        var c=new CompoundTag();
        c.store("temp",ParsedTemplate.CODEC,parsedTemplate);

        List a=new ArrayList<>();
        a.add(new CustomAll(ResourceLocation.parse("chyx:command2run"), Optional.of(c)));
        List<CommandTemplate> b=a;
        System.out.println("\n\n\n============chyx chyx==========\n"+c);
        return  b.get(0);
    }
    @Accessor("MAP_CODEC")
    public static void setBiomes(MapCodec biomes) {
        throw new AssertionError();
    }
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void injected(CallbackInfo ci) {
        MapCodec<CommandTemplate> MAP_CODEC2 = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(ParsedTemplate.CODEC.fieldOf("template").forGetter(CommandTemplate::template)).apply(instance, DialogCommandMixin::new2);
        });
        setBiomes(MAP_CODEC2);
    }
}
