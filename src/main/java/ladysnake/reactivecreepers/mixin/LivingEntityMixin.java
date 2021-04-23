package ladysnake.reactivecreepers.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
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
