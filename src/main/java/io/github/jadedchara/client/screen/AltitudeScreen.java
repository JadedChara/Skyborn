package io.github.jadedchara.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AltitudeScreen extends AbstractContainerScreen<AltitudeScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.parse("textures/gui/container/dispenser.png");

    public MultiLineEditBox floatInput;
    public Button sendInput;
    private AltitudeScreenHandler handler;

    public AltitudeScreen(AltitudeScreenHandler a, Inventory i, Component c) {
        super(a, i, c);
        this.handler = a;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }
    @Override
    protected void init() {
        super.init();
        // Center the title
        titleLabelX = (imageWidth - font.width(title)) / 2;

        //
        floatInput = new MultiLineEditBox(
                this.font,
                this.width/2,
                this.height/2,
                16,
                16,Component.literal(""),
                Component.translatable("skyborn.prompt.altitude.input")
        );
        floatInput.setCharacterLimit(5);
        floatInput.setValueListener((var)->{
            try{
                Float.parseFloat(var);
            }catch(Exception e){
                floatInput.setValue("");
            }
        });
        sendInput = Button
                .builder(
                        Component.literal("Set Altitude"),
                        button->{
                                    try{
                                        this.handler.setAltitude(Float.parseFloat(floatInput.getValue()));
                                    }catch(Exception e){
                                        floatInput.setValue("");
                                    }
                                }
                        )
                .pos(this.width/2,this.height/2-16).build();
    }
}
