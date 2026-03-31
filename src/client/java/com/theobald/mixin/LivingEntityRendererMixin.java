package com.theobald.mixin;

import com.theobald.FightCameraClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {

    @Unique
    ClientPlayerEntity clientPlayerEntity =
        MinecraftClient.getInstance().player;

    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    private void forceRenderNametag(
        LivingEntity entity,
        double squaredDistance,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (FightCameraClient.active) {
            cir.setReturnValue(true);
        }
    }
}
