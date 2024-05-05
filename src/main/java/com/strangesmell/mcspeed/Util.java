package com.strangesmell.mcspeed;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public class Util {
    public static int AN2OTime = 80;

    public static void sendParticle(ServerLevel serverLevel,SpeedBoat speedBoat){

        serverLevel.sendParticles(ParticleTypes.SMOKE,speedBoat.getX()-0.3 + (double) speedBoat.getRandom().nextFloat()/2, speedBoat.getY() , speedBoat.getZ()-0.3 +  (double) speedBoat.getRandom().nextFloat()/2,2,0,0,0,0);
        serverLevel.sendParticles(ParticleTypes.SMOKE,speedBoat.getX()+0.3 + (double) speedBoat.getRandom().nextFloat()/2, speedBoat.getY() , speedBoat.getZ()+0.3 +  (double) speedBoat.getRandom().nextFloat()/2,2,0,0,0,0);

    }
    public static void sendParticle2(ServerLevel serverLevel,SpeedBoat speedBoat){
        serverLevel.sendParticles(ParticleTypes.BUBBLE,speedBoat.getX()-0.3 + (double) speedBoat.getRandom().nextFloat()/2, speedBoat.getY() +0.25, speedBoat.getZ()-0.3 +  (double) speedBoat.getRandom().nextFloat()/10,2,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat()/4,speedBoat.getRandom().nextFloat());
        serverLevel.sendParticles(ParticleTypes.BUBBLE,speedBoat.getX()+0.3 + (double) speedBoat.getRandom().nextFloat()/10, speedBoat.getY()+0.25 , speedBoat.getZ()+0.3 +  (double) speedBoat.getRandom().nextFloat()/10,2,0,0,0,0);

    }
    public static void sendParticle3(ServerLevel serverLevel,SpeedBoat speedBoat){
        serverLevel.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR,speedBoat.getX()-0.3 + (double) speedBoat.getRandom().nextFloat()/10, speedBoat.getY() +0.5, speedBoat.getZ()-0.3 +  (double) speedBoat.getRandom().nextFloat()/2,2,0,0,0,0);
        serverLevel.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR,speedBoat.getX()+0.3 + (double) speedBoat.getRandom().nextFloat()/10, speedBoat.getY()+0.5 , speedBoat.getZ()+0.3 +  (double) speedBoat.getRandom().nextFloat()/2,2,0,0,0,0);

    }
}
