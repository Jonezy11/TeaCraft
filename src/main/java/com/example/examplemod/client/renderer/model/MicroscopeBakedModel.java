package com.example.examplemod.client.renderer.model;

import com.example.examplemod.block.MicroscopeBlock;
import com.example.examplemod.tileentity.MicroscopeTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MicroscopeBakedModel implements IBakedModel {

    private IBakedModel baseModel;

    public ModelProperty<ItemStack> RENDER_ITEM = new ModelProperty<>();

    public ModelDataMap getEmptyIModelData() {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        builder.withInitial(RENDER_ITEM, null);
        return builder.build();
    }

    public MicroscopeBakedModel(IBakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        IBakedModel base = this.baseModel;
        if (!extraData.hasProperty(RENDER_ITEM) || state == null) {
            return base.getQuads(state, side, rand, extraData);
        }
        ItemStack item = extraData.getData(RENDER_ITEM);
        if (item == null) {
            return base.getQuads(state, side, rand, extraData);
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(item, null, null);
        Matrix4f matrix = Matrix4f.makeScale(0.36f, 0.36f, 0.36f);
        matrix.mul(new Quaternion(-90.0f, 0.0f, 0.0f, true));
        matrix.mul(Matrix4f.makeTranslate(0.89f, -2.23f, 0.58f));
        switch (state.get(MicroscopeBlock.FACING)) {
            case NORTH:
                matrix.mul(new Quaternion(0.0f, 0.0f, 180.0f, true));
                break;
            case EAST:
                matrix.mul(new Quaternion(0.0f, 0.0f, 90.0f, true));
                break;
            case SOUTH:
                break;
            case WEST:
                matrix.mul(new Quaternion(0.0f, 0.0f, -90.0f, true));
                break;
        }
        TransformationMatrix tm = new TransformationMatrix(matrix);
        QuadTransformer transformer = new QuadTransformer(tm);
        List<BakedQuad> updatedItemQuads = transformer.processMany(itemModel.getQuads(state, side, rand, extraData));
        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(base.getQuads(state, side, rand, extraData));
        quads.addAll(updatedItemQuads);
        return quads;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        if (state == world.getBlockState(pos)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof MicroscopeTileEntity) {
                if (((MicroscopeTileEntity) te).getItemStack().getItem() != Blocks.AIR.asItem()) {
                    ItemStack item = ((MicroscopeTileEntity) te).getItemStack().copy();
                    ModelDataMap map = getEmptyIModelData();
                    map.setData(RENDER_ITEM, item);
                    return map;
                }
            }
        }
        return getEmptyIModelData();
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return baseModel.getParticleTexture();
    }

    // IBakedModel Methods
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean func_230044_c_() {
        return baseModel.func_230044_c_();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseModel.getOverrides();
    }
}
