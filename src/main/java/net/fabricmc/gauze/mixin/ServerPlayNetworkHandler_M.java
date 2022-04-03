package net.fabricmc.gauze.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandler_M {
    @Shadow public ServerPlayerEntity player;

    @Shadow
    private static boolean canPlace(ServerPlayerEntity player, ItemStack stack) {
        return false;
    }

    //MC-129886
    boolean changed = false;
    @Redirect(method =  "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopY()I"))
    public int changeY(World instance, PlayerInteractBlockC2SPacket packet) {
        changed = false;
        BlockHitResult blockHitResult = packet.getBlockHitResult();
        BlockPos blockPos = blockHitResult.getBlockPos();
        int j = instance.getBottomY();
        if (blockPos.getY() < j) {
            Text text2 = (new TranslatableText("build.tooLow", new Object[]{j})).formatted(Formatting.RED);
            this.player.sendMessage(text2, MessageType.GAME_INFO, Util.NIL_UUID);
            changed = true;
        }
        return instance.getTopY();
    }
    @Redirect(method = "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ActionResult;shouldSwingHand()Z"))
    public boolean belowBottomHeight(ActionResult instance, PlayerInteractBlockC2SPacket packet) {
        int j = this.player.world.getBottomY();
        BlockHitResult blockHitResult = packet.getBlockHitResult();
        Direction direction = blockHitResult.getSide();
        BlockPos blockPos = blockHitResult.getBlockPos();
        ServerWorld serverWorld = this.player.getWorld();
        Hand hand = packet.getHand();
        ItemStack itemStack = this.player.getStackInHand(hand);
        ActionResult actionResult = this.player.interactionManager.interactBlock(this.player, serverWorld, itemStack, hand, blockHitResult);
        if (!changed && direction == Direction.DOWN && !actionResult.isAccepted() && blockPos.getY() <= j && canPlace(this.player, itemStack)) {
            Text text2 = (new TranslatableText("build.tooLow", new Object[]{j})).formatted(Formatting.RED);
            this.player.sendMessage(text2, MessageType.GAME_INFO, Util.NIL_UUID);
            return false;
        } else if (changed) {
            return false;
        }
        return actionResult.shouldSwingHand();
    }
}
