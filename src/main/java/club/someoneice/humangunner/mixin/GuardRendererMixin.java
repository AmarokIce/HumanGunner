package club.someoneice.humangunner.mixin;

import com.tacz.guns.api.item.IGun;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tallestegg.guardvillagers.client.renderer.GuardRenderer;
import tallestegg.guardvillagers.entities.Guard;

@Mixin(GuardRenderer.class)
public class GuardRendererMixin {
    @Inject(method = "getArmPose", at = @At("HEAD"), remap = false, cancellable = true)
    private void inject$getArmPose(Guard entityIn, ItemStack itemStackMain, ItemStack itemStackOff, InteractionHand handIn, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = entityIn.getMainHandItem();
        if (!(stack.getItem() instanceof IGun)) {
            return;
        }
        cir.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
    }
}
