package net.fabricmc.patchwork.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( FishingBobberEntityRenderer.class )
public abstract class FishingBobberEntityRenderer_M {

    //MC-116379
    @ModifyVariable(method = "render", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
    private float swingProg(float p, FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        PlayerEntity pE = fishingBobberEntity.getPlayerOwner();
        ItemStack itemStack = pE.getMainHandStack();
        if (!itemStack.isOf(Items.FISHING_ROD)) {
            return 0f;
        }
        return p;
    }
}
