package gg.norisk.addon.popup.ui;

import gg.norisk.addon.popup.sync.LauncherThemeSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

/**
 * Moderner, flacher Button im exakten Stil des NoRisk Launchers (Button.tsx).
 * Erbt direkt von AbstractWidget, um jegliche Minecraft-Pixel-Sprites zu vermeiden.
 */
public class LauncherButton extends AbstractWidget {

    // minecraft:uniform = Vektorfont (sauber, kein Bitmap-Pixelblock!)
    public static final FontDescription MODERN_FONT =
            new FontDescription.Resource(Identifier.fromNamespaceAndPath("minecraft", "uniform"));

    public enum ButtonStyle {
        PRIMARY,   // Akzentfarbener Haupt-Button (z.B. Zum Download)
        SECONDARY, // Gedimmter Transluzenter Button (z.B. Nicht erneut anzeigen)
        NEUTRAL,   // Subtiler Rahmen-Button (z.B. Okay)
        CLOSE_ICON // '✕' Icon oben rechts im Header
    }

    private final ButtonStyle style;
    private final Consumer<LauncherButton> onPress;

    public LauncherButton(int x, int y, int width, int height, Component message, ButtonStyle style, Consumer<LauncherButton> onPress) {
        super(x, y, width, height, message);
        this.style = style;
        this.onPress = onPress;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean bl) {
        if (this.active && this.visible && this.onPress != null) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress.accept(this);
        }
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (!this.visible) return;

        boolean hovered = this.isHoveredOrFocused();
        int accent = LauncherThemeSync.getAccentColor();

        int bgColor;
        int borderColor;
        int textColor;

        switch (this.style) {
            case PRIMARY -> {
                int base = accent;
                if (!this.active) {
                    bgColor = 0xFF2A2D35;
                    borderColor = 0xFF3B404D;
                    textColor = 0xFF71717A;
                } else if (hovered) {
                    bgColor = 0xFF000000 | LauncherThemeSync.lighten(base, 0.12f);
                    borderColor = 0xFF000000 | LauncherThemeSync.lighten(base, 0.28f);
                    textColor = 0xFFFFFFFF;
                } else {
                    bgColor = 0xFF000000 | base;
                    borderColor = 0xFF000000 | LauncherThemeSync.lighten(base, 0.15f);
                    textColor = 0xFFFFFFFF;
                }
            }
            case SECONDARY -> {
                if (hovered) {
                    bgColor = 0x662C3446;
                    borderColor = 0x88FFFFFF;
                    textColor = 0xFFFFFFFF;
                } else {
                    bgColor = 0x331C212E;
                    borderColor = 0x33FFFFFF;
                    textColor = 0xFFD1D5DB;
                }
            }
            case CLOSE_ICON -> {
                if (hovered) {
                    bgColor = 0x55EF4444;
                    borderColor = 0x99EF4444;
                    textColor = 0xFFFFFFFF;
                } else {
                    bgColor = 0x22FFFFFF;
                    borderColor = 0x33FFFFFF;
                    textColor = 0xFF9CA3AF;
                }
            }
            default -> { // NEUTRAL
                if (hovered) {
                    bgColor = 0x44FFFFFF;
                    borderColor = 0x88FFFFFF;
                    textColor = 0xFFFFFFFF;
                } else {
                    bgColor = 0x22FFFFFF;
                    borderColor = 0x44FFFFFF;
                    textColor = 0xFFE5E7EB;
                }
            }
        }

        // 1. Button Oberfläche zeichnen (keine Minecraft-Pixel!)
        boolean isPrimary = (this.style == ButtonStyle.PRIMARY && this.active);
        int bottomShadow = 0xFF000000 | LauncherThemeSync.darken(accent, 0.25f);
        RenderUtils.drawSleekButton(graphics, this.getX(), this.getY(), this.width, this.height,
                bgColor, borderColor, isPrimary, bottomShadow);

        // 2. Text mit modernem Vector-Font (Uniform) rendern
        Minecraft mc = Minecraft.getInstance();
        Component styledText = this.getMessage().copy().withStyle(s -> s.withFont(MODERN_FONT));

        int textY = this.getY() + (this.height - mc.font.lineHeight) / 2;
        graphics.centeredText(mc.font, styledText, this.getX() + this.width / 2, textY, textColor);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}

