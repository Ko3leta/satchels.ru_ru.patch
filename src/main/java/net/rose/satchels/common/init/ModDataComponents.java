package net.rose.satchels.common.init;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.rose.satchels.common.Satchels;
import net.rose.satchels.common.data_component.SatchelContentsComponent;

public class ModDataComponents {
    public static final ComponentType<SatchelContentsComponent> SATCHEL_CONTENTS = Registry.register(
            Registries.DATA_COMPONENT_TYPE, Satchels.identifier("satchel_content"), ComponentType
                    .<SatchelContentsComponent>builder()
                    .codec(SatchelContentsComponent.CODEC)
                    .packetCodec(SatchelContentsComponent.PACKET_CODEC)
                    .build()
    );

    public static void initialize() {
    }
}
