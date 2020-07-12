package dev.eeasee.eeaseemod.mixin;

import dev.eeasee.eeaseemod.config.Configs;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity> {
    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V")
    )
    private void dinnerboned(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        if (Configs.ENABLE_CUSTOM_DINNERBONES.getBooleanValue()) {
            matrixStack.translate(0.0F, (float) (livingEntity.getHeight() * Configs.ENTITY_Y_SHIFT.getDoubleValue()), 0.0F);
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float)Configs.ENTITY_Z_ROTATION.getDoubleValue()));
        }
        if (Configs.ENTITY_Y_ROTATE_WITH_POSITION.getBooleanValue()){
            float rotation = (float) ((livingEntity.getX() + livingEntity.getY() + livingEntity.getZ()) * 40 * Configs.ENTITY_SPINNING_SPEED.getDoubleValue()) % 360 + 180;
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        }
    }
}
