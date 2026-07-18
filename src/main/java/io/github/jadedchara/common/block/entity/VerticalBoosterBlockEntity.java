package io.github.jadedchara.common.block.entity;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import io.github.jadedchara.client.screen.FuelAccessHandler;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class VerticalBoosterBlockEntity extends BlockEntity implements BlockEntitySubLevelActor, Container,
        MenuProvider {
    public NonNullList<ItemStack> VBEinventory = NonNullList.withSize(18, ItemStack.EMPTY);

    public float thrust = 0.0F;
    public float counter = 0.0F; // = 1200F;
    public float targetAltitude = 90.0F;

    public VerticalBoosterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SkybornBlocks.VERTICAL_BOOSTER_BLOCKENTITY, blockPos, blockState);
    }



    public boolean isPowered() {
        return (
                (this.getRedstonePower())/* &&
                (level
                        .getBlockEntity(
                                this.getBlockPos().above()
                        ) instanceof AltitudeControlBlockEntity
                ) &&
                (this.getInventory().contains(Items.COAL) ||
                        this.getInventory().contains(Items.CHARCOAL) ||
                        this.getInventory().contains(Items.COAL_BLOCK) ||
                        this.getInventory().contains(Items.LAVA_BUCKET)
                )*/
        );
    }

    private boolean getRedstonePower() {
        return level != null && level.getBestNeighborSignal(worldPosition) > 0;
    }

    private float getTargetAltitude(){
        if(level.getBlockEntity(this.getBlockPos().above()) instanceof AltitudeControlBlockEntity acbe){
            return acbe.getAltitude();
            //return targetAltitude;
        }else{
            return targetAltitude;
        }
    }

    public static void tick(Level l, BlockPos bp, BlockState bs, VerticalBoosterBlockEntity e){
        if(!e.isPowered()){
            //e.counter = 0.0F;
        }else{
            for(int i = 0;i < 18; i++) {
                ItemStack is = e.getItem(i);
                if (is.getItem().equals(Items.NETHER_STAR)) {
                    //MAGIC
                    return;
                } else if ((e.counter % 194400F == 0.0F) && is.getItem().equals(Items.LAVA_BUCKET)) {
                    //LA LA LA LAVA! CH CH CH CHICKEN!
                    e.getInventory().set(i,Items.BUCKET.getDefaultInstance());
                    return;
                } else if ((e.counter % 21600F == 0.0F) && is.getItem().equals(Items.COAL_BLOCK)) {
                    //Coal Block, ninth of Lava Bucket
                    is.shrink(1);
                } else if ((e.counter % 2400F == 0.0F) && is.getItem().equals(Items.COAL)) {
                    //Coal, ninth of Coal Block
                    is.shrink(1);
                    return;
                } else if ((e.counter % 2000F == 0.0F) && is.getItem().equals(Items.CHARCOAL)) {
                    //Charcoal, little more than half as good as Coal
                    is.shrink(1);
                    return;
                }
            }
            if(e.counter<12000F){
                e.counter++;
            }else {
                e.counter = 0.0F;
            }

        }
    }

    @Override
    public void sable$physicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {
        if(
                !this.isPowered() ||
                        !this.getDestackedInventory().contains(Items.COAL) ||
                        !(this.getLevel().getBlockEntity(this.getBlockPos().above()) instanceof AltitudeControlBlockEntity)

        ){
            //System.out.println(this.getInventory());
            return;
        }
        Vec3 truePosition = Sable.HELPER.projectOutOfSubLevel(subLevel.getLevel(),this.getBlockPos().getCenter());
        double trueElevation = truePosition.y;

        //OBJECTIVES
        /*
        If the vertical speed is 0, or below, AND the target's above begin calculating distance.
        store the value until our vertical condition's satisfied.
        If you're above the target, implement deceleration measures, to ease into place.
         */

        //First, we set the thrust...
        if(this.isPowered() && trueElevation < this.getTargetAltitude()-0.25 && this.thrust < 5){
            this.thrust += 0.025F;
        }else if((!this.isPowered() || trueElevation > this.getTargetAltitude()+0.25) && this.thrust > 0){
            this.thrust -= 0.025F;
        }else if(trueElevation >= this.getTargetAltitude()-0.25 && trueElevation <= this.getTargetAltitude()+0.25){
            //this.thrust = 0;
        }

        //...and then we apply it, based on whether the craft has reached the target.
        if(this.isPowered() && trueElevation > this.getTargetAltitude()-0.25 && trueElevation < this.getTargetAltitude()+0.25){
            subLevel.getOrCreateQueuedForceGroup(ForceGroups.GRAVITY.get()).reset();
            Sable.HELPER.getVelocity(subLevel.getLevel(),subLevel.logicalPose().position());
            handle.addLinearAndAngularVelocity(
                    JOMLConversion.toJOML(new Vec3(0,subLevel.latestLinearVelocity.y*-1,0)),
                    JOMLConversion.toJOML(new Vec3(0,0,0))
            );
        }else{

            handle.applyLinearImpulse(JOMLConversion.toJOML(
                    subLevel.logicalPose().transformNormalInverse(
                            new Vec3(0,thrust,0)
                    )
            ));
        }
    }


    //---------------------------------------------------------------
    //NBT
    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putFloat("Thrust", thrust);
        nbt.putFloat("BurningCounter",counter);
        ContainerHelper.saveAllItems(nbt, this.VBEinventory, registryLookup);
    }
    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        thrust = nbt.getFloat("Thrust");
        counter = nbt.getFloat("BurningCounter");
        ContainerHelper.loadAllItems(nbt, this.VBEinventory, registryLookup);

    }



    //---------------------------------------------------------------
    //Inventory Fuckery


    public NonNullList<ItemStack> getInventory() {
        return this.VBEinventory;
    }

    public NonNullList<Item> getDestackedInventory(){
        NonNullList<Item> returnables = NonNullList.createWithCapacity(18);
        for(ItemStack item : this.getInventory()){
            returnables.add(item.getItem());
        }
        return returnables;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FuelAccessHandler(i,inventory,this);

    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack s = this.getItem(i);
            if (!s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.getInventory().get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack r = ContainerHelper.removeItem(this.getInventory(), i, j);
        if (!r.isEmpty()) {
            this.setChanged();
        }
        return r;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.getInventory(), i);
    }

    @Override
    public void setItem(int i, ItemStack s) {
        this.getInventory().set(i, s);
        if (s.getCount() > s.getMaxStackSize()) {
            s.setCount(s.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.getInventory().clear();
        this.setChanged();
    }

}
