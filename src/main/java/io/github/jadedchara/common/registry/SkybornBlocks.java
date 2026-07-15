package io.github.jadedchara.common.registry;

import io.github.jadedchara.Skyborn;
import io.github.jadedchara.common.block.AltitudeControlBlock;
import io.github.jadedchara.common.block.PoweredThrusterBlock;
import io.github.jadedchara.common.block.VerticalBoosterBlock;
import io.github.jadedchara.common.block.entity.AltitudeControlBlockEntity;
import io.github.jadedchara.common.block.entity.PoweredThrusterBlockEntity;
import io.github.jadedchara.common.block.entity.VerticalBoosterBlockEntity;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SkybornBlocks {
    public static final Block POWERED_THRUSTER = register(
            new PoweredThrusterBlock(BlockBehaviour.Properties.of()),
            "powered_thruster",
            true
    );
    public static final BlockEntityType<PoweredThrusterBlockEntity> POWERED_THRUSTER_BLOCKENTITY =
            register("powered_thruster_blockentity", PoweredThrusterBlockEntity::new, SkybornBlocks.POWERED_THRUSTER);
    public static final Block VERTICAL_BOOSTER = register(
            new VerticalBoosterBlock(BlockBehaviour.Properties.of()),
            "vertical_booster",
            true
    );
    public static final BlockEntityType<VerticalBoosterBlockEntity> VERTICAL_BOOSTER_BLOCKENTITY =
            register("vertical_booster_blockentity", VerticalBoosterBlockEntity::new, SkybornBlocks.VERTICAL_BOOSTER);
    public static final Block ALTITUDE_CONTROL = register(
            new AltitudeControlBlock(BlockBehaviour.Properties.of()),
            "altitude_control",
            true
    );
    public static final BlockEntityType<AltitudeControlBlockEntity> ALTITUDE_CONTROL_BLOCKENTITY =
            register("altitude_control_blockentity", AltitudeControlBlockEntity::new, SkybornBlocks.ALTITUDE_CONTROL);



    //Utils

    public static <T extends BlockEntity> BlockEntityType<T> register(
            String n,
            BlockEntityType.BlockEntitySupplier<? extends T> e,
            Block b
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Skyborn.MOD_ID, n);
        return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id,
                BlockEntityType.Builder.<T>of(e, b).build()
        );
    }
    public static Block register(Block b, String n, boolean s) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Skyborn.MOD_ID, n);
        if (s) {
            BlockItem blockItem = new BlockItem(b, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, b);
    }

    //Final Registration
    public static final ResourceKey<CreativeModeTab> SKYBORN_BLOCKS_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(Skyborn.MOD_ID, "blocks")
    );
    public static final CreativeModeTab SKYBORN_BLOCKS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SkybornBlocks.POWERED_THRUSTER.asItem()))
            .title(Component.translatable("itemGroup.skyborn.blocks"))
            .build();

    public static void init(){
        Skyborn.LOGGER.info("Registering Blocks!");
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SKYBORN_BLOCKS_KEY, SKYBORN_BLOCKS_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(SKYBORN_BLOCKS_KEY).register(itemGroup -> {
            itemGroup.accept(SkybornBlocks.POWERED_THRUSTER.asItem());
            //
        });
    }
}
