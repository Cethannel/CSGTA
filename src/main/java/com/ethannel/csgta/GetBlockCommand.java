package com.ethannel.csgta;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChunkCoordinates;

public class GetBlockCommand implements ICommand {

    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public String getCommandName() {
        return "getBlock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Gets the command";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> out = new ArrayList<String>();

        return out;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ChunkCoordinates playerpos = sender.getPlayerCoordinates();
        var block = sender.getEntityWorld()
            .getBlock(playerpos.posX, playerpos.posY - 1, playerpos.posZ);
        var blockData = sender.getEntityWorld()
            .getBlockMetadata(playerpos.posX, playerpos.posY - 1, playerpos.posZ);
        var id = Block.getIdFromBlock(block);
        CSGTA.LOG.info("Id is: " + id + ":" + blockData);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTabCompletionOptions'");
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUsernameIndex'");
    }
}
