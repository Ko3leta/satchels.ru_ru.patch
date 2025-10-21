package net.rose.satchels.common.data_component;

import com.mojang.serialization.*;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.rose.satchels.common.init.ModItemTags;

import java.util.*;

public class SatchelContentsComponent implements TooltipData {
    public static final SatchelContentsComponent DEFAULT = new SatchelContentsComponent(List.of());
    public static final int MAX_ITEM_COUNT = 3;
    public static int CURRENT_SELECTED_SLOT_INDEX;

    public static final Codec<SatchelContentsComponent> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder
            .create(instance -> instance
                    .group(
                            Codecs.rangedInt(0, MAX_ITEM_COUNT)
                                    .fieldOf("selectedStackIndex")
                                    .forGetter(SatchelContentsComponent::collectSelectedStackIndex),
                            ItemStack.CODEC.listOf()
                                    .fieldOf("stacks")
                                    .forGetter(SatchelContentsComponent::collectStacks)
                    )
                    .apply(instance, SatchelContentsComponent::new)
            ));

    public static final PacketCodec<RegistryByteBuf, SatchelContentsComponent> PACKET_CODEC = PacketCodec.of(
            SatchelContentsComponent::encodePacketCodec,
            SatchelContentsComponent::decodePacketCodec
    );

    public final List<ItemStack> stacks;
    public final int selectedStackIndex;

    private SatchelContentsComponent(Integer selectedStackIndex, List<ItemStack> itemStacks) {
        this(itemStacks, selectedStackIndex);
    }

    private int collectSelectedStackIndex() {
        return this.selectedStackIndex;
    }

    private List<ItemStack> collectStacks() {
        return this.stacks;
    }

    public SatchelContentsComponent(List<ItemStack> stacks, int selectedStackIndex) {
        this.stacks = stacks;
        this.selectedStackIndex = selectedStackIndex;
    }

    public SatchelContentsComponent(List<ItemStack> stacks) {
        this(stacks, -1);
    }

    // private static DataResult<? extends SatchelContentsComponent> validateCodec(List<ItemStack> list) {
    //     return DataResult.success(new SatchelContentsComponent(list));
    // }

    // private static DataResult<? extends List<ItemStack>> collectCodec(SatchelContentsComponent component) {
    //     return DataResult.success(component.stacks);
    // }

    private static void encodePacketCodec(SatchelContentsComponent value, RegistryByteBuf buf) {
        buf.writeInt(value.selectedStackIndex);
        ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).encode(buf, value.stacks);
    }

    private static SatchelContentsComponent decodePacketCodec(RegistryByteBuf buf) {
        final var selectedItemSlot = buf.readInt();
        final var items = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).decode(buf);
        return new SatchelContentsComponent(items, selectedItemSlot);
    }

    public String toString() {
        return "SatchelContents" + this.stacks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SatchelContentsComponent otherComponent)) {
            return false;
        }

        if (this.stacks.size() != otherComponent.stacks.size()) {
            return false;
        }

        for (var i = 0; i < this.stacks.size(); i++) {
            if (!ItemStack.areEqual(this.stacks.get(i), otherComponent.stacks.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public float getOccupancy() {
        return this.stacks.size() / (float) MAX_ITEM_COUNT;
    }

    public static final class Builder {
        private final List<ItemStack> stacks;
        private int selectedStackIndex;

        public Builder(SatchelContentsComponent baseComponent) {
            this.stacks = new ArrayList<>(baseComponent.stacks);
        }

        public boolean add(ItemStack stack) {
            if (stack.isEmpty() || this.stacks.size() >= MAX_ITEM_COUNT || stack.isIn(ModItemTags.SATCHELS)) {
                return false;
            }

            this.stacks.add(stack);
            return true;
        }

        public Optional<ItemStack> removeCurrent() {
            if (this.stacks.isEmpty()) {
                return Optional.empty();
            }

            final var index = MathHelper.clamp(CURRENT_SELECTED_SLOT_INDEX, 0, this.stacks.size() - 1);
            System.out.println("Index: " + CURRENT_SELECTED_SLOT_INDEX + " Clamped: " + index);
            final var itemStack = this.stacks.get(index).copy();
            this.stacks.remove(index);
            return Optional.of(itemStack);
        }

        public SatchelContentsComponent build() {
            return new SatchelContentsComponent(List.of(this.stacks.toArray(ItemStack[]::new)), selectedStackIndex);
        }

        public void setSelectedStackIndex(int selectedStackIndex) {
            this.selectedStackIndex = selectedStackIndex;
        }
    }
}