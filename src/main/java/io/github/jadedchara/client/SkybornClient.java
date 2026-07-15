package io.github.jadedchara.client;

import io.github.jadedchara.Skyborn;
import io.github.jadedchara.client.screen.FuelAccessScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class SkybornClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(Skyborn.FUEL_ACCESS_HANDLER, FuelAccessScreen::new);
    }
}

