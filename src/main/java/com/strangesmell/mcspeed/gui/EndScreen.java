package com.strangesmell.mcspeed.gui;

import com.strangesmell.mcspeed.blocks.EndBlockEntity;
import com.strangesmell.mcspeed.blocks.StartBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EndScreen extends Screen {
    private static final Component DONE = Component.translatable("mcspeed.clock_done");
    private static final Component SUGGESTION = Component.translatable("mcspeed.start_suggestion");
    private static final Component SUGGESTION2 = Component.translatable("mcspeed.start_suggestion2");
    protected EditBox nameEdit;
    protected EditBox timeEdit;
    protected Button doneButton;
    private static EndBlockEntity blockEntity;
    public EndScreen(Component p_96550_, EndBlockEntity endBlockEntity) {
        super(p_96550_);
        blockEntity  = endBlockEntity;
    }
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        // 然后是窗口小部件，如果这是Screen的直接子项
        super.render(graphics, mouseX, mouseY, partialTick);
        // 在小部件之后渲染的内容（工具提示）
        graphics.drawCenteredString(this.font,SUGGESTION,this.width / 2, 20, 16777215);
        graphics.drawCenteredString(this.font,SUGGESTION2,this.width / 2, 29, 16777215);


    }
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    protected void init() {
        super.init();
        this.doneButton = this.addRenderableWidget(Button.builder(DONE, (p_97691_) -> {
            this.onDone();
        }).bounds(this.width / 2 - 70, this.height / 4 + 120 + 12, 150, 20).build());

        this.nameEdit = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, Component.translatable("mcspeed.command"));
        this.nameEdit.setMaxLength(32500);
        this.nameEdit.setValue(blockEntity.clockName);
        //this.nameEdit.setResponder(this::onEdited);

        this.timeEdit = new EditBox(this.font, this.width / 2 - 150, 80, 300, 20, Component.translatable("advMode.command"));
        this.timeEdit.setMaxLength(32500);
        this.timeEdit.setValue(String.valueOf(blockEntity.clockTime));
        //this.nameEdit.setResponder(this::onEdited);

        // 添加小部件和已预计算的值
        this.addRenderableWidget(nameEdit);
        this.addRenderableWidget(timeEdit);
        this.setInitialFocus(this.nameEdit);
    }

    protected void onDone() {
        // 在此处停止任何处理器,这里向服务器返回数据包
        blockEntity.clockTime=Integer.parseInt(timeEdit.getValue());
        blockEntity.clockName=nameEdit.getValue();
        // 最后调用，以防干扰重写后的方法体
        //Channel.sendToServer(new C2SStartMessage(blockEntity.clockName,blockEntity.clockTime));
        blockEntity.needSync();
        assert this.minecraft != null;
        this.minecraft.setScreen((Screen)null);
    }
}
