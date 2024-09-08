package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.MCSpeed;
import com.strangesmell.mcspeed.SpeedBoat;
import com.strangesmell.mcspeed.gui.EndScreen;
import com.strangesmell.mcspeed.gui.StartScreen;
import com.strangesmell.mcspeed.savedate.SpeedData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
import org.jetbrains.annotations.Nullable;

public class EndBlock extends SpeedEntityBlock {
    public EndBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }

    public int clockTime = 0;
    public String clockName = "old";

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof SpeedBoat speedBoat) {
            if (speedBoat.getControllingPassenger() == null) return;

            EndBlockEntity endBlockEntity = (EndBlockEntity) level.getBlockEntity(pos);
            clockName = endBlockEntity.clockName;

            speedBoat.setIsClock(false);
            clockTime = speedBoat.getClock();

            if (!level.isClientSide) {
                ServerLevel serverLevel = (ServerLevel) level;
                SpeedData speedData = SpeedData.get(serverLevel.getServer());
                if (speedData.everyTime.get(clockName) != null) {
                    //Integer.valueOf(speedData.everyTime.get(clockName).get(1))
                    if (Integer.parseInt(speedData.everyTime.get(clockName)[0]) > speedBoat.getClock() || Integer.parseInt(speedData.everyTime.get(clockName)[0]) == 0) {
                        speedData.put(clockName, speedBoat.getClock(), speedBoat.getControllingPassenger().getName().getString());
                        speedData.setDirty();
                    }
                } else {
                    speedData.put(clockName, 0, speedBoat.getControllingPassenger().getName().getString());
                    speedData.setDirty();
                }
            }
            if (speedBoat.getSelfClock() > clockTime || speedBoat.getSelfClock() == 0) {

                CompoundTag tag =( (CompoundTag)speedBoat.getControllingPassenger().getPersistentData().get(MCSpeed.MODID+"recode"));
                if(tag==null){
                    tag = new CompoundTag();
                }
                tag.putInt(clockName,clockTime);
                speedBoat.getControllingPassenger().getPersistentData().put(MCSpeed.MODID+"recode",tag);

                speedBoat.setSelfClock(clockTime);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EndBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> function(pLevel,pPos));

/*            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof EndBlockEntity endBlockEntity) {
                EndScreen endScreen = new EndScreen(Component.translatable("mcspeed.end_screen"), endBlockEntity);
                Minecraft.getInstance().setScreen(endScreen);
            }*/
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @OnlyIn(Dist.CLIENT)
    public void function(Level pLevel,BlockPos pPos){
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof EndBlockEntity endBlockEntity) {
            EndScreen endScreen = new EndScreen(Component.translatable("mcspeed.end_screen"), endBlockEntity);
            Minecraft.getInstance().setScreen(endScreen);
        }
    }
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
