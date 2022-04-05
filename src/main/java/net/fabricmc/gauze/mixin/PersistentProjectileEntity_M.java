package net.fabricmc.gauze.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntity_M {

    @Shadow private SoundEvent sound;

    @Shadow protected abstract SoundEvent getHitSound();

    //MC-153929
    @Redirect(method = "onBlockHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setSound(Lnet/minecraft/sound/SoundEvent;)V"))
    public void setSound(PersistentProjectileEntity instance, SoundEvent sound) {
        this.sound = this.getHitSound();
    }
}
