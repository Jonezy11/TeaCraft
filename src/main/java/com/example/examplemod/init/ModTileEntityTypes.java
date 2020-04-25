package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.tileentity.MicroscopeTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModTileEntityTypes {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ExampleMod.MOD_ID);

    public static final RegistryObject<TileEntityType<MicroscopeTileEntity>> MICROSCOPE = TILE_ENTITY_TYPES.register("microscope", () -> TileEntityType.Builder.create(MicroscopeTileEntity::new, ModBlocks.MICROSCOPE.get()).build(null));

}
