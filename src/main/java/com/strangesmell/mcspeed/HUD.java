package com.strangesmell.mcspeed;

import com.mojang.blaze3d.systems.RenderSystem;
import com.strangesmell.mcspeed.blocks.StartBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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

        if (this.minecraft.player.getControlledVehicle() instanceof SpeedBoat speedBoat)
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
    //N2O
    protected void renderTextureOverlay1(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0,  0.0F, 0.0F, 16, 16, 16,16);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    //N2O
    protected void renderTextureOverlay2(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 16, 0,  0.0F, 0.0F, 16, 16, 16,16);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    //小喷
    protected void renderTextureOverlay3(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, pGuiGraphics.guiWidth() / 2 - 91, pGuiGraphics.guiHeight() - 70,  0.0F, 0.0F, 8, 8, 8,8);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    //速度,集气,计时
    public void renderExperience2(GuiGraphics guiGraphics,SpeedBoat speedBoat)
    {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        int color ;



        //速度
        guiGraphics.drawCenteredString(Minecraft.getInstance().font,String.format("%.2f",speedBoat.getDeltaMovement().length()*20)+"m/s",guiGraphics.guiWidth() / 2 ,guiGraphics.guiHeight() - 60,0xFFFFFF);
        //计时
        int clock = speedBoat.getClock();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.time").append(String.format("%02d",clock/1200)).append("'").append(String.format("%02d",clock/20%60)).append("'").append(String.format("%02d",clock%20*5)) ,guiGraphics.guiWidth()-30,2,0xFFFFFF);
        //最佳记录
        clock = speedBoat.getClockInt();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.best_time").append(String.format("%02d",clock/1200)).append("'").append(String.format("%02d",clock/20%60)).append("'").append(String.format("%02d",clock%20*5)) ,guiGraphics.guiWidth()-30,15,0xFFFFFF);

        //个人记录
        clock=speedBoat.getSelfClock();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.self_time").append(String.format("%02d",clock/1200)).append("'").append(String.format("%02d",clock/20%60)).append("'").append(String.format("%02d",clock%20*5)) ,guiGraphics.guiWidth()-38,28,0xFFFFFF);
        //地图名、纪录保持者
        guiGraphics.drawCenteredString(Minecraft.getInstance().font,Component.translatable("mcspeed.map").append(speedBoat.getClockName()),guiGraphics.guiWidth()/2 ,2,0xFFFFFF);
        if(Minecraft.getInstance().player.getName().getString().equals(speedBoat.getClockBestName())&&speedBoat.getSelfClock()!=0){
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.best_player_is_yourself"),guiGraphics.guiWidth()/2 ,15,0xFF0000);
        }else {
            if(speedBoat.getClockInt()==0){
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.best_player"),guiGraphics.guiWidth()/2 ,15,0xFFFFFF);

            }else{
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("mcspeed.best_player").append(speedBoat.getClockBestName()) ,guiGraphics.guiWidth()/2 ,15,0xFFFFFF);

            }

        }


        //集气
        renderExperienceBar(guiGraphics, guiGraphics.guiWidth() / 2 - 91);

        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    //集气
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
