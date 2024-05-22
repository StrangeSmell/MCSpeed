package com.strangesmell.mcspeed.Massage;

import com.strangesmell.mcspeed.MCSpeed;
import com.strangesmell.mcspeed.Massage.C2SMassage;
import com.strangesmell.mcspeed.Massage.S2CClockTimeMassage;
import com.strangesmell.mcspeed.SpeedBoat;
import com.strangesmell.mcspeed.blocks.EndBlockEntity;
import com.strangesmell.mcspeed.blocks.RecodeBlockEntity;
import com.strangesmell.mcspeed.blocks.StartBlock;
import com.strangesmell.mcspeed.blocks.StartBlockEntity;
import com.strangesmell.mcspeed.savedate.SpeedData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.permission.PermissionAPI;

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
    public static void handlePacket2(S2CClockTimeMassage msg, Supplier<NetworkEvent.Context> ctx) {


        Entity entity = ctx.get().getSender().getControlledVehicle();

        if(entity instanceof SpeedBoat speedBoat){
            BlockPos blockpos = speedBoat.getOnPosLegacy();
            BlockState blockstate = speedBoat.level().getBlockState(blockpos);
            if(blockstate.getBlock() instanceof StartBlock startBlock){
                startBlock.clockName = msg.clockName;
                startBlock.clockTime = msg.clockTime;

            }
        }

    }
    public static void handlePacket3(C2SStartMessage msg, Supplier<NetworkEvent.Context> ctx) {
        //PermissionAPI.getPermission(ctx.get().getSender(), MCSpeed.USE_START_OR_END)
        if(true){
            BlockEntity blockEntity = ctx.get().getSender().level().getBlockEntity(msg.blockPos);
            if(blockEntity instanceof StartBlockEntity startBlockEntity){
                startBlockEntity.clockTime=msg.clockTime;
                startBlockEntity.clockName=msg.clockName;
            }
            if(blockEntity instanceof EndBlockEntity endBlockEntity){
                endBlockEntity.clockTime=msg.clockTime;
                endBlockEntity.clockName=msg.clockName;
            }
        }else {
            ctx.get().getSender().sendSystemMessage(Component.translatable("mcspeed.no_permission").append(MCSpeed.USE_START_OR_END.getNodeName()));
        }
    }

    public static void handlePacket4(RecodeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        if(Minecraft.getInstance().level.getBlockEntity(msg.blockPos) instanceof RecodeBlockEntity recodeBlockEntity){
            recodeBlockEntity.everyTime=msg.everyTime;
        }

    }

    public static void handlePacket5(C2SDeleteRecode msg, Supplier<NetworkEvent.Context> ctx) {
        //PermissionAPI.getPermission(ctx.get().getSender(), MCSpeed.DELETE_RECODE)
        if(true){
            SpeedData.get(ctx.get().getSender().getServer()).everyTime.remove(msg.name);
            SpeedData.get(ctx.get().getSender().getServer()).setDirty();
        }else{
            ctx.get().getSender().sendSystemMessage(Component.translatable("mcspeed.no_permission").append(MCSpeed.DELETE_RECODE.getNodeName()));
        }
    }
}
