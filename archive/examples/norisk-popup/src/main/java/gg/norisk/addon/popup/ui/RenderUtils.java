package gg.norisk.addon.popup.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Zeichnet gestochen scharfe, moderne Oberflächen im NoRisk Launcher-Design.
 * Verwendet präzise 1px-Geometrie mit sanfter Kantenglättung ohne pixelige Treppenstufen.
 */
public final class RenderUtils {

    private RenderUtils() {}

    /**
     * Mischt zwei ARGB-Farben im Verhältnis ratio (0.0 = c1, 1.0 = c2).
     */
    public static int blend(int c1, int c2, float ratio) {
        float inv = 1.0f - ratio;
        int a = (int) (((c1 >>> 24) & 0xFF) * inv + ((c2 >>> 24) & 0xFF) * ratio);
        int r = (int) (((c1 >> 16) & 0xFF) * inv + ((c2 >> 16) & 0xFF) * ratio);
        int g = (int) (((c1 >> 8) & 0xFF) * inv + ((c2 >> 8) & 0xFF) * ratio);
        int b = (int) ((c1 & 0xFF) * inv + ((c2 & 0xFF) * ratio));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Zeichnet ein modernes, flaches Launcher-Panel (Card) mit sauberen 1px-Kanten
     * und subtiler 2px-Eckenschrägung (Anti-Aliased Bevel, keine Treppenstufen!).
     */
    public static void drawSleekPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
                                     int bgColor, int borderColor, int bottomAccentColor) {
        int x2 = x + width;
        int y2 = y + height;

        // 1. Innenfläche (Kompakt und dunkel wie der Launcher)
        graphics.fill(x + 1, y + 2, x2 - 1, y2 - 2, bgColor);
        graphics.fill(x + 2, y + 1, x2 - 2, y + 2, bgColor);
        graphics.fill(x + 2, y2 - 2, x2 - 2, y2 - 1, bgColor);

        // 2. Kanten (1px gestochen scharf)
        // Oben (1px)
        graphics.fill(x + 2, y, x2 - 2, y + 1, borderColor);
        // Links (1px)
        graphics.fill(x, y + 2, x + 1, y2 - 2, borderColor);
        // Rechts (1px)
        graphics.fill(x2 - 1, y + 2, x2, y2 - 2, borderColor);
        // Unten (2px solide 3D-Akzentkante wie im Launcher 'border-b-2')
        graphics.fill(x + 2, y2 - 2, x2 - 2, y2, bottomAccentColor);

        // 3. Glatte Eckpunkte (Subpixel Anti-Aliasing Bevel statt Treppen!)
        int bevelTop = blend(borderColor, bgColor, 0.45f);
        int bevelBottom = blend(bottomAccentColor, bgColor, 0.35f);

        // Oben-Links
        graphics.fill(x + 1, y + 1, x + 2, y + 2, bevelTop);
        // Oben-Rechts
        graphics.fill(x2 - 2, y + 1, x2 - 1, y + 2, bevelTop);
        // Unten-Links
        graphics.fill(x + 1, y2 - 2, x + 2, y2 - 1, bevelBottom);
        // Unten-Rechts
        graphics.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, bevelBottom);
    }

    /**
     * Zeichnet einen flachen, modernen Launcher-Button mit sanften Kanten.
     */
    public static void drawSleekButton(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
                                      int bgColor, int borderColor, boolean isPrimary, int bottomColor) {
        int x2 = x + width;
        int y2 = y + height;

        // Hauptfläche
        graphics.fill(x + 1, y + 1, x2 - 1, y2 - 1, bgColor);

        // Kanten (1px)
        graphics.fill(x + 2, y, x2 - 2, y + 1, borderColor); // Top
        graphics.fill(x, y + 2, x + 1, y2 - 2, borderColor); // Left
        graphics.fill(x2 - 1, y + 2, x2, y2 - 2, borderColor); // Right

        if (isPrimary) {
            // 2px sanfter Schatten unten
            graphics.fill(x + 2, y2 - 2, x2 - 2, y2, bottomColor);
        } else {
            graphics.fill(x + 2, y2 - 1, x2 - 2, y2, borderColor);
        }

        // Subtile Ecken (keine Pixelstufen!)
        int cornerColor = blend(borderColor, bgColor, 0.5f);
        graphics.fill(x + 1, y + 1, x + 2, y + 2, cornerColor);
        graphics.fill(x2 - 2, y + 1, x2 - 1, y + 2, cornerColor);
        graphics.fill(x + 1, y2 - 2, x + 2, y2 - 1, isPrimary ? bottomColor : cornerColor);
        graphics.fill(x2 - 2, y2 - 2, x2 - 1, y2 - 1, isPrimary ? bottomColor : cornerColor);
    }

    /**
     * Zeichnet eine dezente Info-Pille (Badge).
     */
    public static void drawSleekPill(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
                                    int bgColor, int borderColor) {
        int x2 = x + width;
        int y2 = y + height;

        graphics.fill(x + 1, y + 1, x2 - 1, y2 - 1, bgColor);
        graphics.fill(x + 2, y, x2 - 2, y + 1, borderColor);
        graphics.fill(x + 2, y2 - 1, x2 - 2, y2, borderColor);
        graphics.fill(x, y + 2, x + 1, y2 - 2, borderColor);
        graphics.fill(x2 - 1, y + 2, x2, y2 - 2, borderColor);
    }
}

