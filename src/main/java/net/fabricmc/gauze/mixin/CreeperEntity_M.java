package net.fabricmc.gauze.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(CreeperEntity.class)
public class CreeperEntity_M {

    //MC-185618
    @Redirect(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean fireCharge(ItemStack is, Item i) {
        return is.isOf(i) || is.isOf(Items.FIRE_CHARGE);
    }
    @Redirect(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private <T extends LivingEntity> void use(ItemStack instance, int amount, T entity, Consumer<T> breakCallback, PlayerEntity p, Hand h) {
        if (instance.isOf(Items.FLINT_AND_STEEL)) {
            instance.damage(1, entity, (playerx) -> {
                playerx.sendToolBreakStatus(h);
            });
        } else if (instance.isOf(Items.FIRE_CHARGE)) {
            instance.decrement(1);
        }
    }
}
