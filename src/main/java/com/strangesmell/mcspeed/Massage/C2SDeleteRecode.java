package com.strangesmell.mcspeed.Massage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDeleteRecode {

    public String name;

    public C2SDeleteRecode( String name) {
        this.name=name;
    }

    public C2SDeleteRecode(FriendlyByteBuf buf) {
        name=buf.readUtf();

    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(name);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ClientPacketHandler.handlePacket5(new C2SDeleteRecode(name), supplier);

        });
        context.setPacketHandled(true);
        return true;
    }
}
