package net.fabricmc.gauze.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntity_M {
    @Shadow protected Entity holdingEntity;

    //MC-16663
    @Inject(method = "detachLeash", at = @At("HEAD"), cancellable = true)
    private void detachLeash(boolean sendPacket, boolean dropItem, CallbackInfo ci) {
        if (holdingEntity != null && holdingEntity instanceof LeashKnotEntity) {
            this.holdingEntity.discard();
        }
    }
}
