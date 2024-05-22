package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.MCSpeed;
import com.strangesmell.mcspeed.massage.C2SStartMessage;
import com.strangesmell.mcspeed.massage.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EndBlockEntity extends BlockEntity {
    public String clockName="";
    public int clockTime=0;
    public int laps=0;
    public EndBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    public EndBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MCSpeed.EndBlockEntity.get(), pPos, pBlockState);
    }
    public void load(CompoundTag pTag) {
        super.load(pTag);
        clockName=pTag.getString("clockName");
        clockTime=pTag.getInt("clockTime");
        laps=pTag.getInt("laps");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("clockName",clockName);
        pTag.putInt("clockTime",clockTime);
        pTag.putInt("laps",laps);
        markUpdated();
        //level.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),2);
    }
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        //将你的数据写入标签
        tag.putString("clockName",clockName);
        tag.putInt("clockTime",clockTime);
        tag.putInt("laps",laps);
        return tag;

    }

    public void markUpdated() {
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
    public void needSync(){
        assert this.level != null;
        if(!level.isClientSide) return;
        //level.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),2);
        Channel.sendToServer(new C2SStartMessage(this.clockName,this.clockTime,this.getBlockPos()));
    }
}
