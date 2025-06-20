package club.someoneice.humangunner.mixin;

import club.someoneice.humangunner.GunnerGoal;
import com.craftix.hostile_humans.entity.entities.Human;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Human.class)
public abstract class HumanMixin implements IGunOperator {
    @Unique
    private final GunnerGoal<Human> gunner$GunnerGoal = new GunnerGoal<>((Human) (Object) this);

    @Inject(method = {"setCombatTask"}, at = {@At("TAIL")}, remap = false, cancellable = true)
    public void onSetCombat(CallbackInfo ci) {
        Human thiz = (Human)(Object) this;

        if (thiz.getLevel().isClientSide) {
            return;
        }

        thiz.goalSelector.removeGoal(this.gunner$GunnerGoal);

        if (thiz.getMainHandItem().getItem() instanceof AbstractGunItem) {
            thiz.goalSelector.addGoal(-1, this.gunner$GunnerGoal);
            ci.cancel();
        }
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
