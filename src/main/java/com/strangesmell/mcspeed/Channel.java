package com.strangesmell.mcspeed;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.strangesmell.mcspeed.MCSpeed.MODID;

public class Channel {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }


    public static void register() {
    SimpleChannel net = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    INSTANCE =net;

        net.messageBuilder(C2SMassage.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SMassage::new)
                .encoder(C2SMassage::toBytes)
                .consumerMainThread(C2SMassage::handle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }


    public static <MSG> void sendToChunk(MSG message , LevelChunk levelChunk) {
       INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(()->levelChunk), message);
    }

    public static <MSG> void sendToNear(MSG message ) {
        INSTANCE.send(PacketDistributor.NEAR.noArg(), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }


}

