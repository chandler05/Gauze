package net.fabricmc.gauze.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockState_M {

    @Shadow public abstract Block getBlock();

    @Shadow protected abstract BlockState asBlockState();

    // Make comparator behaviour editable by extensions
    @Inject(method = "hasComparatorOutput", at = @At("RETURN"), cancellable = true)
    public void hasComparatorOutput(CallbackInfoReturnable<Boolean> cir) {
        if (this.getBlock() instanceof CampfireBlock) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "getComparatorOutput", at = @At("RETURN"), cancellable = true)
    public void getComparatorOutput(World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (this.getBlock() instanceof CampfireBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CampfireBlockEntity cBE) {
                int lit = this.asBlockState().get(CampfireBlock.LIT) ? 1 : 0;
                int signal = this.asBlockState().get(CampfireBlock.SIGNAL_FIRE) ? 1 : 0;
                DefaultedList<ItemStack> food = cBE.getItemsBeingCooked();
                int foodFound = 0;
                for (ItemStack itemStack : food) {
                    if (itemStack != ItemStack.EMPTY) {
                        foodFound++;
                    }
                }
                int output = (foodFound * 3) + lit + signal;
                cir.setReturnValue(output);
            }
        }
    }
}
