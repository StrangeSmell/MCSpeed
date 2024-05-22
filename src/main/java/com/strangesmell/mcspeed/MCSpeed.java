package com.strangesmell.mcspeed;

import com.mojang.datafixers.DSL;
import com.strangesmell.mcspeed.Massage.C2SMassage;
import com.strangesmell.mcspeed.Massage.Channel;
import com.strangesmell.mcspeed.blocks.*;
import com.strangesmell.mcspeed.gui.EndMenu;
import com.strangesmell.mcspeed.gui.RecodeMenu;
import com.strangesmell.mcspeed.gui.StartMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import static com.strangesmell.mcspeed.KeyRegister.*;
import static net.minecraftforge.network.NetworkEvent.RegistrationChangeType.REGISTER;

@Mod(MCSpeed.MODID)
public class MCSpeed
{
    public static final String MODID = "mcspeed";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> N2OBlock = BLOCKS.register("n2o_block", N2OBlock::new);
    public static final RegistryObject<Block> DownBlock = BLOCKS.register("down_block", DownBlock::new);
    public static final RegistryObject<Block> StartBlock = BLOCKS.register("start_block", StartBlock::new);
    public static final RegistryObject<Block> EndBlock = BLOCKS.register("end_block", EndBlock::new);
    public static final RegistryObject<Block> RecodeBlock = BLOCKS.register("recode_block", RecodeBlock::new);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final RegistryObject<BlockEntityType<StartBlockEntity>> StartBlockEntity = BLOCK_ENTITIES.register("start_block_entity", () -> BlockEntityType.Builder.of(StartBlockEntity::new, StartBlock.get()).build(DSL.remainderType()));
    public static final RegistryObject<BlockEntityType<EndBlockEntity>> EndBlockEntity = BLOCK_ENTITIES.register("end_block_entity", () -> BlockEntityType.Builder.of(EndBlockEntity::new, StartBlock.get()).build(DSL.remainderType()));
    public static final RegistryObject<BlockEntityType<RecodeBlockEntity>> RecodeBlockEntity = BLOCK_ENTITIES.register("recode_block_entity", () -> BlockEntityType.Builder.of(RecodeBlockEntity::new, RecodeBlock.get()).build(DSL.remainderType()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SpeedBoatItem = ITEMS.register("speed_boat_item",()->new SpeedBoatItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> N2OIBlocktem = ITEMS.register("n2o_block_item",()->new BlockItem(N2OBlock.get(),new Item.Properties()));
    public static final RegistryObject<Item> DownBlockItem = ITEMS.register("down_block_item",()->new BlockItem(DownBlock.get(),new Item.Properties()));
    public static final RegistryObject<Item> StartBlockItem = ITEMS.register("start_block_item",()->new BlockItem(StartBlock.get(),new Item.Properties()));
    public static final RegistryObject<Item> EndBlockItem = ITEMS.register("end_block_item",()->new BlockItem(EndBlock.get(),new Item.Properties()));
    public static final RegistryObject<Item> RecodeBlockItem = ITEMS.register("recode_block_item",()->new RecodeBlockItem(RecodeBlock.get(),new Item.Properties()));

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    //public static final RegistryObject<MenuType<StartMenu>> START_MENU = MENUS.register("start_menu", () -> new MenuType(StartMenu::new, FeatureFlags.DEFAULT_FLAGS));
    //public static final RegistryObject<MenuType<EndMenu>> END_MENU = MENUS.register("end_menu", () -> new MenuType(EndMenu::new, FeatureFlags.DEFAULT_FLAGS));
    //public static final RegistryObject<MenuType<RecodeMenu>> RECODE_MENU = MENUS.register("recode_menu", () -> new MenuType(RecodeBlock::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> NO_GUI_TAB = CREATIVE_MODE_TABS.register("no_gui_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.OAK_BOAT::getDefaultInstance)
            .title( Component.translatable("MCSpeed"))
            .displayItems((parameters, output) -> {
                output.accept(SpeedBoatItem.get());
                output.accept(N2OIBlocktem.get());
                output.accept(DownBlockItem.get());
                output.accept(StartBlockItem.get());
                output.accept(EndBlockItem.get());
                output.accept(RecodeBlockItem.get());
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

    public static final PermissionNode<Boolean> USE_START_OR_END = new PermissionNode<>(MODID, "use.statr_or_end",
            PermissionTypes.BOOLEAN, (player, uuid, contexts) -> player != null && player.hasPermissions(Commands.LEVEL_GAMEMASTERS));
    public static final PermissionNode<Boolean> DELETE_RECODE = new PermissionNode<>(MODID, "deletc.recode",
            PermissionTypes.BOOLEAN, (player, uuid, contexts) -> player != null && player.hasPermissions(Commands.LEVEL_GAMEMASTERS));


    public MCSpeed()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        BLOCKS.register(modEventBus);
        //MENUS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
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
        forgeEventBus.addListener(this::registerPermissionNodes);
    }

    public void registerPermissionNodes(PermissionGatherEvent.Nodes event)
    {
        event.addNodes(USE_START_OR_END);
        event.addNodes(DELETE_RECODE);
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
        BlockPos blockpos = speedBoat.getOnPosLegacy();
        BlockState blockstate = speedBoat.level().getBlockState(blockpos);

        if( speedBoat.getDapenTime()<20 && speedBoat.getN2O()>0 && DAPEN_MAPPING.get().isDown()){
            if( speedBoat.level().isClientSide){
                EntityBoundSoundInstance daPen = new EntityBoundSoundInstance(DAPEN.get(), SoundSource.PLAYERS,1,1,speedBoat,1);
                Minecraft.getInstance().getSoundManager().play(daPen);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderBack(CustomizeGuiOverlayEvent event) {
        if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player.getControlledVehicle() instanceof SpeedBoat speedBoat)) {
            return;
        }
        HUD hud = new HUD(Minecraft.getInstance() );
        hud.render2(event.getGuiGraphics(),event.getPartialTick());
        hud.renderExperience2(event.getGuiGraphics(),speedBoat);
    }
}
