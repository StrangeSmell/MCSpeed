package com.strangesmell.mcspeed.savedate;

import com.strangesmell.mcspeed.MCSpeed;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class SpeedData extends SavedData {
    public HashMap<String,String[]> everyTime = new HashMap<String, String[]>();

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putInt(MCSpeed.MODID+"recode_size",everyTime.size());
        Object[] key = everyTime.keySet().toArray();
        for(int i = 0;i<everyTime.size();i++){
            CompoundTag strings = new CompoundTag();
            strings.putString("clockName",key[i].toString());
            strings.putString("clockTime",everyTime.get(key[i])[0]);
            strings.putString("playerName",everyTime.get(key[i])[1]);
            compoundTag.put(MCSpeed.MODID+"recode_index"+i, strings);
        }
        return compoundTag;
    }

    public void put(String clockName,int clockTime,String playerName) {
        everyTime.put(clockName, new String[]{String.valueOf(clockTime), playerName});
        this.setDirty();
    }

    public void putTag(CompoundTag compoundTag) {
        everyTime.put(compoundTag.getString("clockName"), new String[]{compoundTag.getString("clockTime"), compoundTag.getString("playerName")});
    }

    public static SpeedData load(CompoundTag tag) {
        SpeedData speedData = new SpeedData();
        int size = tag.getInt(MCSpeed.MODID+"recode_size");
        for(int i = 0;i<size;i++){
            speedData.putTag(tag.getCompound(MCSpeed.MODID+"recode_index"+i));
        }
        return speedData;
    }

    public static SpeedData get(MinecraftServer minecraftServer) {
        ServerLevel serverLevel = minecraftServer.overworld();
        DimensionDataStorage storage =serverLevel.getDataStorage();
        return storage.computeIfAbsent(SpeedData::load,SpeedData::new,"speed_time");
    }


}
