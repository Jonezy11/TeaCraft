package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.KettleHangerBlock;
import com.example.examplemod.block.MicroscopeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, ExampleMod.MOD_ID);

    public static final RegistryObject<Block> MICROSCOPE = BLOCKS.register("microscope",
            () -> new MicroscopeBlock(Block.Properties.create(Material.IRON).lightValue(11)));

    public static final RegistryObject<Block> KETTLE_HANGER = BLOCKS.register("kettle_hanger",
            () -> new KettleHangerBlock(Block.Properties.create(Material.WOOD)));
}
