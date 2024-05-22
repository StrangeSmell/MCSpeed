package com.strangesmell.mcspeed.massage;

import com.strangesmell.mcspeed.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class RecodeMessage {
    public HashMap<String,String[]> everyTime = new HashMap<String, String[]>();
    public BlockPos blockPos ;
    public RecodeMessage( HashMap everyTime,BlockPos blockPos) {
        this.everyTime=everyTime;
        this.blockPos=blockPos;
    }
    public RecodeMessage(FriendlyByteBuf buf ) {
        this.everyTime=buf.readMap(HashMap::new,FriendlyByteBuf::readUtf,Util::readVarStringArray);
        this.blockPos=buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        FriendlyByteBuf.Writer<String[]> valueWriter = (buf2, value) -> Util.writeVarStringArray(value, buf2);
        buf.writeMap(this.everyTime,FriendlyByteBuf::writeUtf,valueWriter);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->  ClientPacketHandler.handlePacket4(new RecodeMessage(everyTime,blockPos), supplier));

        });
        context.setPacketHandled(true);
        return true;

    }
}
