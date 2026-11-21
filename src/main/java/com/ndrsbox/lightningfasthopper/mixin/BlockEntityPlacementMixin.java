package com.ndrsbox.lightningfasthopper.mixin;

import com.ndrsbox.lightningfasthopper.block.LightningHopperBlock;
import com.ndrsbox.lightningfasthopper.block.LightningHopperBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityPlacementMixin {
    @Inject(method = "validateBlockState", at = @At("HEAD"), cancellable = true)
    private void allowCustomHopperPlacement(BlockState state, CallbackInfo ci) {
        BlockEntity self = (BlockEntity) (Object) this;
        // If this is our custom block entity and it's being placed inside our custom block, cancel the vanilla mismatch check!
        if (self instanceof LightningHopperBlockEntity && state.getBlock() instanceof LightningHopperBlock) {
            ci.cancel();
        }
    }
}
