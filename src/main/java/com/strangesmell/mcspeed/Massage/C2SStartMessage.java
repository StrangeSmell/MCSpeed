package com.strangesmell.mcspeed.Massage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SStartMessage {

    public String clockName;
    public int clockTime;
    public BlockPos blockPos ;

    public C2SStartMessage( String clockName,int clockTime,BlockPos blockPos) {
        this.clockName=clockName;
        this.clockTime=clockTime;
        this.blockPos=blockPos;

    }

    public C2SStartMessage(FriendlyByteBuf buf) {
        clockName=buf.readUtf();
        clockTime=buf.readInt();
        blockPos=buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(clockName);
        buf.writeInt(clockTime);
        buf.writeBlockPos(blockPos);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ClientPacketHandler.handlePacket3(new C2SStartMessage( clockName,clockTime,blockPos), supplier);

        });
        context.setPacketHandled(true);
        return true;
    }
}
