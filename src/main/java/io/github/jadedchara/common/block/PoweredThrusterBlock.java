package io.github.jadedchara.common.block;

import com.mojang.serialization.MapCodec;
import io.github.jadedchara.common.block.entity.PoweredThrusterBlockEntity;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class PoweredThrusterBlock extends BaseEntityBlock {
    public static final DirectionProperty DIRECTION = DirectionProperty.create("direction");

    public PoweredThrusterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PoweredThrusterBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p, BlockState b) {
        return new PoweredThrusterBlockEntity(p,b);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, SkybornBlocks.POWERED_THRUSTER_BLOCKENTITY, PoweredThrusterBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext c) {
        return super.getStateForPlacement(c).setValue(DIRECTION,c.getHorizontalDirection());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
