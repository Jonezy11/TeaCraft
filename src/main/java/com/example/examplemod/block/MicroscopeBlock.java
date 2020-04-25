package com.example.examplemod.block;

import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModTileEntityTypes;
import com.example.examplemod.tileentity.MicroscopeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.world.World;


public class MicroscopeBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    protected static final VoxelShape NS_AABB;
    protected static final VoxelShape EW_AABB;

    public MicroscopeBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof MicroscopeTileEntity) {
                if (((MicroscopeTileEntity) te).getItemStack().getItem() != Blocks.AIR.asItem()) {
                    if (player.getHeldItem(handIn).isEmpty()) {
                        player.setHeldItem(handIn, ((MicroscopeTileEntity) te).returnToPlayer());
                        worldIn.notifyBlockUpdate(pos, state, state, 2);
                        LOGGER.info("Called BlockUpdater");
                    }
                    else if (player.getHeldItem(handIn).getItem() == ((MicroscopeTileEntity) te).getItemStack().getItem() &&
                             player.getHeldItem(handIn).getCount() < player.getHeldItem(handIn).getMaxStackSize()) {
                        ((MicroscopeTileEntity) te).resetItemStack();
                        player.getHeldItem(handIn).grow(1);
                        worldIn.notifyBlockUpdate(pos, state, state, 2);
                        LOGGER.info("Called BlockUpdater");
                    }
                }
                else if (((MicroscopeTileEntity) te).queryAcceptedItems(player.getHeldItem(handIn).getItem())) {
                    ((MicroscopeTileEntity) te).addItem(player.getHeldItem(handIn));
                    player.getHeldItem(handIn).shrink(1);
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                    LOGGER.info("Called BlockUpdater");
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof MicroscopeTileEntity) {
                InventoryHelper.dropItems(worldIn, pos, ((MicroscopeTileEntity) te).droppedItems());
            }
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
            case SOUTH:
            default:
                return NS_AABB;
            case EAST:
            case WEST:
                return EW_AABB;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return ModTileEntityTypes.MICROSCOPE.get().create();
    }

    static {
        NS_AABB = Block.makeCuboidShape(3.0, 0.0, 2.0, 13.0, 16.0, 14.0);
        EW_AABB = Block.makeCuboidShape(2.0, 0.0, 3.0, 14.0, 16.0, 13.0);
    }
}
