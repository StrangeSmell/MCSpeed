package com.strangesmell.mcspeed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import static com.strangesmell.mcspeed.Util.AN2OTime;


public class HUD extends ForgeGui {
    protected static final ResourceLocation LAVA_BUCKET_LOCATION = new ResourceLocation("textures/item/lava_bucket.png");
    protected static final ResourceLocation BUCKET_LOCATION = new ResourceLocation("textures/item/bucket.png");
    public HUD(Minecraft mc) {
        super(mc);
    }


    public void render2(GuiGraphics guiGraphics, float partialTick)
    {

        if (this.minecraft.player.getControlledVehicle() instanceof SpeedBoat speedBoat && minecraft.gameMode.hasExperience())
        {
            if(speedBoat.getN2O()==0){
                renderTextureOverlay1(guiGraphics,BUCKET_LOCATION,1.0f);
                renderTextureOverlay2(guiGraphics,BUCKET_LOCATION,1.0f);
            }
            if(speedBoat.getN2O()==1){
                renderTextureOverlay1(guiGraphics,LAVA_BUCKET_LOCATION,1.0f);
                renderTextureOverlay2(guiGraphics,BUCKET_LOCATION,1.0f);
            }
            if(speedBoat.getN2O()==2){
                renderTextureOverlay1(guiGraphics,LAVA_BUCKET_LOCATION,1.0f);
                renderTextureOverlay2(guiGraphics,LAVA_BUCKET_LOCATION,1.0f);
            }
            if((speedBoat.getAfterPiaoyi()<15 && speedBoat.getAfterPiaoyi() > 0) || (speedBoat.getOnLand()>0&&speedBoat.getOnLand()<15) || (speedBoat.getInAir()>0&&speedBoat.getInAir()<15)){
                renderTextureOverlay3(guiGraphics,LAVA_BUCKET_LOCATION,1.0f);
            }
        }
    }

    protected void renderTextureOverlay1(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0,  0.0F, 0.0F, 16, 16, 16,16);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderTextureOverlay2(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 16, 0,  0.0F, 0.0F, 16, 16, 16,16);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderTextureOverlay3(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, pGuiGraphics.guiWidth() / 2 - 91, pGuiGraphics.guiHeight() - 70,  0.0F, 0.0F, 8, 8, 8,8);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderExperience2(GuiGraphics guiGraphics)
    {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font,String.format("%.2f",Minecraft.getInstance().player.getControlledVehicle().getDeltaMovement().length())+"m/s",guiGraphics.guiWidth() / 2 ,guiGraphics.guiHeight() - 60,0xFFFFFF);
        if (minecraft.gameMode.hasExperience())
        {
            renderExperienceBar(guiGraphics, guiGraphics.guiWidth() / 2 - 91);
        }
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    /**
     * Renders the experience bar on the screen using the provided GuiGraphics object and x-coordinate.
     * @param pGuiGraphics the GuiGraphics object used for rendering.
     * @param pX the x-coordinate for rendering the experience bar.
     */
    public void renderExperienceBar(GuiGraphics pGuiGraphics, int pX) {
        this.minecraft.getProfiler().push("N2OBar");

        assert this.minecraft.player != null;
        SpeedBoat speedBoat = (SpeedBoat) (this.minecraft.player.getControlledVehicle());
        int i =AN2OTime - speedBoat.getPiaoyiTime();//getXpNeededForNextLevel
        int have =  speedBoat.getPiaoyiTime();
        if (i > 0) {
            int j = 182;
            int k = (int)((float)have/ (float) AN2OTime * 183.0F);
            int l = pGuiGraphics.guiHeight() - 32 - 20;
            pGuiGraphics.blit(GUI_ICONS_LOCATION, pX, l, 0, 64, 182, 5);
            if (k > 0) {
                pGuiGraphics.blit(GUI_ICONS_LOCATION, pX, l, 0, 69, k, 5);
            }
        }

        this.minecraft.getProfiler().pop();
        if (have > 0) {
            this.minecraft.getProfiler().push("expLevel");
            String s = "" + have;
            int i1 = (this.screenWidth - this.getFont().width(s)) / 2;
            int j1 = this.screenHeight - 31 - 4;
            pGuiGraphics.drawString(this.getFont(), s, i1 + 1, j1, 0, false);
            pGuiGraphics.drawString(this.getFont(), s, i1 - 1, j1, 0, false);
            pGuiGraphics.drawString(this.getFont(), s, i1, j1 + 1, 0, false);
            pGuiGraphics.drawString(this.getFont(), s, i1, j1 - 1, 0, false);
            pGuiGraphics.drawString(this.getFont(), s, i1, j1, 8453920, false);
            this.minecraft.getProfiler().pop();
        }

    }

}
