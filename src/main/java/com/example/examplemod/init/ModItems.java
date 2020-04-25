package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ExampleMod.MOD_ID);

    public static final RegistryObject<Item> MICROSCOPE = ITEMS.register("microscope",
            () -> new BlockItem(ModBlocks.MICROSCOPE.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    public static final RegistryObject<Item> KETTLE_HANGER = ITEMS.register("kettle_hanger",
            () -> new BlockItem(ModBlocks.KETTLE_HANGER.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
}
