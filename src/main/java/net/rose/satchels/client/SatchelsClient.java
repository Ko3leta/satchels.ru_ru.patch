package net.rose.satchels.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import net.rose.satchels.client.tooltip.SatchelTooltipComponent;
import net.rose.satchels.common.data_component.SatchelContentsComponent;

public class SatchelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register(tooltipData -> {
            if (tooltipData instanceof SatchelContentsComponent data) return new SatchelTooltipComponent(data);
            return null;
        });
    }
}
