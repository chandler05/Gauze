package net.fabricmc.gauze.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManager_M {

    @Shadow private int blockBreakingCooldown;

    @Shadow @Final private MinecraftClient client;

    //MC-202070
    @Redirect(method = "attackBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 1))
    public boolean setCooldown(ClientPlayerInteractionManager instance, BlockPos pos) {
        if (this.client.world.getBlockState(pos).getBlock().getHardness() == 0.0f) {
            this.blockBreakingCooldown = 5;
        }
        return instance.breakBlock(pos);
    }
}
