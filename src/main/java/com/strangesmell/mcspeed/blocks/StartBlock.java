package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.MCSpeed;
import com.strangesmell.mcspeed.SpeedBoat;
import com.strangesmell.mcspeed.gui.StartScreen;
import com.strangesmell.mcspeed.savedate.SpeedData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;


public class StartBlock extends SpeedEntityBlock {
    public StartBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }

    public int clockTime = 1;
    public int laps = 0;
    public String clockName = "";

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof SpeedBoat speedBoat) {

            if (!level.isClientSide) {
                if (speedBoat.getControllingPassenger() == null) return;

                StartBlockEntity startBlockEntity = (StartBlockEntity) level.getBlockEntity(pos);
                clockName = startBlockEntity.clockName;
                laps = startBlockEntity.clockTime;
                speedBoat.setIsClock(true);
                speedBoat.setClock(0);
                speedBoat.setClockName(clockName);

                speedBoat.setSelfClock(((CompoundTag)speedBoat.getControllingPassenger().getPersistentData().get(MCSpeed.MODID + "recode")).getInt(clockName));

                ServerLevel serverLevel = (ServerLevel) level;
                SpeedData speedData = SpeedData.get(serverLevel.getServer());

                if (speedData.everyTime.get(clockName) == null) {
                    speedData.put(clockName, 0, speedBoat.getControllingPassenger().getName().getString());
                    speedData.setDirty();
                }

                speedBoat.setClockBestName(speedData.everyTime.get(clockName)[1]);
                if (speedData.everyTime.get(clockName) != null) {

                    clockTime = Integer.parseInt(speedData.everyTime.get(clockName)[0]);
                } else {
                    speedData.put(clockName, 0, speedBoat.getControllingPassenger().getName().getString());
                    clockTime = 0;
                }
                speedBoat.setClockInt(clockTime);

            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StartBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {


        if (pLevel.isClientSide()) {

            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> function(pLevel,pPos));

/*            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof StartBlockEntity startBlockEntity) {
                StartScreen startScreen = new StartScreen(Component.translatable("mcspeed.start_screen"), startBlockEntity);
                Minecraft.getInstance().setScreen(startScreen);
            }*/
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
    @OnlyIn(Dist.CLIENT)
    public void function(Level pLevel,BlockPos pPos){
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof StartBlockEntity startBlockEntity) {
            StartScreen startScreen = new StartScreen(Component.translatable("mcspeed.start_screen"), startBlockEntity);
            Minecraft.getInstance().setScreen(startScreen);
        }
    }
}
