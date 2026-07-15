package io.github.jadedchara.common.block;

import com.mojang.serialization.MapCodec;
import io.github.jadedchara.common.block.entity.VerticalBoosterBlockEntity;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class VerticalBoosterBlock extends BaseEntityBlock {
    public static final DirectionProperty DIRECTION = DirectionProperty.create("direction");
    public VerticalBoosterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.UP));

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(VerticalBoosterBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p, BlockState b) {
        return new VerticalBoosterBlockEntity(p,b);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, SkybornBlocks.VERTICAL_BOOSTER_BLOCKENTITY, VerticalBoosterBlockEntity::tick);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    //


    @Override
    public InteractionResult useWithoutItem(BlockState state, Level l, BlockPos pos, Player p,
                                   BlockHitResult hit) {
        if (!l.isClientSide()) {
            MenuProvider screenHandlerFactory = state.getMenuProvider(l, pos);
            if (screenHandlerFactory != null) {
                p.openMenu(screenHandlerFactory);
            }
        }
        //System.out.print("Interacted!");
        return InteractionResult.SUCCESS_NO_ITEM_USED;

    }

    @Override
    protected void onRemove(BlockState bs, Level l, BlockPos bp, BlockState bs2, boolean bl) {
        if (bs.getBlock() != bs2.getBlock()) {
            BlockEntity be = l.getBlockEntity(bp);
            if (be instanceof VerticalBoosterBlockEntity vbe) {

                Containers.dropContents(l, bp, (Container) vbe);
                l.updateNeighborsAt(bp, this);
            }
            super.onRemove(bs, l, bp, bs2, bl);
        }
    }
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }


}
