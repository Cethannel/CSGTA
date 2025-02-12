package com.ethannel.csgta;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.lwjgl.input.Keyboard;

import com.ethannel.csgta.utils.Vector3i;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class ClientProxy extends CommonProxy {

    public static KeyBinding myKeyBinding;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Create the key binding with corrected category name
        myKeyBinding = new KeyBinding("key.pickBlock.desc", Keyboard.KEY_G, "key.csgta.category");

        // Register the keybinding
        ClientRegistry.registerKeyBinding(myKeyBinding);
        CSGTA.LOG.info("Registered keybind: " + myKeyBinding.getKeyDescription());

        // Register event handler for key input
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        CSGTA.LOG.info("Registered ClientProxy for key events.");
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // Use consumeClick to ensure the action only occurs once per key press
        if (myKeyBinding.isPressed()) {
            var player = Minecraft.getMinecraft().thePlayer;
            var pos = getLookingAtLocation(player);
            var world = Minecraft.getMinecraft().theWorld;
            var block = world.getBlock(pos.x, pos.y, pos.z);
            CSGTA.LOG.info("Looking at: " + block);
            var blockId = Block.getIdFromBlock(block);
            var blockData = world.getBlockMetadata(pos.x, pos.y, pos.z);
            String blockIDString = blockId + ":" + blockData;
            CSGTA.LOG.info("Looking at: " + blockData);
            StringSelection stringSelection = new StringSelection(blockIDString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            player.addChatMessage(
                    new ChatComponentText("Copied: " + blockIDString + " to clipboard"));
        }
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

}
