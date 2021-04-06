package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.block.EightWayDirection;
import com.fuzs.biomerivers.block.EightWayFenceBlock;
import com.fuzs.biomerivers.block.IEightWayBlock;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin extends FourWayBlock implements IEightWayBlock {

    private Object2IntMap<BlockState> statePaletteMap2 = new Object2IntOpenHashMap<>();

    public FenceBlockMixin(float nodeWidth, float extensionWidth, float nodeHeight, float extensionHeight, float collisionY, Properties properties) {

        super(nodeWidth, extensionWidth, nodeHeight, extensionHeight, collisionY, properties);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(AbstractBlock.Properties properties, CallbackInfo callbackInfo) {

        this.setDefaultState(this.getDefaultState().with(NORTH_EAST, Boolean.FALSE).with(SOUTH_EAST, Boolean.FALSE).with(SOUTH_WEST, Boolean.FALSE).with(NORTH_WEST, Boolean.FALSE));
    }

    @Inject(method = "fillStateContainer", at = @At("TAIL"))
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder, CallbackInfo callbackInfo) {

        this.fillEightWayStateContainer(builder);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected int getIndex(BlockState state) {

        if (this.statePaletteMap2 == null) {

            this.statePaletteMap2  = new Object2IntOpenHashMap<>();
        }

        return EightWayFenceBlock.getIndex(this.statePaletteMap2, state);
    }

    private int getStateIndex(BlockState stateIn) {

        int index = 0;
        if (stateIn.get(NORTH)) {
            
            index |= EightWayDirection.NORTH.getHorizontalIndex();
        }

        if (stateIn.get(EAST)) {
            
            index |= EightWayDirection.EAST.getHorizontalIndex();
        }

        if (stateIn.get(SOUTH)) {
            
            index |= EightWayDirection.SOUTH.getHorizontalIndex();
        }

        if (stateIn.get(WEST)) {
            
            index |= EightWayDirection.WEST.getHorizontalIndex();
        }
        
        if (stateIn.get(NORTH_EAST)) {

            index |= EightWayDirection.NORTH_EAST.getHorizontalIndex();
        }

        if (stateIn.get(SOUTH_EAST)) {

            index |= EightWayDirection.SOUTH_EAST.getHorizontalIndex();
        }

        if (stateIn.get(SOUTH_WEST)) {

            index |= EightWayDirection.SOUTH_WEST.getHorizontalIndex();
        }

        if (stateIn.get(NORTH_WEST)) {

            index |= EightWayDirection.NORTH_WEST.getHorizontalIndex();
        }

        return index;
    }

    public boolean canConnect(BlockState state) {

        return this.isWoodenFence(state.getBlock());
    }

    @Shadow
    private boolean isWoodenFence(Block block) {

        throw new IllegalStateException();
    }

    @Inject(method = "getStateForPlacement", at = @At("TAIL"), cancellable = true)
    public void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> callbackInfo) {

        BlockState state = callbackInfo.getReturnValue();
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockPos northEastPos = blockpos.north().east();
        BlockPos southEastPos = blockpos.south().east();
        BlockPos southWestPos = blockpos.south().west();
        BlockPos northWestPos = blockpos.north().west();
        BlockState northEastState = iblockreader.getBlockState(northEastPos);
        BlockState southEastState = iblockreader.getBlockState(southEastPos);
        BlockState southWestState = iblockreader.getBlockState(southWestPos);
        BlockState northWestState = iblockreader.getBlockState(northWestPos);

        callbackInfo.setReturnValue(state.with(NORTH_EAST, this.canConnect(northEastState)).with(SOUTH_EAST, this.canConnect(southEastState)).with(SOUTH_WEST, this.canConnect(southWestState)).with(NORTH_WEST, this.canConnect(northWestState)));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
        
        float nodeStart = 8.0F - nodeWidth;
        float nodeEnd = 8.0F + nodeWidth;
        float extensionStart = 8.0F - extensionWidth;
        float extensionEnd = 8.0F + extensionWidth;
        VoxelShape nodeShape = Block.makeCuboidShape(nodeStart, 0.0, nodeStart, nodeEnd, nodeHeight, nodeEnd);
        VoxelShape northShape = Block.makeCuboidShape(extensionStart, extensionBottom, 0.0, extensionEnd, extensionHeight, extensionStart);
        VoxelShape eastShape = Block.makeCuboidShape(16.0 - extensionEnd, extensionBottom, extensionStart, 16.0, extensionHeight, extensionEnd);
        VoxelShape southShape = Block.makeCuboidShape(extensionStart, extensionBottom, 16.0 - extensionEnd, extensionEnd, extensionHeight, 16.0);
        VoxelShape westShape = Block.makeCuboidShape(0.0, extensionBottom, extensionStart, extensionStart, extensionHeight, extensionEnd);
        VoxelShape northEastShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, false, true);
        VoxelShape southEastShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, false, false);
        VoxelShape southWestShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, true, false);
        VoxelShape northWestShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, true, true);

        VoxelShape[] directionalShapes = new VoxelShape[]{southShape, westShape, northShape, eastShape, southWestShape, northWestShape, northEastShape, southEastShape};
        VoxelShape[] stateShapes = new VoxelShape[(int) Math.pow(2, directionalShapes.length)];
        for (int i = 0; i < stateShapes.length; i++) {

            stateShapes[i] = nodeShape;
            for (int j = 0; j < directionalShapes.length; j++) {

                if (((i >> j) & 1) == 1) {

                    stateShapes[i] = VoxelShapes.or(stateShapes[i], directionalShapes[j]);
                }
            }
        }

        return stateShapes;
    }

    private VoxelShape getDiagonalShape(float extensionWidth, float extensionBottom, float extensionHeight, boolean directionX, boolean directionZ) {

        VoxelShape diagonalShape = VoxelShapes.empty();
        int steps = 64;
        for (int i = 0; i < steps; i++) {

            double step = i * 8.0 / steps;
            double posX = directionX ? 16.0 - step : step;
            double posZ = directionZ ? 16.0 - step : step;
            VoxelShape cubeShape = Block.makeCuboidShape(posX - extensionWidth, extensionBottom, posZ - extensionWidth, posX + extensionWidth, extensionHeight, posZ + extensionWidth);
            diagonalShape = VoxelShapes.or(diagonalShape, cubeShape);
        }

        return diagonalShape;
    }

}
