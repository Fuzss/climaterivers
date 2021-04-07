package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.block.EightWayDirection;
import com.fuzs.biomerivers.block.IEightWayBlock;
import com.fuzs.biomerivers.util.shape.NoneVoxelShape;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin extends FourWayBlock implements IEightWayBlock {

    private Object2IntMap<BlockState> statePaletteMap = new Object2IntOpenHashMap<>();

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

        if (this.statePaletteMap == null) {

            this.statePaletteMap = new Object2IntOpenHashMap<>();
        }

        return statePaletteMap.computeIntIfAbsent(state, this::getEightWayStateIndex);
    }

    @Shadow
    public abstract boolean canConnect(BlockState state, boolean isSideSolid, Direction direction);

    public boolean canConnect(BlockState state) {

        return this.isWoodenFence(state.getBlock());
    }

    @Shadow
    private boolean isWoodenFence(Block block) {

        throw new IllegalStateException();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
    public void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> callbackInfo) {

        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        BlockPos[] positions = Stream.of(EightWayDirection.values()).map(EightWayDirection::getDirectionVec).map(blockpos::add).toArray(BlockPos[]::new);
        BlockState[] states = Stream.of(positions).map(iblockreader::getBlockState).toArray(BlockState[]::new);
        int connections = 0;
        for (int i = 0; i < 4; i++) {

            Direction direction = Direction.byHorizontalIndex(i);
            if (this.canConnect(states[i], states[i].isSolidSide(iblockreader, positions[i], direction), direction)) {

                connections |= 1 << i;
            }
        }

        BlockState stateForPlacement = super.getStateForPlacement(context)
                .with(SOUTH, (connections & 1) != 0)
                .with(WEST, (connections & 2) != 0)
                .with(NORTH, (connections & 4) != 0)
                .with(EAST, (connections & 8) != 0)
                .with(SOUTH_WEST, this.canConnect(states[4]) && (connections & 1) == 0 && (connections & 2) == 0)
                .with(NORTH_WEST, this.canConnect(states[5]) && (connections & 2) == 0 && (connections & 4) == 0)
                .with(NORTH_EAST, this.canConnect(states[6]) && (connections & 4) == 0 && (connections & 8) == 0)
                .with(SOUTH_EAST, this.canConnect(states[7]) && (connections & 8) == 0 && (connections & 1) == 0)
                .with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);

        callbackInfo.setReturnValue(stateForPlacement);
    }



    @SuppressWarnings("NullableProblems")
    @Override
    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
        
        float nodeStart = 8.0F - nodeWidth;
        float nodeEnd = 8.0F + nodeWidth;
        float extensionStart = 8.0F - extensionWidth;
        float extensionEnd = 8.0F + extensionWidth;
        VoxelShape nodeShape = Block.makeCuboidShape(nodeStart, 0.0, nodeStart, nodeEnd, nodeHeight, nodeEnd);
        VoxelShape southShape = Block.makeCuboidShape(extensionStart, extensionBottom, 0.0, extensionEnd, extensionHeight, extensionStart);
        VoxelShape westShape = Block.makeCuboidShape(16.0 - extensionEnd, extensionBottom, extensionStart, 16.0, extensionHeight, extensionEnd);
        VoxelShape northShape = Block.makeCuboidShape(extensionStart, extensionBottom, 16.0 - extensionEnd, extensionEnd, extensionHeight, 16.0);
        VoxelShape eastShape = Block.makeCuboidShape(0.0, extensionBottom, extensionStart, extensionStart, extensionHeight, extensionEnd);
        VoxelShape southWestShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, EightWayDirection.SOUTH_WEST);
        VoxelShape northWestShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, EightWayDirection.NORTH_WEST);
        VoxelShape northEastShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, EightWayDirection.NORTH_EAST);
        VoxelShape southEastShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, EightWayDirection.SOUTH_EAST);

        VoxelShape[] directionalShapes = new VoxelShape[]{northShape, eastShape, southShape, westShape, northEastShape, southEastShape, southWestShape, northWestShape};
        VoxelShape[] stateShapes = new VoxelShape[(int) Math.pow(2, directionalShapes.length)];
        for (int i = 0; i < stateShapes.length; i++) {

            stateShapes[i] = nodeShape;
            for (int j = 0; j < directionalShapes.length; j++) {

                if ((i & (1 << j)) != 0) {

                    stateShapes[i] = VoxelShapes.or(stateShapes[i], directionalShapes[j]);
                }
            }
        }

        return stateShapes;
    }

    private VoxelShape getDiagonalShape(float extensionWidth, float extensionBottom, float extensionHeight, EightWayDirection direction) {

        VoxelShape diagonalShape = VoxelShapes.empty();
        for (int i = 0; i < 8; i++) {

            Vector3i directionVec = direction.getDirectionVec();
            int posX = directionVec.getX() > 0 ? i : 16 - i;
            int posZ = directionVec.getZ() > 0 ? i : 16 - i;
            VoxelShape cubeShape = Block.makeCuboidShape(posX - extensionWidth, extensionBottom, posZ - extensionWidth, posX + extensionWidth, extensionHeight, posZ + extensionWidth);
            diagonalShape = VoxelShapes.or(diagonalShape, cubeShape);
        }

        return diagonalShape;
    }

    private VoxelShape getDiagonalShapeB(float extensionWidth, float extensionBottom, float extensionHeight, EightWayDirection direction) {

        VoxelShape diagonalShape = this.getDiagonalShape(extensionWidth, extensionBottom, extensionHeight, direction);

        final float diagonalSide = 0.7071067812F * extensionWidth;
        Vector3d bottom1 = new Vector3d(-diagonalSide, extensionBottom, diagonalSide);
        Vector3d bottom2 = new Vector3d(diagonalSide, extensionBottom, -diagonalSide);
        Vector3d bottom3 = bottom1.add(8.0, 0.0, 8.0);
        Vector3d bottom4 = bottom2.add(8.0, 0.0, 8.0);
        Vector3d top1 = new Vector3d(bottom1.x, extensionHeight, bottom1.z);
        Vector3d top2 = new Vector3d(bottom2.x, extensionHeight, bottom2.z);
        Vector3d top3 = new Vector3d(bottom3.x, extensionHeight, bottom3.z);
        Vector3d top4 = new Vector3d(bottom4.x, extensionHeight, bottom4.z);

        List<Vector3d> boundingEdges = Arrays.asList(

                // Bottom
                bottom1, bottom2,
                bottom2, bottom3,
                bottom3, bottom4,
                bottom4, bottom1,

                // Sides
                bottom1, top1,
                bottom2, top2,
                bottom3, top3,
                bottom4, top4,

                // Top
                top1, top2,
                top2, top3,
                top3, top4,
                top4, top1
        );

        if (direction.getDirectionVec().getX() != 1) {

            boundingEdges = boundingEdges.stream().map(vec3d -> new Vector3d(1.0 - vec3d.x, vec3d.y, vec3d.z)).collect(Collectors.toList());
        }

        if (direction.getDirectionVec().getZ() != 1) {

            boundingEdges = boundingEdges.stream().map(vec3d -> new Vector3d(vec3d.x, vec3d.y, 1.0 - vec3d.z)).collect(Collectors.toList());
        }

        return null;
//        return new NoneVoxelShape(diagonalShape, boundingEdges);
    }

    @SuppressWarnings({"NullableProblems", "deprecation"})
    @Override
    public void updateDiagonalNeighbors(BlockState state, IWorld world, BlockPos pos, int flags, int recursionLeft) {

        BlockPos.Mutable neighborPos = new BlockPos.Mutable();
        for (EightWayDirection direction : EightWayDirection.getIntercardinalDirections()) {

            Vector3i directionVec = direction.getDirectionVec();
            neighborPos.setAndOffset(pos, directionVec.getX(), directionVec.getY(), directionVec.getZ());
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof FenceBlock) {

                BlockState neighborPostUpdateState = neighborState.with(DIRECTION_TO_PROPERTY_MAP.get(direction.getOpposite()), this.canConnect(neighborState));
                Block.replaceBlockState(neighborState, neighborPostUpdateState, world, neighborPos, flags, recursionLeft);
            }
        }
    }

}
