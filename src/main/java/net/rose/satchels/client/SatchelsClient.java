package net.rose.satchels.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;
import net.rose.satchels.client.tooltip.SatchelTooltipComponent;
import net.rose.satchels.common.data_component.SatchelContentsComponent;

public class SatchelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register(tooltipData -> {
            if (tooltipData instanceof SatchelContentsComponent data) {
                return new SatchelTooltipComponent(data);
            }

            return null;
        });
    }
}
