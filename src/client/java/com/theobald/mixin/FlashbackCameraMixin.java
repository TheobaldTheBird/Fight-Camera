package com.theobald.mixin;

import com.moulberry.flashback.Flashback;
import com.theobald.FightCameraClient;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = Flashback.class, remap = false)
public class FlashbackCameraMixin {
    @Inject(method = "isInReplay", at=@At("HEAD"), cancellable = true)
    private static void cancelInReplay(CallbackInfoReturnable<Boolean> cir) {
        if (FightCameraClient.getActive()) {
            cir.setReturnValue(false);
        }
    }
}
