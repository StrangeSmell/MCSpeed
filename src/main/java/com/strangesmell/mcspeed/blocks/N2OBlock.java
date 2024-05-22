package com.strangesmell.mcspeed.blocks;

import com.strangesmell.mcspeed.MCSpeed;
import com.strangesmell.mcspeed.SpeedBoat;
import com.strangesmell.mcspeed.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class N2OBlock extends SpeedBlock {
    public N2OBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity){
        if(entity instanceof SpeedBoat speedBoat){
/*
            speedBoat.setDapenTime(Util.AN2OTime);
            speedBoat.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20,2,false,false,false));
            speedBoat.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,60,1,false,false,false));
*/


            speedBoat.setDapenTime(Util.AN2OTime);
            speedBoat.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20,2,false,false,false));
            speedBoat.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,60,1,false,false,false));


            if(!level.isClientSide){
                ServerLevel serverLevel =  (ServerLevel)level;
                serverLevel.sendParticles(ParticleTypes.BUBBLE,speedBoat.getX()-0.3 + (double) speedBoat.getRandom().nextFloat()/2, speedBoat.getY() +0.25, speedBoat.getZ()-0.3 +  (double) speedBoat.getRandom().nextFloat()/10,2,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat());
                serverLevel.sendParticles(ParticleTypes.BUBBLE,speedBoat.getX()+0.3 + (double) speedBoat.getRandom().nextFloat()/10, speedBoat.getY()+0.25 , speedBoat.getZ()+0.3 +  (double) speedBoat.getRandom().nextFloat()/10,2,0,0,0,0);
                level.playSound((Player) (speedBoat.getControllingPassenger()),speedBoat, MCSpeed.DAPEN.get(), SoundSource.PLAYERS,1,1 );

            }
        }
    }

}
