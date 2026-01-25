package net.rose.satchels.client.item_model;

import com.mojang.serialization.MapCodec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

import net.rose.satchels.common.Satchels;
import net.rose.satchels.common.data_component.SatchelContentsDataComponent;
import net.rose.satchels.common.item.SatchelItem;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SatchelHasSelectedItemProperty() implements BooleanProperty {
    public static final MapCodec<SatchelHasSelectedItemProperty> CODEC = MapCodec.unit(new SatchelHasSelectedItemProperty());

    @Override
    public boolean test(ItemStack itemStack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        SatchelContentsDataComponent component = SatchelItem.getSatchelDataComponent(itemStack);

        if (component != null && component.selectedSlotIndex() >= 0 && component.selectedSlotIndex() < component.stacks().size()) {
            return !itemStack.isEmpty();
        }

        return false;
    }

    public MapCodec<SatchelHasSelectedItemProperty> getCodec() {
        return CODEC;
    }
}