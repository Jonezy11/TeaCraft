package com.example.examplemod.util;

import com.example.examplemod.ExampleMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {}
}
