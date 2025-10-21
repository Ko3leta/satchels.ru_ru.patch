package net.rose.satchels.common.packets.c2s;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.rose.satchels.common.Satchels;
import net.rose.satchels.common.item.SatchelItem;

public record SatchelItemSelectedC2SPayload(int slotId, int selectedItemIndex) implements CustomPayload {
    public static final Identifier IDENTIFIER = Satchels.identifier("satchel_item_selected");
    public static final CustomPayload.Id<SatchelItemSelectedC2SPayload> CUSTOM_ID = new Id<>(IDENTIFIER);
    public static final PacketCodec<PacketByteBuf, SatchelItemSelectedC2SPayload> CODEC = new SatchelPacketCodec();

    @Override
    public Id<? extends CustomPayload> getId() {
        return CUSTOM_ID;
    }

    public void receive(ServerPlayNetworking.Context context) {
        final var screenHandler = context.player().currentScreenHandler;
        if (this.slotId() < 0 || this.slotId() >= screenHandler.slots.size()) {
            return;
        }

        final var itemStack = screenHandler.slots.get(this.slotId()).getStack();
        SatchelItem.setSelectedStackIndex(itemStack, this.selectedItemIndex());
    }

    public static class SatchelPacketCodec implements PacketCodec<PacketByteBuf, SatchelItemSelectedC2SPayload> {
        @Override
        public SatchelItemSelectedC2SPayload decode(PacketByteBuf buf) {
            return new SatchelItemSelectedC2SPayload(buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(PacketByteBuf buf, SatchelItemSelectedC2SPayload value) {
            buf.writeInt(value.slotId);
            buf.writeInt(value.selectedItemIndex);
        }
    }
}
