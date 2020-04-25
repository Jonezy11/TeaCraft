package com.example.examplemod.util;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.MicroscopeBlock;
import com.example.examplemod.client.renderer.model.MicroscopeBakedModel;
import com.example.examplemod.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.MICROSCOPE.get(), RenderType.getCutoutMipped());
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        for (BlockState blockState : ModBlocks.MICROSCOPE.get().getStateContainer().getValidStates()) {
            ModelResourceLocation modelRL = BlockModelShapes.getModelLocation(blockState);
            IBakedModel existingModel = event.getModelRegistry().get(modelRL);
            if (existingModel == null) {
                LOGGER.warn("Did not find Model in registry");
            } else if (existingModel instanceof MicroscopeBakedModel) {
                LOGGER.warn("Tried to replace Model twice");
            } else {
                MicroscopeBakedModel customModel = new MicroscopeBakedModel(existingModel);
                event.getModelRegistry().put(modelRL, customModel);
            }
        }
    }
}
