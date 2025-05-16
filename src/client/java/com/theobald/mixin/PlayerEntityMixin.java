package com.theobald.mixin;

import com.theobald.FightCameraClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(value = PlayerEntity.class, priority = 1101)
public abstract class PlayerEntityMixin {
    @Shadow public abstract boolean isMainPlayer();

    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void disableMovement(CallbackInfo ci) {
        if (this.isMainPlayer() && FightCameraClient.active) {
            ci.cancel();
        }
    }
}
