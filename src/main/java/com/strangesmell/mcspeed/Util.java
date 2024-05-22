package com.strangesmell.mcspeed;


import io.netty.handler.codec.DecoderException;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static int AN2OTime = 60;
    public static int DownBlockF = 100;

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

    public static HashMap<String,int[]> stringToInt(HashMap<String, String[]> hashMap){
        HashMap<String, int[]> intMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String[] stringArray = entry.getValue();

            int[] intArray = new int[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                intArray[i] = Integer.parseInt(stringArray[i]);
            }

            intMap.put(key, intArray);
        }
        return intMap;
    }

    public static FriendlyByteBuf writeVarStringArray(String[] strings, FriendlyByteBuf buf) {
        buf.writeVarInt(strings.length);
        for(String i : strings) {
            buf.writeUtf(i);
        }
        return buf;
    }

    public static String[] readVarStringArray(FriendlyByteBuf buf) {
        return readVarStringArray(buf.readableBytes(),buf);
    }

    public static String[] readVarStringArray(int p_130117_, FriendlyByteBuf buf) {
        int i = buf.readVarInt();
        if (i > p_130117_) {
            throw new DecoderException("VarIntArray with size " + i + " is bigger than allowed " + p_130117_);
        } else {
            String[] aint = new String[i];
            for(int j = 0; j < aint.length; ++j) {
                aint[j] = buf.readUtf();
            }
            return aint;
        }
    }
}
