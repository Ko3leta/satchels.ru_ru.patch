package net.rose.satchels.client.tooltip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;
import net.minecraft.client.input.Scroller;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.rose.satchels.common.data_component.SatchelContentsComponent;
import net.rose.satchels.common.init.ModItemTags;
import net.rose.satchels.common.item.SatchelItem;

public class SatchelTooltipSubmenuHandler implements TooltipSubmenuHandler {
    private final MinecraftClient client;
    private final Scroller scroller;

    public SatchelTooltipSubmenuHandler(MinecraftClient client) {
        this.client = client;
        this.scroller = new Scroller();
    }

    @Override
    public boolean isApplicableTo(Slot slot) {
        return slot.getStack().isIn(ModItemTags.SATCHELS);
    }

    public boolean onScroll(double horizontal, double vertical, int slotId, ItemStack itemStack) {
        final var itemStackCount = SatchelItem.getItemStackCount(itemStack);
        if (itemStackCount == 0) {
            return false;
        }

        final var scrollDelta = this.scroller.update(horizontal, vertical);
        final var scrollAmount = scrollDelta.y == 0 ? -scrollDelta.x : scrollDelta.y;
        if (scrollAmount != 0) {
            final var currentSelectedSlot = SatchelItem.getSelectedStackIndex(itemStack);
            final var cycledSlotIndex = Scroller.scrollCycling(-scrollAmount, SatchelContentsComponent.CURRENT_SELECTED_SLOT_INDEX, itemStackCount);
            if (SatchelContentsComponent.CURRENT_SELECTED_SLOT_INDEX != cycledSlotIndex) {
                SatchelContentsComponent.CURRENT_SELECTED_SLOT_INDEX = cycledSlotIndex;
                // this.sendPacket(itemStack, slotId, cycledSlotIndex);
            }
        }

        return true;
    }

    public void reset(Slot slot) {
        this.reset(slot.getStack(), slot.id);
    }

    public void reset(ItemStack item, int slotId) {
        this.sendPacket(item, slotId, -1);
    }

    private void sendPacket(ItemStack itemStack, int slotId, int selectedItemIndex) {
       // if (this.client.getNetworkHandler() != null && selectedItemIndex < SatchelItem.getItemStackCount(itemStack)) {
       //     SatchelItem.setSelectedStackIndex(itemStack, selectedItemIndex);

       //     if (this.client.player != null) {
       //         // SatchelItem.onContentChanged(this.client.player);
       //     }

       //     // ClientPlayNetworking.send(new SatchelItemSelectedC2SPayload(slotId, selectedItemIndex));
       // }
    }

    public void onMouseClick(Slot slot, SlotActionType actionType) {
        if (actionType == SlotActionType.QUICK_MOVE || actionType == SlotActionType.SWAP) {
            this.reset(slot.getStack(), slot.id);
        }
    }
}
