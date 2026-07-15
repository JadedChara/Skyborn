package io.github.jadedchara.common.block;

import com.mojang.serialization.MapCodec;
import io.github.jadedchara.common.block.entity.AltitudeControlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AltitudeControlBlock extends BaseEntityBlock {
    public AltitudeControlBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AltitudeControlBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p, BlockState b) {
        return new AltitudeControlBlockEntity(p,b);
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override


    //==========
    public InteractionResult useWithoutItem(
            BlockState state,
            Level l,
            BlockPos pos,
            Player p,
            BlockHitResult hit
    ) {
        if (!l.isClientSide()) {
            MenuProvider screenHandlerFactory = state.getMenuProvider(l, pos);
            if (screenHandlerFactory != null) {
                p.openMenu(screenHandlerFactory);
            }
        }
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }
}
