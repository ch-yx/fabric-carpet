package carpet.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Zombie.ZombieGroupData;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class AllNPCRespawnMixin extends LivingEntity {

    protected AllNPCRespawnMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Unique
    private static RandomSource _random = RandomSource.create();
    static{
        System.out.println("\n\n\n\n\nLOADED!\n     ++++   LOADED!LOADED!\n\n\n");
    }
    @Inject(method = "killedEntity", at = @At("RETURN"), cancellable = true)
    private void inj(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }
        if (livingEntity instanceof AbstractVillager villager) {
            ZombieVillager zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
            if (zombieVillager != null) {
                zombieVillager.setCustomName(villager.getName());
                zombieVillager.finalizeSpawn(serverLevel,
                        serverLevel.getCurrentDifficultyAt(zombieVillager.blockPosition()),
                        EntitySpawnReason.CONVERSION, new ZombieGroupData(false, true));
                while (zombieVillager.getVillagerData().getProfession() == VillagerProfession.NITWIT
                        || zombieVillager.getVillagerData().getProfession() == VillagerProfession.NONE) {
                    BuiltInRegistries.VILLAGER_PROFESSION.getRandom(_random)
                            .ifPresent(reference -> zombieVillager.setVillagerData(zombieVillager.getVillagerData()
                                    .setProfession((VillagerProfession) reference.value())));
                }
                zombieVillager
                        .setVillagerData(zombieVillager.getVillagerData().setLevel(VillagerData.MAX_VILLAGER_LEVEL));
                zombieVillager.setTradeOffers(villager.getOffers().copy());
                zombieVillager.setVillagerXp(villager.getVillagerXp());
                if (!this.isSilent()) {
                    serverLevel.levelEvent(null, 1026, this.blockPosition(), 0);
                }
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void inj(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if(isCurrentlyGlowing()){
            cir.setReturnValue(super.hurt(damageSource,f));
            return;
        }
        cir.setReturnValue(false);
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        for (int opop = 0; opop < 1000; opop++) {
            
        
        ServerLevel serverLevel = (ServerLevel)level;
        EntityType<? extends Zombie> entityType = ((Zombie)(Object)this).getType();
        Zombie zombie = entityType.create(this.level(), EntitySpawnReason.REINFORCEMENT);
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY());
        int k = Mth.floor(this.getZ()+3);
        BlockPos blockPos = new BlockPos(i,j,k);
        if (!SpawnPlacements.isSpawnPositionOk(entityType, this.level(), blockPos) || !SpawnPlacements.checkSpawnRules(entityType, serverLevel, EntitySpawnReason.REINFORCEMENT, blockPos, this.level().random)) continue;
        zombie.setPos(i, j, k);
        if ( !this.level().isUnobstructed(zombie) || !this.level().noCollision(zombie) || this.level().containsAnyLiquid(zombie.getBoundingBox())) continue;
        serverLevel.addFreshEntityWithPassengers(zombie);
        zombie.setGlowingTag(true);
        return;
        }
    }
}
