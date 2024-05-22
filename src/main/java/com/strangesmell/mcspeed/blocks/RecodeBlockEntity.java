package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.MCSpeed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;


public class RecodeBlockEntity extends BlockEntity {
    public HashMap<String,String[]> everyTime = new HashMap<String, String[]>();
    public String clockName="";
    public int clockTime=0;
    public String playerName="";
    public int laps=0;
    public RecodeBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    public RecodeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MCSpeed.RecodeBlockEntity.get(), pPos, pBlockState);
    }

}
