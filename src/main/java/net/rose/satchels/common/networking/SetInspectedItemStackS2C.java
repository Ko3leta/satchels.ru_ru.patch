package net.rose.satchels.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.rose.satchels.common.Satchels;
import net.rose.satchels.common.item.SatchelItem;

public record SetInspectedItemStackS2C(ItemStack selectedItem) implements CustomPayload {
    public static final CustomPayload.Id<SetInspectedItemStackS2C> ID = new Id<>(Satchels.id("set_inspected_item_stack_s2c"));

    public static final PacketCodec<RegistryByteBuf, SetInspectedItemStackS2C> CODEC = PacketCodec.tuple(
            ItemStack.OPTIONAL_PACKET_CODEC, SetInspectedItemStackS2C::selectedItem,
            SetInspectedItemStackS2C::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void receive(ClientPlayNetworking.Context ctx) {
        ctx.player().sendMessage(Text.literal("Received message: " + selectedItem()), false);
    }
}
