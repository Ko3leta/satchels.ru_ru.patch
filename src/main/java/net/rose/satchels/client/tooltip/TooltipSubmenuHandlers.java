package net.rose.satchels.client.tooltip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;

import java.util.function.Consumer;
import java.util.function.Function;

public class TooltipSubmenuHandlers {
    public static void addAll(Consumer<Function<MinecraftClient, TooltipSubmenuHandler>> builder) {
        builder.accept(SatchelTooltipSubmenuHandler::new);
    }
}
