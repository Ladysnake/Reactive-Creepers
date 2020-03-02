package ladysnake.reactivecreepers.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntityMixin {
    @Shadow protected abstract void explode();

    @Shadow public abstract void setIgnited();

    @Shadow private int fuseTime;

    @Shadow @Final private static TrackedData<Boolean> CHARGED;

    protected CreeperEntityMixin(EntityType<? extends MobEntityWithAi> type, World world) {
        super(type, world);
    }

    @Override
    protected float onDamage(DamageSource source, float amount) {
        if (source.isExplosive()) {
            if (shouldCharge(source)) {
                this.getDataTracker().set(CHARGED, true);
                return 1F;
            } else {
                this.fuseTime = 0;
                this.setIgnited();
                return 0.0F;
            }
        }
        if (source.isFire() && !this.getDataTracker().get(CHARGED)) {
            this.setIgnited();
        }
        return amount;
    }

    private boolean shouldCharge(DamageSource source) {
        return source.getAttacker() instanceof CreeperEntity && source.getAttacker().getDataTracker().get(CHARGED) && !this.getDataTracker().get(CHARGED);
    }
}
