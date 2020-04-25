package com.example.examplemod.tileentity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModTileEntityTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MicroscopeTileEntity extends TileEntity {

    private ItemStack SLIDE_ITEM = new ItemStack(Blocks.AIR.asItem(), 0);

    private static List<Item> ACCEPTED_ITEMS = new ArrayList<Item>();

    public MicroscopeTileEntity() {
        super(ModTileEntityTypes.MICROSCOPE.get());
        ACCEPTED_ITEMS.add(Items.PORKCHOP);
        ACCEPTED_ITEMS.add(Items.BEETROOT);
        ACCEPTED_ITEMS.add(Items.GLASS_PANE);
    }

    public boolean queryAcceptedItems(Item item) {
        return ACCEPTED_ITEMS.contains(item);
    }

    // Called before TileEntity removal
    public NonNullList<ItemStack> droppedItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.add(SLIDE_ITEM);
        return items;
    }

    public ItemStack returnToPlayer() {
        ItemStack returned = this.SLIDE_ITEM;
        resetItemStack();
        return returned;
    }

    // Never modify
    public ItemStack getItemStack() {
        return this.SLIDE_ITEM;
    }

    public void resetItemStack() {
        this.SLIDE_ITEM = new ItemStack(Blocks.AIR.asItem(), 0);
        this.markDirty();
    }

    public void addItem(ItemStack itemStackIn) {
            ItemStack copied_stack = itemStackIn.copy();
            copied_stack.setCount(1);
            this.SLIDE_ITEM = copied_stack;
            this.markDirty();
    }

    @Override
    public void read(CompoundNBT compound) {
        LogManager.getLogger().info("TileEntity data loading");
        super.read(compound);
        this.SLIDE_ITEM = ItemStack.read(compound.getCompound("Slide"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        LogManager.getLogger().info("TileEntity data saved");
        CompoundNBT written_compound = this.createTag(compound);
        return super.write(written_compound);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        LogManager.getLogger().info("Updating the Client packet");
        return new SUpdateTileEntityPacket(getPos(),-1, this.createTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        LogManager.getLogger().info("Reading the packet ClientSide");
        CompoundNBT compound = pkt.getNbtCompound();
        this.SLIDE_ITEM = ItemStack.read(compound.getCompound("Slide"));
        ClientWorld clientWorld = Minecraft.getInstance().world;
        if (clientWorld != null) {
            clientWorld.markBlockRangeForRenderUpdate(getPos(), Minecraft.getInstance().world.getBlockState(getPos()), null);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        LogManager.getLogger().info("Chunk tag calledfor");
        return super.write(this.createTag());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        LogManager.getLogger().info("Chunk tag handled on client");
        this.SLIDE_ITEM = ItemStack.read(tag.getCompound("Slide"));
    }

    public CompoundNBT createTag () {
        CompoundNBT compound = new CompoundNBT();
        CompoundNBT tag = new CompoundNBT();
        compound.put("Slide", this.SLIDE_ITEM.write(tag));
        return compound;
    }

    public CompoundNBT createTag (CompoundNBT compound) {
        CompoundNBT tag = new CompoundNBT();
        compound.put("Slide", this.SLIDE_ITEM.write(tag));
        return compound;
    }

}
