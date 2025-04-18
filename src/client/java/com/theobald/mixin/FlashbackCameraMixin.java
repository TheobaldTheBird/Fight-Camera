package com.theobald.mixin;

import com.moulberry.flashback.Flashback;
import com.theobald.FightCameraClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Flashback.class)
public class FlashbackCameraMixin {
//    @Inject(
//            method = "isInReplay()Z", // Z = boolean return type
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private static void disableReplayDetection(CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(false);
//    }
}
