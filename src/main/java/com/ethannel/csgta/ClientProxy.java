package com.ethannel.csgta;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.ethannel.csgta.utils.Vector3i;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    public static KeyBinding myKeyBinding;
    protected static Minecraft mc;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Create the key binding with corrected category name
        myKeyBinding = new KeyBinding("key.pickBlock.desc", Keyboard.KEY_G, "key.csgta.category");
        mc = Minecraft.getMinecraft();

        // Register the keybinding
        ClientRegistry.registerKeyBinding(myKeyBinding);
        CSGTA.LOG.info("Registered keybind: " + myKeyBinding.getKeyDescription());

        // Register event handler for key input
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
                .bus()
                .register(this);
        CSGTA.LOG.info("Registered ClientProxy for key events.");
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // Use consumeClick to ensure the action only occurs once per key press
        if (myKeyBinding.isPressed()) {
            var player = Minecraft.getMinecraft().thePlayer;
            player.addChatMessage(new ChatComponentText("Getting block"));
            var pos = getLookingAtLocation(player);
            var world = Minecraft.getMinecraft().theWorld;
            var block = world.getBlock(pos.x, pos.y, pos.z);
            CSGTA.LOG.info("Looking at: " + block);
            var blockData = world.getBlockMetadata(pos.x, pos.y, pos.z);
            copyIdFromBlock(block, blockData);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Config.enableInventory) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();

        DeobfuscationLayer.mc = mc;

        // Ensure we're in a GUI screen and a key has been pressed
        if (mc.currentScreen instanceof GuiContainer guiContainer) {
            if (Keyboard.isKeyDown(myKeyBinding.getKeyCode())) {
                int slotCount = getSlotCountWithID(mc.currentScreen, guiContainer.inventorySlots);
                Slot selectedSlot = getSelectedSlotWithID(mc.currentScreen, slotCount, guiContainer.inventorySlots);
                var stack = selectedSlot.getStack();
                var item = stack.getItem();
                var block = Block.getBlockFromItem(item);
                if (block instanceof BlockAir) {
                    return;
                }
                var blockData = stack.getItemDamage();
                copyIdFromBlock(block, blockData);
            }
        }
    }

    public void copyIdFromBlock(Block block, int blockData) {
        var player = Minecraft.getMinecraft().thePlayer;
        var blockId = Block.getIdFromBlock(block);
        String blockIDString = blockId + ":" + blockData;
        CSGTA.LOG.info("Looking at: " + blockData);
        copyToClipboard(blockIDString, player);
    }

    public static int getSlotCountWithID(GuiScreen currentScreen, Object container) {
        return DeobfuscationLayer.getSlots(DeobfuscationLayer.asContainer(container))
                .size();
    }

    public static Slot getSelectedSlotWithID(GuiScreen currentScreen, int slotCount, Object container) {
        return DeobfuscationLayer.getSelectedSlot(
                DeobfuscationLayer.asGuiContainer(currentScreen),
                DeobfuscationLayer.asContainer(container),
                slotCount);
    }

    /**
     * @param player
     * @return
     */
    public static Vector3i getLookingAtLocation(EntityPlayer player) {
        double reachDistance = player instanceof EntityPlayerMP mp ? mp.theItemInWorldManager.getBlockReachDistance()
                : Minecraft.getMinecraft().playerController.getBlockReachDistance();

        Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        Vec3 lookVec = player.getLook(1);

        Vec3 modifiedPosVec = posVec
                .addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance,
                        lookVec.zCoord * reachDistance);

        MovingObjectPosition hit = player.worldObj.rayTraceBlocks(posVec, modifiedPosVec);

        Vector3i target;

        if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
            target = new Vector3i(hit.blockX, hit.blockY, hit.blockZ);
        } else {
            target = new Vector3i(
                    MathHelper.floor_double(modifiedPosVec.xCoord),
                    MathHelper.floor_double(modifiedPosVec.yCoord),
                    MathHelper.floor_double(modifiedPosVec.zCoord));
        }

        return target;
    }

    private void copyToClipboard(String text, EntityPlayer player) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        player.addChatMessage(new ChatComponentText("Copied: " + text + " to clipboard"));
    }
}
