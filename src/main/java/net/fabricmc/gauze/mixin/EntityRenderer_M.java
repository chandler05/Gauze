package net.fabricmc.gauze.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRenderer_M {

    //MC-132199
    @Redirect(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"))
    public <T extends Entity> int drawColor(TextRenderer instance, Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, T entity) {
        if (entity instanceof ItemFrameEntity) {
            Formatting formatting = ((ItemFrameEntity) entity).getHeldItemStack().getRarity().formatting;
            if (formatting.getColorValue() != null) {
                return instance.draw(text, x, y, formatting.getColorValue(), shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
            }
        }
        return instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }
}
