package club.someoneice.humangunner.mixin;

import club.someoneice.humangunner.HumanGunner;
import com.craftix.hostile_humans.entity.entities.Human;
import com.craftix.hostile_humans.entity.entities.HumanInventoryGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanInventoryGenerator.class)
public class HumanInventoryGeneratorMixin {
    @Inject(method = {"generateInventory"}, at = {@At("RETURN")}, remap = false)
    private static void generateInventory(Human human, boolean forceRanged, CallbackInfo ci) {
        HumanGunner.hook(human, forceRanged);
    }
}
