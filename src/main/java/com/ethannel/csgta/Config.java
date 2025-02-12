package com.ethannel.csgta;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Boolean enableInventory = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        enableInventory = configuration.getBoolean("enableInventory", Configuration.CATEGORY_GENERAL, enableInventory,
                "Enable picking in inventories [WARNING: may crash game if non-vanialla inventory]");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
