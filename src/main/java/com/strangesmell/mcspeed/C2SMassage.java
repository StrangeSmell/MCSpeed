package com.strangesmell.mcspeed;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMassage {


    private boolean piaoYiIsDown;
    private boolean daPenIsDown;
    private boolean xiaoPenIsDown;
    private boolean upIsDown;
    private boolean downIsDown;
    private boolean leftIsDown;
    private boolean rightIsDown;

    public C2SMassage( boolean piaoYiIsDown,boolean daPenIsDown,boolean xiaoPenIsDown,boolean upIsDown,boolean downIsDown,boolean leftIsDown,boolean rightIsDown) {
        this.piaoYiIsDown=piaoYiIsDown;
        this.daPenIsDown=daPenIsDown;
        this.xiaoPenIsDown=xiaoPenIsDown;
        this.upIsDown=upIsDown;
        this.downIsDown=downIsDown;
        this.leftIsDown=leftIsDown;
        this.rightIsDown=rightIsDown;
    }



    public C2SMassage(FriendlyByteBuf buf) {
        piaoYiIsDown=buf.readBoolean();
        daPenIsDown=buf.readBoolean();
        xiaoPenIsDown=buf.readBoolean();
        upIsDown=buf.readBoolean();
        downIsDown=buf.readBoolean();
        leftIsDown=buf.readBoolean();
        rightIsDown=buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(piaoYiIsDown);
        buf.writeBoolean(daPenIsDown);
        buf.writeBoolean(xiaoPenIsDown);
        buf.writeBoolean(upIsDown);
        buf.writeBoolean(downIsDown);
        buf.writeBoolean(leftIsDown);
        buf.writeBoolean(rightIsDown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

                    ClientPacketHandler.handlePacket(new C2SMassage( piaoYiIsDown,daPenIsDown,xiaoPenIsDown,upIsDown,downIsDown,leftIsDown,rightIsDown), supplier);

        });
        context.setPacketHandled(true);
        return true;
    }
    public boolean isPiaoYiIsDown() {
        return piaoYiIsDown;
    }

    public boolean isDaPenIsDown() {
        return daPenIsDown;
    }

    public boolean isXiaoPenIsDown() {
        return xiaoPenIsDown;
    }

    public boolean isUpIsDown() {
        return upIsDown;
    }

    public boolean isDownIsDown() {
        return downIsDown;
    }

    public boolean isLeftIsDown() {
        return leftIsDown;
    }

    public boolean isRightIsDown() {
        return rightIsDown;
    }

}
