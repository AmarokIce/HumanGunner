package club.someoneice.humangunner.mixin;

import club.someoneice.humangunner.GunnerGoal;
import club.someoneice.humangunner.HumanGunner;
import com.tacz.guns.api.entity.IGunOperator;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestegg.guardvillagers.entities.Guard;

@Mixin(Guard.class)
public abstract class GuardMixin implements IGunOperator {
    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void inject$registerGoals(CallbackInfo ci) {
        var thiz = (Guard) (Object) this;
        thiz.goalSelector.addGoal(-1, new GunnerGoal<>(thiz));
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("RETURN"))
    private void setWeaponGun(DifficultyInstance difficulty, CallbackInfo ci) {
        HumanGunner.hook((Guard) (Object) this);
    }

    @Override
    public boolean needCheckAmmo() {
        return false;
    }

    @Override
    public boolean consumesAmmoOrNot() {
        return false;
    }
}
