package club.someoneice.humangunner.mixin;

import com.craftix.hostile_humans.client.renderer.HumanRenderer;
import com.craftix.hostile_humans.entity.entities.Human;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanRenderer.class)
public abstract class HumanRenderMixin {
    @Shadow(remap = false)
    protected abstract void setHandPose(Human paramHuman, HumanoidModel.ArmPose paramArmPose);

    @Inject(method = {
            "render(Lcom/craftix/hostile_humans/entity/entities/Human;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"},
            at = {@At("TAIL")}, remap = false)
    private void inject$getArmPose(Human human, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        ItemStack stack = human.getMainHandItem();
        if (!(stack.getItem() instanceof IGun)) {
            return;
        }

        setHandPose(human, HumanoidModel.ArmPose.BOW_AND_ARROW);
    }
}
