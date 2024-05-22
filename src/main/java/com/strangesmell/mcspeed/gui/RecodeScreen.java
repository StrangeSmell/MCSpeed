package com.strangesmell.mcspeed.gui;

import com.strangesmell.mcspeed.Massage.C2SDeleteRecode;
import com.strangesmell.mcspeed.Massage.Channel;
import com.strangesmell.mcspeed.blocks.RecodeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecodeScreen extends Screen {
    private static final Component DELETE = Component.translatable("mcspeed.recode_delete");
    private static final Component SUGGESTION = Component.translatable("mcspeed.recode_select");
    private static final Component NEXT = Component.translatable("mcspeed.recode_next");
    private static final Component PREVIOUS = Component.translatable("mcspeed.recode_previous");

    protected EditBox nameEdit;
    protected int page;
    protected HashMap<String,String[]> everyTime = new HashMap<>();
    protected String deleteName;
    //protected List<Button> buttons=new ArrayList<>(10);
    protected Button[] buttons=new Button[10];
    protected Button deleteButton1;
    protected Button deleteButton2;
    protected Button deleteButton3;
    protected Button deleteButton4;
    protected Button deleteButton5;
    protected Button deleteButton6;
    protected Button deleteButton7;
    protected Button deleteButton8;
    protected Button deleteButton9;
    protected Button deleteButton10;

    protected Button nextButton;
    protected Button previousButton;
    private static RecodeBlockEntity blockEntity;

    public RecodeScreen(Component p_96550_, RecodeBlockEntity recodeBlockEntity) {
        super(p_96550_);
        blockEntity= recodeBlockEntity;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 然后是窗口小部件，如果这是Screen的直接子项
        this.nameEdit.render( graphics,  mouseX,  mouseY,  partialTick);
        // 在小部件之后渲染的内容（工具提示）
        graphics.drawCenteredString(this.font, SUGGESTION, 20, 15, 16777215);
        String name = this.nameEdit.getValue();

        if(name.equals("")){
            int size = blockEntity.everyTime.size();
            Object[] keyList = blockEntity.everyTime.keySet().toArray();
            if(page*10+10<size){
                nextButton.render( graphics,  mouseX,  mouseY,  partialTick);
            }
            if(page>0) previousButton.render(graphics,  mouseX,  mouseY,  partialTick);

            for(int i = 0; i+page*10<size&&i<10;i++){

                Object key = keyList[i+page*10];//地图
                if(buttons[i].isMouseOver(mouseX,mouseY)) deleteName = key.toString();

                this.buttons[i].render( graphics,  mouseX,  mouseY,  partialTick);
                String playerName = blockEntity.everyTime.get(key)[1];
                int time = Integer.parseInt(blockEntity.everyTime.get(key)[0]) ;

                graphics.drawString(this.font,
                        Component.translatable("mcspeed.map").append(key.toString()).append(":").append(Component.translatable("mcspeed.best_player")).append(playerName).append(":").append(Component.translatable("mcspeed.time")).append(String.format("%02d",time/1200)).append("'").append(String.format("%02d",time/20%60)).append("'").append(String.format("%02d",time%20*5)),
                        30, this.height / 8 + 20 * i+3 , 16777215 );
            }
        }else{
            HashMap<String,String[]> values = new HashMap<>();

            for (String key : blockEntity.everyTime.keySet()) {
                if (key.contains(name)||blockEntity.everyTime.get(key)[1].contains(name)) {
                    values.put(key,blockEntity.everyTime.get(key));
                }
            }
            int size = values.size();
            Object[] keyList = values.keySet().toArray();
            if(page*10+9<size){
                nextButton.render( graphics,  mouseX,  mouseY,  partialTick);
            }
            if(page>0) previousButton.render(graphics,  mouseX,  mouseY,  partialTick);
            for(int i = 0; i+page*10<size&&i<10;i++){
                Object map = keyList[i];
                if(buttons[i].isMouseOver(mouseX,mouseY)) deleteName = map.toString();
                this.buttons[i].render( graphics,  mouseX,  mouseY,  partialTick);
                String playerName = values.get(map)[1];
                int time = Integer.parseInt(values.get(map)[0]);
                graphics.drawString(this.font,
                        Component.translatable("mcspeed.map").append(map.toString()).append(":").append(Component.translatable("mcspeed.best_player")).append(playerName).append(":").append(Component.translatable("mcspeed.time")).append(String.format("%02d",time/1200)).append("'").append(String.format("%02d",time/20%60)).append("'").append(String.format("%02d",time%20*5)),
                        30, this.height / 8 + 20 * i +3, 16777215 );
            }
        }

    }

    @Override
    protected void init() {
        super.init();
        page=0;
        if(blockEntity!=null){
            buttons[0] = deleteButton1;
            buttons[1] = deleteButton2;
            buttons[2] = deleteButton3;
            buttons[3] = deleteButton4;
            buttons[4] = deleteButton5;
            buttons[5] = deleteButton6;
            buttons[6] = deleteButton7;
            buttons[7] = deleteButton8;
            buttons[8] = deleteButton9;
            buttons[9] = deleteButton10;
            for(int i = 0; i<10;i++){
                buttons[i]= this.addRenderableWidget(Button.builder(DELETE, (p_97691_) -> {
                    this.onDone();//pos(p_254166_, p_253872_).size(
                }).bounds(this.width - 60, this.height / 8 + 20 * i, 30, 17).build());
            }
        }

        nextButton = this.addRenderableWidget(Button.builder(NEXT, (p_97691_) -> {
            this.nextDone();//pos(p_254166_, p_253872_).size(
        }).bounds(this.width-60,  this.height -25, 30, 20).build());

        previousButton = this.addRenderableWidget(Button.builder(PREVIOUS, (p_97691_) -> {
            this.previousDone();//pos(p_254166_, p_253872_).size(
        }).bounds(30 , this.height -25, 30, 20).build());

        this.nameEdit = new EditBox(this.font, this.width / 2 - 150, 10, 300, 15, Component.translatable("mcspeed.command"));
        this.nameEdit.setMaxLength(32500);
        this.addRenderableWidget(nameEdit);
    }
    protected void nextDone() {
        page++;
    }
    protected void previousDone() {
        page--;
    }
    protected void onDone() {
        // 在此处停止任何处理器,这里向服务器返回数据包
        if(deleteName == null) return;
        Channel.sendToServer(new C2SDeleteRecode(deleteName));
        blockEntity.everyTime.remove(deleteName);


    }

    public boolean isPauseScreen() {
        return false;
    }

}
