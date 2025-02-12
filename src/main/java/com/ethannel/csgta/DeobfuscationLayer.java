package com.ethannel.csgta;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

public class DeobfuscationLayer {

    public static Minecraft mc;

    public static GuiScreen getCurrentScreen() {
        return mc.currentScreen;
    }

    public static boolean isGuiContainer(GuiScreen guiScreen) {
        return guiScreen instanceof GuiContainer;
    }

    public static boolean isValidGuiContainer(GuiScreen guiScreen) {
        return (guiScreen != null) && !(guiScreen.getClass()
            .getSimpleName()
            .contains("CJB_GuiCrafting"))
            && !(guiScreen.getClass()
                .equals(GuiContainerCreative.class));
    }

    public static boolean isVanillaCraftingOutputSlot(Container container, Slot slot) {
        return ((container instanceof ContainerWorkbench) && (getSlotNumber(slot) == 0))
            || ((container instanceof ContainerPlayer) && (getSlotNumber(slot) == 0))
            || ((container instanceof ContainerFurnace) && (getSlotNumber(slot) == 2))
            || ((container instanceof ContainerRepair) && (getSlotNumber(slot) == 2));
    }

    public static GuiContainer asGuiContainer(GuiScreen guiScreen) {
        return (GuiContainer) guiScreen;
    }

    public static Container asContainer(Object obj) {
        return (Container) obj;
    }

    public static Slot asSlot(Object obj) {
        return (Slot) obj;
    }

    public static Container getContainer(GuiContainer guiContainer) {
        return guiContainer.inventorySlots;
    }

    public static List<Slot> getSlots(Container container) {
        return container.inventorySlots;
    }

    public static Slot getSlot(Container container, int index) {
        return getSlots(container).get(index);
    }

    public static ItemStack getSlotStack(Slot slot) {
        return (slot == null) ? null : slot.getStack();
    }

    public static int getWindowId(Container container) {
        return container.windowId;
    }

    public static void windowClick(int windowId, int slotNumber, int mouseButton, int shiftPressed) {
        // if (slotNumber != -1) {
        getPlayerController().windowClick(windowId, slotNumber, mouseButton, shiftPressed, getThePlayer());
        // }
    }

    public static EntityClientPlayerMP getThePlayer() {
        return mc.thePlayer;
    }

    public static InventoryPlayer getInventoryPlayer() {
        return getThePlayer().inventory;
    }

    public static int getDisplayWidth() {
        return mc.displayWidth;
    }

    public static int getDisplayHeight() {
        return mc.displayHeight;
    }

    public static ItemStack getStackOnMouse() {
        return getInventoryPlayer().getItemStack();
    }

    public static PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    public static IInventory getSlotInventory(Slot slot) {
        return slot.inventory;
    }

    public static int getSlotNumber(Slot slot) {
        return slot.slotNumber;
    }

    public static int getSlotYPosition(Slot slot) {
        return slot.yDisplayPosition;
    }

    public static int getItemStackSize(ItemStack itemStack) {
        return itemStack.stackSize;
    }

    public static int getMaxItemStackSize(ItemStack itemStack) {
        return itemStack.getMaxStackSize();
    }

    public static ItemStack copyItemStack(ItemStack itemStack) {
        return (itemStack == null) ? null : itemStack.copy();
    }

    public static boolean areStacksCompatible(ItemStack itemStack1, ItemStack itemStack2) {
        return ((itemStack1 == null) || (itemStack2 == null))
            || (itemStack1.isItemEqual(itemStack2) && ItemStack.areItemStackTagsEqual(itemStack1, itemStack2));
    }

    public static Slot getSelectedSlot(GuiContainer guiContainer, Container container, int slotCount) {
        for (int i = 0; i < slotCount; i++) {
            Slot slot = getSlot(container, i);
            if (guiContainer.isMouseOverSlot(slot, getRequiredMouseX(), getRequiredMouseY())) return slot;
        }

        return null;
    }

    /**
     * Disables the vanilla RMB drag mechanic in the given GuiContainer. If your
     * guiContainer is based on the vanilla
     * GuiContainer, you can use this method to disable the RMB drag.
     *
     * @param guiContainer The guiContainer to disable RMB drag in.
     */
    public static void disableVanillaRMBDrag(GuiContainer guiContainer) {
        guiContainer.field_146995_H = true;
        guiContainer.field_147007_t = false;
    }

    public static int getRequiredMouseX() {
        ScaledResolution var8 = new ScaledResolution(mc, getDisplayWidth(), getDisplayHeight());
        int var9 = var8.getScaledWidth();
        return (Mouse.getX() * var9) / getDisplayWidth();
    }

    public static int getRequiredMouseY() {
        ScaledResolution var8 = new ScaledResolution(mc, getDisplayWidth(), getDisplayHeight());
        int var10 = var8.getScaledHeight();
        return var10 - ((Mouse.getY() * var10) / getDisplayHeight()) - 1;
    }
}
