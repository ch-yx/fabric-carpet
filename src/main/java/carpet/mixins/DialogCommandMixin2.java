package carpet.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dialog.action.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ActionTypes.class)
public class DialogCommandMixin2 {
    private static final MapCodec<CustomAll> MAP_CODEC2 = RecordCodecBuilder.mapCodec(
    instance -> instance.group(ParsedTemplate.CODEC.fieldOf("template").forGetter((CustomAll x)->{

                var t = x.additions().get().read("temp",ParsedTemplate.CODEC);
                ParsedTemplate parsedTemplate = t.get();

                return parsedTemplate;}))
                        .apply(instance, (ParsedTemplate x)->{
                            var c=new CompoundTag();
                            c.store("temp",ParsedTemplate.CODEC,x);

                            return new CustomAll(ResourceLocation.parse("chyx:command2run"), Optional.of(c));})
	);
    @WrapOperation(
            method = "bootstrap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private static Object xxx(Registry registry, ResourceLocation resourceLocation, Object object, Operation<Object> original){
        if (resourceLocation.getPath().equals("dynamic/run_command")){
            System.out.println("\n\n\ndetected!!!!!!\n\n\n");
            return original.call(registry,resourceLocation,MAP_CODEC2);
        }
        return original.call(registry,resourceLocation,object);

    }

}
