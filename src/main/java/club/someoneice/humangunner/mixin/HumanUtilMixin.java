package club.someoneice.humangunner.mixin;

import com.craftix.hostile_humans.HumanUtil;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HumanUtil.class)
public class HumanUtilMixin {
    @Inject(method = {"isRangedWeapon"}, at = {@At("RETURN")}, remap = false, cancellable = true)
    private static void isRangedWeapon(ItemStack value, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        cir.setReturnValue(value.getItem() instanceof AbstractGunItem);
    }
}
