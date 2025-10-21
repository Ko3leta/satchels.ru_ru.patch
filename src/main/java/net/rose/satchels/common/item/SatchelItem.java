package net.rose.satchels.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;

import net.minecraft.util.Formatting;
import net.rose.satchels.common.data_component.SatchelContentsComponent;
import net.rose.satchels.common.init.ModDataComponents;

import java.util.Optional;

public class SatchelItem extends Item {
    public SatchelItem(Settings settings) {
        super(settings);
    }

    public static int getItemStackCount(ItemStack itemStack) {
        final var maybeSatchelComponent = tryGetSatchelComponent(itemStack);
        if (maybeSatchelComponent.isPresent()) {
            final var satchelComponent = maybeSatchelComponent.get();
            return satchelComponent.stacks.size();
        }

        return 0;
    }

    public static void setSelectedStackIndex(ItemStack itemStack, int selectedStackIndex) {
        final var maybeSatchelComponent = tryGetSatchelComponent(itemStack);
        if (maybeSatchelComponent.isPresent()) {
            final var satchelComponent = maybeSatchelComponent.get();
            final var builder = new SatchelContentsComponent.Builder(satchelComponent);
            builder.setSelectedStackIndex(selectedStackIndex);
            itemStack.set(ModDataComponents.SATCHEL_CONTENTS, builder.build());
        }
    }

    public static int getSelectedStackIndex(ItemStack itemStack) {
        final var maybeSatchelComponent = tryGetSatchelComponent(itemStack);
        if (maybeSatchelComponent.isPresent()) {
            final var satchelComponent = maybeSatchelComponent.get();
            return satchelComponent.selectedStackIndex;
        }

        return 0;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.ofNullable(stack.contains(ModDataComponents.SATCHEL_CONTENTS)
                ? stack.get(ModDataComponents.SATCHEL_CONTENTS)
                : null
        );
    }

    private static Optional<SatchelContentsComponent> tryGetSatchelComponent(ItemStack itemStack) {
        if (itemStack.contains(ModDataComponents.SATCHEL_CONTENTS)) {
            return Optional.ofNullable(itemStack.get(ModDataComponents.SATCHEL_CONTENTS));
        }

        return Optional.empty();
    }

    @Override
    public boolean onStackClicked(ItemStack satchelItemStack, Slot slot, ClickType clickType, PlayerEntity user) {
        final var maybeSatchelComponent = tryGetSatchelComponent(satchelItemStack);
        if (maybeSatchelComponent.isPresent()) {
            final var satchelComponent = maybeSatchelComponent.get();
            final var itemStackInSlot = slot.getStack();

            if (clickType == ClickType.LEFT) {
                if (itemStackInSlot.isEmpty()) {
                    return false;
                }

                final var builder = new SatchelContentsComponent.Builder(satchelComponent);
                if (builder.add(itemStackInSlot.copyWithCount(1))) {
                    itemStackInSlot.decrement(1);
                    satchelItemStack.set(ModDataComponents.SATCHEL_CONTENTS, builder.build());
                    onContentChanged(user);
                    return true;
                }

                return false;
            }

            if (slot.getStack().isEmpty()) {
                final var builder = new SatchelContentsComponent.Builder(satchelComponent);
                final var removed = builder.removeCurrent();
                if (removed.isPresent()) {
                    slot.setStack(removed.get().copy());
                    satchelItemStack.set(ModDataComponents.SATCHEL_CONTENTS, builder.build());
                    onContentChanged(user);
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    @Override
    public boolean onClicked(ItemStack satchelItemStack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity user, StackReference cursorStackReference) {
        final var maybeSatchelComponent = tryGetSatchelComponent(satchelItemStack);
        if (maybeSatchelComponent.isPresent()) {
            final var satchelComponent = maybeSatchelComponent.get();
            final var itemStackInCursor = cursorStackReference.get();

            if (!itemStackInCursor.isEmpty()) {
                final var builder = new SatchelContentsComponent.Builder(satchelComponent);
                if (builder.add(itemStackInCursor.copyWithCount(1))) {
                    itemStackInCursor.decrement(1);
                    satchelItemStack.set(ModDataComponents.SATCHEL_CONTENTS, builder.build());
                    onContentChanged(user);
                    return true;
                }

                return false;
            }

            if (clickType == ClickType.RIGHT) {
                final var builder = new SatchelContentsComponent.Builder(satchelComponent);
                user.sendMessage(Text.literal("Selected: " + satchelComponent.selectedStackIndex), false);
                for (var i = 0; i < satchelComponent.stacks.size(); i++) {
                    final var stack = satchelComponent.stacks.get(i);
                    user.sendMessage(Text.literal(" - " + stack.getName().getString()).formatted(i == satchelComponent.selectedStackIndex ? Formatting.YELLOW : Formatting.WHITE), false);
                }
                final var removed = builder.removeCurrent();
                if (removed.isPresent()) {
                    cursorStackReference.set(removed.get().copy());
                    satchelItemStack.set(ModDataComponents.SATCHEL_CONTENTS, builder.build());
                    onContentChanged(user);
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    public static void onContentChanged(PlayerEntity user) {
        final var screenHandler = user.currentScreenHandler;
        if (screenHandler != null) screenHandler.onContentChanged(user.getInventory());
    }
}
