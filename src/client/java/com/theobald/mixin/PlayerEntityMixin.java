package com.theobald.mixin;

import com.moulberry.flashback.EnhancedFlight;
import com.moulberry.flashback.Flashback;
import com.moulberry.flashback.configuration.FlashbackConfig;
import com.theobald.FightCameraClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(value = PlayerEntity.class, priority = 1101)
public class PlayerEntityMixin {
//    @Inject(method="travel", at=@At(value = "HEAD"), cancellable = true)
//    public void cancelFlashbackMovement(Vec3d movementInput, CallbackInfo ci) {
//        if (FightCameraClient.getActive()) {
//            ci.cancel();
//        }
//    }
}
