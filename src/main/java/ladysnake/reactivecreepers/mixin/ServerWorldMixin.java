package ladysnake.reactivecreepers.mixin;

import ladysnake.reactivecreepers.world.CustomCreeperExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @ModifyVariable(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;", at = @At("STORE"))
    private Explosion createExplosion(Explosion explosion, Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType) {
        if (entity instanceof CreeperEntity) {
            return new CustomCreeperExplosion((World) (Object) this, entity, damageSource, behavior, x, y, z, power, destructionType);
        } else {
            return explosion;
        }
    }
}
