package com.strangesmell.mcspeed.massage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CClockTimeMassage {
    public String clockName;
    public int clockTime;

    public S2CClockTimeMassage( String clockName, int clockTime) {
        this.clockName=clockName;
        this.clockTime=clockTime;
    }
    public S2CClockTimeMassage(FriendlyByteBuf buf ) {
        clockName=buf.readUtf();
        clockTime=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(clockName);
        buf.writeInt(clockTime);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->  ClientPacketHandler.handlePacket2(new S2CClockTimeMassage(clockName,clockTime), supplier));

        });
        context.setPacketHandled(true);
        return true;
    }
}
