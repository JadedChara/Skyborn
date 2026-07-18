package io.github.jadedchara.client.screen;

import io.github.jadedchara.Skyborn;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FuelAccessHandler extends AbstractContainerMenu {
    private final Container inventory;
    public FuelAccessHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(18));
    }
    public FuelAccessHandler(int syncId, Inventory pl, Container inventory) {
        super(Skyborn.FUEL_ACCESS_HANDLER, syncId);
        checkContainerSize(inventory, 18);
        this.inventory = inventory;

        // some inventories do custom logic when a player opens it.
        inventory.startOpen(pl.player);

        int m;
        int l;
        // Our inventory
        for (m = 0; m < 2; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + m * 9, 8 + l * 18, 17 + m * 18));
            }
        }
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(pl, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(pl, m, 8 + m * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (i < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        this.inventory.setChanged();

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
}
