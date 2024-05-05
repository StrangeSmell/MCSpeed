package com.strangesmell.mcspeed;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handlePacket(C2SMassage msg, Supplier<NetworkEvent.Context> ctx) {
        Entity entity = ctx.get().getSender().getControlledVehicle();
        if(entity instanceof SpeedBoat speedBoat){

            speedBoat.setPiaoYiIsDown(msg.isPiaoYiIsDown());
            speedBoat.setDaPenIsDown(msg.isDaPenIsDown());
            speedBoat.setXiaoPenIsDown(msg.isXiaoPenIsDown());
            speedBoat.setUpIsDown(msg.isUpIsDown());
            speedBoat.setDownIsDown(msg.isDownIsDown());
            speedBoat.setLeftIsDown(msg.isLeftIsDown());
            speedBoat.setRightIsDown(msg.isRightIsDown());

        }

    }

}
