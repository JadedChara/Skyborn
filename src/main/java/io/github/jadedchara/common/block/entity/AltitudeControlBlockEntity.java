package io.github.jadedchara.common.block.entity;

import io.github.jadedchara.client.screen.AltitudeScreenHandler;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltitudeControlBlockEntity extends BlockEntity implements MenuProvider, Container, WorldlyContainer {
    public float altitude = 90.0F;
    private final NonNullList<ItemStack> fauxInventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public AltitudeControlBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkybornBlocks.ALTITUDE_CONTROL_BLOCKENTITY, blockPos, blockState);
    }
    public float getAltitude(){
        return altitude;
    };
    public void setAltitude(float a) {
        altitude = a;
    };

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        nbt.putFloat("Altitude", altitude);

        super.saveAdditional(nbt, registryLookup);
    }
    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        altitude = nbt.getFloat("Altitude");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AltitudeScreenHandler(i,inventory,this,this.getAltitude());
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {

    }
}
