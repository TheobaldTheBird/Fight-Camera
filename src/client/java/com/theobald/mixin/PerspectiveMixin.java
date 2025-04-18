package com.theobald.mixin;

import com.theobald.FightCameraClient;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Perspective.class)
public class PerspectiveMixin {

    @Inject(method = "isFirstPerson", at = @At("HEAD"), cancellable = true)
    private void forceThirdPerson(CallbackInfoReturnable<Boolean> cir) {
        if (FightCameraClient.getActive())
        {
            cir.setReturnValue(false);
        }
    }
}

