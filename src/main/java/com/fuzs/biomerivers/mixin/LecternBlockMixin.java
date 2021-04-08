package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.util.shape.NoneVoxelShape;
import com.fuzs.biomerivers.util.shape.VoxelUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LecternBlock.class)
public abstract class LecternBlockMixin extends ContainerBlock {

    private static final Vector3d[] TOP_SHAPE_CORNERS = VoxelUtils.scaleDown(VoxelUtils.createVectorArray(0.319134879, 13.4776191, 0.01, 12.3295688, 18.45250372, 0.01, 1.849868608, 9.782100968, 0.01, 13.86030253, 14.75698559, 0.01, 0.319134879, 13.4776191, 15.99, 12.3295688, 18.45250372, 15.99, 1.849868608, 9.782100968, 15.99, 13.86030253, 14.75698559, 15.99));
    private static final VoxelShape SKEW_WEST_SHAPE = new NoneVoxelShape(LecternBlock.WEST_SHAPE, LecternBlock.COMMON_SHAPE, VoxelUtils.create12Edges(TOP_SHAPE_CORNERS));
    private static final VoxelShape SKEW_NORTH_SHAPE = new NoneVoxelShape(LecternBlock.NORTH_SHAPE, LecternBlock.COMMON_SHAPE, VoxelUtils.create12Edges(VoxelUtils.rotate(TOP_SHAPE_CORNERS)));
    private static final VoxelShape SKEW_EAST_SHAPE = new NoneVoxelShape(LecternBlock.EAST_SHAPE, LecternBlock.COMMON_SHAPE, VoxelUtils.create12Edges(VoxelUtils.flip(TOP_SHAPE_CORNERS)));
    private static final VoxelShape SKEW_SOUTH_SHAPE = new NoneVoxelShape(LecternBlock.SOUTH_SHAPE, LecternBlock.COMMON_SHAPE, VoxelUtils.create12Edges(VoxelUtils.flip(VoxelUtils.rotate(TOP_SHAPE_CORNERS))));

    protected LecternBlockMixin(Properties builder) {

        super(builder);
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> callbackInfo) {

        callbackInfo.setReturnValue(getShapeFromDirection(state.get(LecternBlock.FACING)));
    }

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> callbackInfo) {

        callbackInfo.setReturnValue(getShapeFromDirection(state.get(LecternBlock.FACING)));
    }

    private static VoxelShape getShapeFromDirection(Direction direction) {

        switch (direction) {

            case NORTH:

                return SKEW_NORTH_SHAPE;
            case SOUTH:

                return SKEW_SOUTH_SHAPE;
            case EAST:

                return SKEW_EAST_SHAPE;
            case WEST:

                return SKEW_WEST_SHAPE;
            default:

                return LecternBlock.COMMON_SHAPE;
        }
    }

}
