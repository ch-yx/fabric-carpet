package carpet.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

@Mixin(net.minecraft.world.entity.vehicle.AbstractMinecart.class)
public abstract class CrossingMinecartMixin

{
    final AbstractMinecart th() {
        return (AbstractMinecart) (Object) this;
    }
    static private boolean glob_flag=false;
    static private boolean iscrossing(Level level,BlockPos blockPos){
        
        for (Direction direction : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
            BlockPos side=blockPos.relative(direction);
            boolean result=false;
            for (BlockPos rail : new BlockPos[]{side, side.above(), side.below()}){
                BlockState blockState = level.getBlockState(rail);
                if (!BaseRailBlock.isRail((BlockState) blockState)){
                    continue;
                };
                RailShape railShape = (RailShape) blockState
                    .getValue(((BaseRailBlock) blockState.getBlock()).getShapeProperty());
                var pair = AbstractMinecart.exits(railShape);
                Vec3i exit1 = pair.getFirst();
                Vec3i exit2 = pair.getSecond();
                if (railShape.isSlope()) {
                    exit1=exit1.above();
                    exit2=exit2.above();
                }
                if (rail.offset(exit1).equals(blockPos)|rail.offset(exit2).equals(blockPos)) {
                    result=true;
                    break;
                }
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }
    @WrapOperation(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/MinecartBehavior;moveAlongTrack()V"))
    private void mix(net.minecraft.world.entity.vehicle.MinecartBehavior instance, Operation<Void> original) {
        if (glob_flag) {
            original.call(instance);
            return;
        }
        Vec3 deltaMovement = th().getDeltaMovement();
        if (deltaMovement.length() > (double) 1.0E-5f) {
            BlockPos blockPos = th().getCurrentBlockPosOrRailBelow();
            BlockState blockState = th().level().getBlockState(blockPos);
            

            if (BaseRailBlock.isRail((BlockState) blockState)&&iscrossing(th().level(), blockPos)) {
                RailShape railShape = (RailShape) blockState
                    .getValue(((BaseRailBlock) blockState.getBlock()).getShapeProperty());
                var pair = AbstractMinecart.exits(railShape);
                Vec3i exit1 = pair.getFirst();
                Vec3i exit2 = pair.getSecond();
                if (exit1.multiply(-1).equals(exit2)) {
                    deltaMovement=deltaMovement.normalize();
                    Vec3 exit = new Vec3(exit1).horizontal();
                    double along = exit.dot(deltaMovement);
                    along = along > 0 ? along : -along;
                    double nalong = exit.cross(deltaMovement).length();
                    if (nalong > along) {
                        glob_flag=true;
                        th().level().setBlock(blockPos, blockState.rotate(net.minecraft.world.level.block.Rotation.CLOCKWISE_90), 0, 0);
                        try {
                            original.call(instance);
                        } finally {
                            th().level().setBlock(blockPos, blockState, 0, 0);
                            glob_flag=false;
                        }
                        return;
                    } else {
                        original.call(instance);
                        return;
                    }
                } else {
                    original.call(instance);
                    return;
                }
            } else {
                original.call(instance);
                return;
            }

        } else {
            original.call(instance);
            return;
        }
    }


    private boolean isDecending(Vec3 vec3, RailShape railShape) {
        return switch (railShape) {
            case RailShape.ASCENDING_EAST -> {
                if (vec3.x < 0.0) {
                    yield true;
                }
                yield false;
            }
            case RailShape.ASCENDING_WEST -> {
                if (vec3.x > 0.0) {
                    yield true;
                }
                yield false;
            }
            case RailShape.ASCENDING_NORTH -> {
                if (vec3.z > 0.0) {
                    yield true;
                }
                yield false;
            }
            case RailShape.ASCENDING_SOUTH -> {
                if (vec3.z < 0.0) {
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }
    private boolean isDecending2(Vec3 vec3, RailShape railShape) {
        return switch (railShape) {
            case RailShape.ASCENDING_EAST -> (vec3.x < 0.0) ;
            
            case RailShape.ASCENDING_WEST -> 
                 (vec3.x > 0.0) ;
            
            case RailShape.ASCENDING_NORTH -> 
                 (vec3.z > 0.0) ;
            
            case RailShape.ASCENDING_SOUTH -> 
                  (vec3.z < 0.0);
            
            default -> false;
        };
    }
}
