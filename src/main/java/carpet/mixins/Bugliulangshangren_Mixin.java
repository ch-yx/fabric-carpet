package carpet.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(net.minecraft.world.level.block.piston.PistonBaseBlock.class)
public abstract class Bugliulangshangren_Mixin  {

    @Shadow
    boolean isSticky;
    
    @Inject(method = "triggerEvent", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
    target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;getNeighborSignal(Lnet/minecraft/world/level/SignalGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", ordinal = 0),
    cancellable = true
)
private void onExtendsOrRetracts(BlockState blockState, Level level, BlockPos blockPos, int i, int j, CallbackInfoReturnable<Boolean> cir, @Local Direction direction)
{
    
    Level serverLevel=(Level)level;
    PistonStructureResolver resolver;
    if (i == 0) {
        resolver = new PistonStructureResolver(serverLevel, blockPos, direction, true);
    } else {
        resolver = new PistonStructureResolver(serverLevel, blockPos.relative(direction), direction, false);
    }
    resolver.resolve();
    List<BlockPos> blocksMoved = resolver.getToPush();
    boolean cancelled = i == 0 ? PISTON_EXTENDS.onPistonExtendsOrRetracts(serverLevel, blockPos, direction, this.isSticky, blocksMoved) : PISTON_RETRACTS.onPistonExtendsOrRetracts(serverLevel, blockPos, direction, this.isSticky, blocksMoved);
    if (cancelled) cir.cancel();
}
}
