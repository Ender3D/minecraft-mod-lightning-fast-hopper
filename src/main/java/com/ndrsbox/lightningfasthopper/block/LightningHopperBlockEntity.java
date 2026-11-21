package com.ndrsbox.lightningfasthopper.block;

import com.ndrsbox.lightningfasthopper.Lightningfasthopper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import java.util.List;

public class LightningHopperBlockEntity extends HopperBlockEntity {
    public static BlockEntityType<LightningHopperBlockEntity> TYPE;

    private int customCooldownTicks = 0;

    public LightningHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return TYPE;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LightningHopperBlockEntity blockEntity) {
        if (blockEntity.customCooldownTicks > 0) {
            blockEntity.customCooldownTicks--;
        }

        if (blockEntity.customCooldownTicks <= 0) {
            boolean outputChanged = false;
            boolean inputChanged = false;

            if (!blockEntity.isEmpty()) {
                outputChanged = dispenseItemOutward(level, pos, state, blockEntity);
            }

            inputChanged = vacuumItemsFromAbove(level, pos, blockEntity);
        }
    }
    private static boolean vacuumItemsFromAbove(Level level, BlockPos pos, LightningHopperBlockEntity blockEntity) {

        BlockPos abovePos = pos.above();

        // --- PHASE A: PULL FROM CONTAINER ABOVE (Chests, Hoppers, etc.) ---
        Container containerAbove = HopperBlockEntity.getContainerAt(level, abovePos);
        if (containerAbove != null) {
            boolean containerChanged = false;

            for (int i = 0; i < containerAbove.getContainerSize(); i++) {
                ItemStack itemInAboveSlot = containerAbove.getItem(i);
                if (!itemInAboveSlot.isEmpty()) {
                    int available = itemInAboveSlot.getCount();

                    int batchTarget = Lightningfasthopper.CONFIG.pullEntireStack ? itemInAboveSlot.getMaxStackSize() : Lightningfasthopper.CONFIG.outputTransferBatchSize;
                    int amountToPull = Math.min(available, batchTarget);

                    ItemStack stackToInsert = itemInAboveSlot.copyWithCount(amountToPull);
                    ItemStack remainder = addItemToContainer(blockEntity, stackToInsert);

                    int movedAmount = amountToPull - remainder.getCount();
                    if (movedAmount > 0) {
                        itemInAboveSlot.shrink(movedAmount);
                        containerAbove.setItem(i, itemInAboveSlot);
                        containerAbove.setChanged();
                        containerChanged = true;

                        if (blockEntity.isEmpty() || remainder.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
            if (containerChanged) return true;
        }

        // PHASE 2: Vacuum floating item entities from world space (e.g., Water streams)
        AABB captureZone = new AABB(abovePos);
        List<ItemEntity> itemsInWater = level.getEntitiesOfClass(ItemEntity.class, captureZone);

                for (ItemEntity itemEntity : itemsInWater) {
                if (itemEntity.isAlive()) {
        ItemStack floatingStack = itemEntity.getItem();

        int maxCapacity = floatingStack.getMaxStackSize();
        int originalCount = floatingStack.getCount();

        int amountToPull = Lightningfasthopper.CONFIG.pullEntireStack ? originalCount : 1;
        amountToPull = Math.min(amountToPull, maxCapacity);

        ItemStack stackToInsert = floatingStack.copyWithCount(amountToPull);
        ItemStack remainder = addItemToContainer(blockEntity, stackToInsert);

        int acceptedAmount = amountToPull - remainder.getCount();

                        if (acceptedAmount > 0) {
                if (acceptedAmount == originalCount) {
                itemEntity.discard();
                            } else {
                                    floatingStack.shrink(acceptedAmount);
                                itemEntity.setItem(floatingStack);
                            }
                                    return true;
                                    }
                                    }
                                    }
                                    return false;
                                    }

private static boolean dispenseItemOutward(Level level, BlockPos pos, BlockState state, LightningHopperBlockEntity blockEntity) {
    Direction facing = state.getValue(HopperBlock.FACING);
    BlockPos targetPos = pos.relative(facing);

    Container targetContainer = HopperBlockEntity.getContainerAt(level, targetPos);
    if (targetContainer != null) {

        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
            ItemStack itemInSlot = blockEntity.getItem(i);
            if (!itemInSlot.isEmpty()) {
                int available = itemInSlot.getCount();
                int batchTarget = Lightningfasthopper.CONFIG.outputTransferBatchSize;

                int amountToPush = Math.min(Math.min(batchTarget, available), itemInSlot.getMaxStackSize());

                ItemStack stackToInsert = itemInSlot.copyWithCount(amountToPush);
                ItemStack remainder = addItemToContainer(targetContainer, stackToInsert);

                int movedAmount = amountToPush - remainder.getCount();
                if (movedAmount > 0) {
                    itemInSlot.shrink(movedAmount);
                    blockEntity.setItem(i, itemInSlot);
                    targetContainer.setChanged();
                    return true;
                }
            }
        }
    }
    return false;
}

private static ItemStack addItemToContainer(Container container, ItemStack stack) {
    for (int i = 0; i < container.getContainerSize(); i++) {
        if (container.canPlaceItem(i, stack)) {
            ItemStack slotStack = container.getItem(i);

            int maxStackLimit = Math.min(stack.getMaxStackSize(), container.getMaxStackSize());

            if (slotStack.isEmpty()) {
                int transfer = Math.min(stack.getCount(), maxStackLimit);
                container.setItem(i, stack.copyWithCount(transfer));
                stack.shrink(transfer);
                if (stack.isEmpty()) return ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(slotStack, stack) && slotStack.getCount() < maxStackLimit) {
                int spaceLeft = maxStackLimit - slotStack.getCount();
                int transferAmount = Math.min(spaceLeft, stack.getCount());
                slotStack.grow(transferAmount);
                stack.shrink(transferAmount);
                if (stack.isEmpty()) return ItemStack.EMPTY;
            }
        }
    }
    return stack;
}
}
