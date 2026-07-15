package io.github.jadedchara.common.block.entity;

import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import io.github.jadedchara.common.block.PoweredThrusterBlock;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class PoweredThrusterBlockEntity extends BlockEntity implements BlockEntityPropeller, BlockEntitySubLevelPropellerActor {
    public PoweredThrusterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkybornBlocks.POWERED_THRUSTER_BLOCKENTITY, blockPos, blockState);
    }

    public float thrust = 0;
    //public float maxThrust = 100F;
    private float airflow = 0;


    //presets
    @Override
    public Direction getBlockDirection() {
        return getBlockState().getValue(PoweredThrusterBlock.DIRECTION);
    }

    @Override
    public double getAirflow() {
        return isActive()?getThrust()*0.8F:this.airflow;
    }

    @Override
    public double getThrust() {
        return isActive()?this.thrust:0;
    }

    @Override
    public boolean isActive() {
        return getRedstonePower();
    }

    @Override
    public double getCurrentAirPressure() {
        double y = worldPosition.getY();
        double max = 700;

        double p = 1.0 - (y / max);
        return Math.max(p, 0.0);
    }

    @Override
    public double getAirflowScaling() {
        return 1.0;
    }

    public static void tick(Level world, BlockPos blockPos, BlockState blockState, PoweredThrusterBlockEntity entity){
        if(entity.isActive() && entity.thrust<100F){
            entity.thrust += 5;
        }else if(!entity.isActive() && entity.thrust>0F){
            entity.thrust -= 5;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        nbt.putFloat("Thrust", thrust);
        nbt.putFloat("Airflow", airflow);

        super.saveAdditional(nbt, registryLookup);
    }
    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        thrust = nbt.getFloat("Thrust");
        airflow = nbt.getFloat("Airflow");
    }

    private boolean getRedstonePower() {
        return level != null ? level.getBestNeighborSignal(worldPosition) > 0 : false;
    }

    @Override
    public BlockEntityPropeller getPropeller() {
        return this;
    }
}
