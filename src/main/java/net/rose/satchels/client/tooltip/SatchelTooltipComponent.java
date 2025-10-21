package net.rose.satchels.client.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.rose.satchels.common.data_component.SatchelContentsComponent;

import java.util.List;
import java.util.Optional;

public record SatchelTooltipComponent(SatchelContentsComponent data) implements TooltipComponent {
    private static final Identifier PROGRESS_BAR_BORDER_TEXTURE = Identifier.ofVanilla("container/bundle/bundle_progressbar_border");
    private static final Identifier PROGRESS_BAR_FILL_TEXTURE = Identifier.ofVanilla("container/bundle/bundle_progressbar_fill");
    private static final Identifier PROGRESS_BAR_FULL_TEXTURE = Identifier.ofVanilla("container/bundle/bundle_progressbar_full");
    private static final Identifier BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = Identifier.ofVanilla("container/bundle/slot_highlight_back");
    private static final Identifier BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = Identifier.ofVanilla("container/bundle/slot_highlight_front");
    private static final Identifier BUNDLE_SLOT_BACKGROUND_TEXTURE = Identifier.ofVanilla("container/bundle/slot_background");
    private static final Text FULL_TEXT = Text.translatable("item.minecraft.bundle.full");
    private static final Text EMPTY_TEXT = Text.translatable("item.minecraft.bundle.empty");
    private static final Text DESCRIPTION_TEXT = Text.translatable("item.satchels.satchel.desc").formatted(Formatting.GRAY);
    private static final int FILL_BAR_HEIGHT = 13;
    private static final int FILL_BAR_WIDTH = 96;
    private static final int MAX_WIDTH = FILL_BAR_WIDTH;

    private static int getDescriptionHeight(TextRenderer textRenderer) {
        return textRenderer.getWrappedLinesHeight(DESCRIPTION_TEXT, MAX_WIDTH);
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return getDescriptionHeight(textRenderer) + FILL_BAR_HEIGHT + 8 + (!this.data.stacks().isEmpty() ? 24 : 0);
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return MAX_WIDTH;
    }

    @Override
    public boolean isSticky() {
        return true;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext drawContext) {
        drawContext.drawWrappedText(textRenderer, DESCRIPTION_TEXT, x, y, MAX_WIDTH, 0xFFFFFFFF, true);
        final var descriptionHeight = getDescriptionHeight(textRenderer);
        y += descriptionHeight;

        if (!this.data.stacks().isEmpty()) {
            var seed = 1;
            for (var i = 0; i < this.data.stacks().size(); i++) {
                this.drawItem(seed, x - 2 + i * 24, y, this.data.stacks(), seed, textRenderer, drawContext);
                seed++;
            }
            y += 24;
        }

        y += 2;

        this.drawProgressBar(x, y, textRenderer, drawContext);
    }

    private void drawItem(int index, int x, int y, List<ItemStack> stacks, int seed, TextRenderer textRenderer, DrawContext drawContext) {
        final var slotIndex = stacks.size() - index;
        final var isSlotSelected = slotIndex == SatchelContentsComponent.selectedSlotIndex;
        final var itemStack = stacks.get(slotIndex);

        final var slotTexture = isSlotSelected ? BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE : BUNDLE_SLOT_BACKGROUND_TEXTURE;
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, slotTexture, x, y, 24, 24);

        drawContext.drawItem(itemStack, x + 4, y + 4, seed);
        drawContext.drawStackOverlay(textRenderer, itemStack, x + 4, y + 4);

        if (isSlotSelected) {
            drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 24, 24);
        }
    }

    private void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext drawContext) {
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.getProgressBarFillTexture(), x + 1, y, this.getProgressBarFill(), FILL_BAR_HEIGHT);
        drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR_BORDER_TEXTURE, x, y, FILL_BAR_WIDTH, FILL_BAR_HEIGHT);

        this.getProgressBarLabel().ifPresent(text ->
                drawContext.drawCenteredTextWithShadow(textRenderer, text, x + FILL_BAR_WIDTH / 2, y + 3, Colors.WHITE)
        );
    }

    private int getProgressBarFill() {
        return (int) MathHelper.clamp(this.data.getOccupancy() * (FILL_BAR_WIDTH - 2), 0, FILL_BAR_WIDTH - 2);
    }

    private Identifier getProgressBarFillTexture() {
        return this.data.getOccupancy() >= 1 ? PROGRESS_BAR_FULL_TEXTURE : PROGRESS_BAR_FILL_TEXTURE;
    }

    private Optional<Text> getProgressBarLabel() {
        if (this.data.stacks().isEmpty()) {
            return Optional.of(EMPTY_TEXT);
        }

        return Optional.ofNullable(this.data.getOccupancy() >= 1 ? FULL_TEXT : null);
    }
}
