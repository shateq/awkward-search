package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    //This logger is used to write text to the console and the log file.
    //It is considered best practice to use your mod id as the logger's name.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    @Override
    public void onInitialize() {
        //Some things (like resources) may still be uninitialized.
        LOGGER.info("Hello Fabric world!");
    }
}
