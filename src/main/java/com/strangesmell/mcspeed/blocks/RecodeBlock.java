package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.massage.Channel;
import com.strangesmell.mcspeed.massage.RecodeMessage;
import com.strangesmell.mcspeed.gui.RecodeScreen;
import com.strangesmell.mcspeed.savedate.SpeedData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;


public class RecodeBlock extends SpeedEntityBlock{
    public RecodeBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RecodeBlockEntity(pPos, pState);
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if(!pLevel.isClientSide()){
            Block block = pState.getBlock();
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RecodeBlockEntity recodeBlockEntity) {
                ServerLevel serverLevel = (ServerLevel) pLevel;
                SpeedData speedData = SpeedData.get(serverLevel.getServer());
                speedData.everyTime.remove("");
                recodeBlockEntity.everyTime = speedData.everyTime;

                Channel.sendToClients(new RecodeMessage(speedData.everyTime,pPos));
            }
        }

        if (pLevel.isClientSide()) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> function(pLevel,pPos));
/*            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RecodeBlockEntity recodeBlockEntity) {
                RecodeScreen recodeScreen = new RecodeScreen(Component.translatable("mcspeed.recode_screen"),recodeBlockEntity);
                Minecraft.getInstance().setScreen(recodeScreen);

            }*/
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    @OnlyIn(Dist.CLIENT)
    public void function(Level pLevel,BlockPos pPos){
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof RecodeBlockEntity recodeBlockEntity) {
            RecodeScreen recodeScreen = new RecodeScreen(Component.translatable("mcspeed.recode_screen"),recodeBlockEntity);
            Minecraft.getInstance().setScreen(recodeScreen);

        }
    }
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
