package ladysnake.reactivecreepers.mixin;

import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends LivingEntityMixin implements Monster {
    @Shadow
    protected abstract void explode();

    @Shadow
    public abstract void ignite();

    @Shadow
    private int fuseTime;

    @Shadow
    @Final
    private static TrackedData<Boolean> CHARGED;

    protected CreeperEntityMixin(EntityType<? extends CreeperEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected float onDamage(DamageSource source, float amount) {
        if (source.isExplosive()) {
            if (shouldCharge(source)) {
                this.getDataTracker().set(CHARGED, true);
                return 1F;
            } else {
                this.ignite();
                return 0.0F;
            }
        }

        if (source.isFire() && !this.getDataTracker().get(CHARGED)) {
            this.ignite();
        }

        if (source.isFromFalling()) {
            return 0;
        }

        return amount;
    }

    private boolean shouldCharge(DamageSource source) {
        return source.getAttacker() instanceof CreeperEntity && source.getAttacker().getDataTracker().get(CHARGED) && !this.getDataTracker().get(CHARGED);
    }

    // bigger follow range and faster speed
    @Inject(method = "createCreeperAttributes", at = @At("RETURN"), cancellable = true)
    private static void createCreeperAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D));
    }

    @Inject(method = "ignite", at = @At("RETURN"))
    public void ignite(CallbackInfo ci) {
        this.fuseTime = this.random.nextInt(30);
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.set(CHARGED, random.nextInt(10) == 0);
    }

}
