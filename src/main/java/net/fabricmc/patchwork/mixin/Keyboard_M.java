package net.fabricmc.patchwork.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class Keyboard_M {
    @Shadow protected MinecraftClient client;

    //MC-183776
    @Inject(method = "processF3", at = @At("RETURN"), cancellable = true)
    private void f3(int key, CallbackInfoReturnable<Boolean> b) {
        if (key == 293 && this.client.player.hasPermissionLevel(2)) {
            b.setReturnValue(false);
        }
    }
}
