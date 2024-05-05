package com.strangesmell.mcspeed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.logging.Level;

import static com.strangesmell.mcspeed.KeyRegister.*;

@Mod(MCSpeed.MODID)
public class MCSpeed
{
    public static final String MODID = "mcspeed";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SpeedBoatItem = ITEMS.register("speed_boat_item",()->new SpeedBoatItem(new Item.Properties().stacksTo(1)));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> NO_GUI_TAB = CREATIVE_MODE_TABS.register("no_gui_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.OAK_BOAT::getDefaultInstance)
            .title( Component.translatable("MCSpeed"))
            .displayItems((parameters, output) -> {
                output.accept(SpeedBoatItem.get());
            }).build());

    public static final DeferredRegister<EntityType<?>> SPEEDBOAT = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final RegistryObject<EntityType<SpeedBoat>> SpeedBoat = SPEEDBOAT.register("speed_boat",()->EntityType.Builder.<SpeedBoat>of(SpeedBoat::new,
                    MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(new ResourceLocation(MODID, "speed_boat").toString())
    );
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    public static final RegistryObject<SoundEvent> PIAOYI = register("piaoyi");
    public static final RegistryObject<SoundEvent> XIAOPEN = register("xiaopen");
    public static final RegistryObject<SoundEvent> DAPEN = register("dapen");
    public static final RegistryObject<SoundEvent> PENGZHUANG = register("pengzhuang");
    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, name)));
    }
    public MCSpeed()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUNDS.register(modEventBus);
        SPEEDBOAT.register(modEventBus);
        Channel.register();
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::clientSetup);
            modEventBus.addListener(this::key);

            forgeEventBus.addListener(this::onClientTick);
            forgeEventBus.addListener(this::renderBack);
        }



    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(SpeedBoat.get(), SpeedBoatRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    private void key(RegisterKeyMappingsEvent event) {
        event.register(DAPEN_MAPPING.get());
        event.register(XIAOPEN_MAPPING.get());
        event.register(PIAOYI_MAPPING.get());
        event.register(UP_MAPPING.get());
        event.register(DOWN_MAPPING.get());
        event.register(LEAFT_MAPPING.get());
        event.register(RIGHT_MAPPING.get());
    }
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player.getControlledVehicle() instanceof SpeedBoat speedBoat)) {
            return;
        }
        Channel.sendToServer(new C2SMassage(PIAOYI_MAPPING.get().isDown(),DAPEN_MAPPING.get().isDown(),XIAOPEN_MAPPING.get().isDown(),UP_MAPPING.get().isDown(),DOWN_MAPPING.get().isDown(),LEAFT_MAPPING.get().isDown(),RIGHT_MAPPING.get().isDown()));

        if((speedBoat.getPiaoyi()==0 || speedBoat.getPiaoyi()==1 )&& KeyRegister.PIAOYI_MAPPING.get().isDown()){
            if(speedBoat.level().isClientSide){
                EntityBoundSoundInstance entityBoundSoundInstance = new EntityBoundSoundInstance(PIAOYI.get(), SoundSource.PLAYERS,1,1,speedBoat,1);
                Minecraft.getInstance().getSoundManager().play(entityBoundSoundInstance);
            }

        }
        //放气
        if(speedBoat.getN2O()>0&& speedBoat.getDapenTime()<20 && DAPEN_MAPPING.get().isDown()){
            if( speedBoat.level().isClientSide){
                EntityBoundSoundInstance daPen = new EntityBoundSoundInstance(DAPEN.get(), SoundSource.PLAYERS,1,1,speedBoat,1);
                Minecraft.getInstance().getSoundManager().play(daPen);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderBack(CustomizeGuiOverlayEvent event) {
        if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player.getControlledVehicle() instanceof SpeedBoat)) {
            return;
        }
        HUD hud = new HUD(Minecraft.getInstance() );
        hud.render2(event.getGuiGraphics(),event.getPartialTick());
        hud.renderExperience2(event.getGuiGraphics());
    }
}
