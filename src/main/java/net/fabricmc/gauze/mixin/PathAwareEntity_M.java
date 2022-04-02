package net.fabricmc.gauze.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathAwareEntity.class)
public abstract class PathAwareEntity_M extends Entity {
    public PathAwareEntity_M(EntityType<?> type, World world) {
        super(type, world);
    }

    //MC-14167
    @Inject(method = "updateLeash", at = @At("TAIL"))
    private void resetFallDistance(CallbackInfo ci) {
        if (this.getVelocity().getY() >= 0) {
            fallDistance = 0;
        }
    }
}
