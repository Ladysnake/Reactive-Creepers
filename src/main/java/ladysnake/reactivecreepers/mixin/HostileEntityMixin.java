package ladysnake.reactivecreepers.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends MobEntityWithAi implements Monster {
    protected HostileEntityMixin(EntityType<? extends MobEntityWithAi> type, World world) {
        super(type, world);
    }

    @ModifyVariable(at = @At(value = "HEAD"), method = "damage", argsOnly = true)
    private float damage(float amount, DamageSource source) {
        return this.onDamage(source, amount);
    }

    @Unique
    protected float onDamage(DamageSource source, float amount) {
        return amount;
    }

}
