package com.strangesmell.mcspeed;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MCSoundEventRegister {



    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MCSpeed.MODID);
    public static final RegistryObject<SoundEvent> PIAOYI = register("piaoyi");
    public static final RegistryObject<SoundEvent> XIAOPEN = register("xiaopen");
    public static final RegistryObject<SoundEvent> DAPEN = register("dapen");
    public static final RegistryObject<SoundEvent> PENGZHUANG = register("pengzhuang");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MCSpeed.MODID, name)));
    }


}
