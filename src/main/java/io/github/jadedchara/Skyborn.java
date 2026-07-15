package io.github.jadedchara;

import io.github.jadedchara.client.screen.AltitudeScreenHandler;
import io.github.jadedchara.client.screen.FuelAccessHandler;
import io.github.jadedchara.common.registry.SkybornBlocks;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skyborn implements ModInitializer {
	public static final String MOD_ID = "skyborn";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//net.minecraft.resources.ResourceLocation
	public static final MenuType<FuelAccessHandler> FUEL_ACCESS_HANDLER =
			Registry.register(
					BuiltInRegistries.MENU,
					ResourceLocation.fromNamespaceAndPath("skyborn","fuel_access"),
					new MenuType<>(
							(syncId,inventory)->
                                    new FuelAccessHandler(syncId, inventory),
							FeatureFlagSet.of())

			);
	public static final MenuType<AltitudeScreenHandler> ALTITUDE_SCREEN_HANDLER =
			Registry.register(
					BuiltInRegistries.MENU,
					ResourceLocation.fromNamespaceAndPath("skyborn","altitude_screen"),
					new MenuType<>(
							(syncId,inventory)->
									new AltitudeScreenHandler(syncId, inventory),
							FeatureFlagSet.of())

			);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric and/or Quilt world! Take to the skies!");
		SkybornBlocks.init();
	}
}