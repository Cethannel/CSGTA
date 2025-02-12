package com.ethannel.csgta;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * MatterScanner
 */
public class ItemMatterScanner extends Item {
    public ItemMatterScanner() {
        String name = "itemMatterScanner";

        this.setCreativeTab(CreativeTabs.tabTools);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(name);

        GameRegistry.registerItem(this, name);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
                .bus()
                .register(this);
    }
}
