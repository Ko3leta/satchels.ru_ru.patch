package net.rose.satchels.common.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.rose.satchels.common.packets.c2s.SatchelItemSelectedC2SPayload;

public class ModPackets {
    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(SatchelItemSelectedC2SPayload.CUSTOM_ID, SatchelItemSelectedC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SatchelItemSelectedC2SPayload.CUSTOM_ID, SatchelItemSelectedC2SPayload::receive);
    }
}
