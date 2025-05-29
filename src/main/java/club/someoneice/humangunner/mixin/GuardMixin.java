package club.someoneice.humangunner.mixin;

import club.someoneice.humangunner.GunnerGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestegg.guardvillagers.entities.Guard;

@Mixin(Guard.class)
public class GuardMixin {
    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void inject$registerGoals(CallbackInfo ci) {
        var thiz = (Guard) (Object) this;
        thiz.goalSelector.addGoal(0, new GunnerGoal<>(thiz));
    }
}
