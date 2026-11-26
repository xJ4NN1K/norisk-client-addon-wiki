package gg.norisk.addon.popup.ui;

import gg.norisk.addon.popup.detector.NoRiskDetector;
import gg.norisk.addon.popup.sync.LauncherThemeSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

import java.awt.Desktop;
import java.net.URI;

/**
 * Universal Popup-Bildschirm im 1:1 modernen NoRisk Launcher-Design (Modal.tsx).
 *
 * Merkmale:
 *  - Keine Treppenstufen, keine Retro-Minecraft-Pixeloptik:
 *    Präzise 1px-Vektorgeometrie mit sanfter Kantenglättung.
 *  - Echter moderner Vector-Font (Uniform/SmallCaps) statt Blockschrift.
 *  - Dynamischer Live-Sync mit dem Launcher (Farbe & Eckenstil).
 *  - 3 Buttons: "Zum Download", "Nicht erneut anzeigen", "Okay".
 */
public class NoRiskMissingScreen extends Screen {

    // minecraft:uniform  = sauberer Vektorfont (kein pixeliges Bitmap!)
    public static final FontDescription MODERN_FONT =
            new FontDescription.Resource(Identifier.fromNamespaceAndPath("minecraft", "uniform"));

    // norisk-popup-example:smallcaps = Launcher-Schrift aus launcher.zip
    public static final FontDescription SMALLCAPS_FONT =
            new FontDescription.Resource(Identifier.fromNamespaceAndPath("norisk-popup-example", "smallcaps"));

    private final Screen parent;
    private final String addonName;

    public NoRiskMissingScreen(Screen parent) {
        this(parent, "NRC Addon");
    }

    public NoRiskMissingScreen(Screen parent, String addonName) {
        super(Component.literal("NoRisk Client erforderlich"));
        this.parent = parent;
        this.addonName = addonName;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int modalWidth = 380;
        int modalHeight = 205;

        int modalX = centerX - modalWidth / 2;
        int modalY = centerY - modalHeight / 2;

        int footerY = modalY + modalHeight - 32;
        int buttonHeight = 22;

        // Horizontale Anordnung der 3 Buttons
        int btn1W = 110;
        int btn2W = 140;
        int btn3W = 70;
        int spacing = 8;

        int totalBtnW = btn1W + btn2W + btn3W + spacing * 2;
        int startBtnX = centerX - totalBtnW / 2;

        // 1. "Zum Download" (Primär-Button)
        this.addRenderableWidget(new LauncherButton(
                startBtnX,
                footerY,
                btn1W,
                buttonHeight,
                Component.literal("\uD83C\uDF10 Zum Download"),
                LauncherButton.ButtonStyle.PRIMARY,
                btn -> openDownloadLink()
        ));

        // 2. "Nicht erneut anzeigen" (Sekundär-Button)
        this.addRenderableWidget(new LauncherButton(
                startBtnX + btn1W + spacing,
                footerY,
                btn2W,
                buttonHeight,
                Component.literal("Nicht erneut anzeigen"),
                LauncherButton.ButtonStyle.SECONDARY,
                btn -> {
                    NoRiskDetector.setNoticeDismissed(true);
                    closeToParent();
                }
        ));

        // 3. "Okay" (Neutral-Button)
        this.addRenderableWidget(new LauncherButton(
                startBtnX + btn1W + spacing + btn2W + spacing,
                footerY,
                btn3W,
                buttonHeight,
                Component.literal("Okay"),
                LauncherButton.ButtonStyle.NEUTRAL,
                btn -> closeToParent()
        ));

        // Close '✕' Button oben rechts im Header
        int closeBtnSize = 18;
        int closeX = modalX + modalWidth - closeBtnSize - 6;
        int closeY = modalY + 5;
        this.addRenderableWidget(new LauncherButton(
                closeX,
                closeY,
                closeBtnSize,
                closeBtnSize,
                Component.literal("x"),
                LauncherButton.ButtonStyle.CLOSE_ICON,
                btn -> closeToParent()
        ));
    }

    private void openDownloadLink() {
        String url = "https://norisk.gg";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                return;
            }
        } catch (Throwable ignored) {}

        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }
        } catch (Throwable ignored) {}
    }

    private void closeToParent() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.gui != null) {
            mc.gui.setScreen(this.parent);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        // 1. Dunkler Backdrop (70% Deckkraft wie im Launcher 'bg-black/70')
        graphics.fill(0, 0, this.width, this.height, 0xB3000000);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int modalWidth = 380;
        int modalHeight = 205;
        int x = centerX - modalWidth / 2;
        int y = centerY - modalHeight / 2;

        int accent = LauncherThemeSync.getAccentColor();

        // Accent-Farbe ohne Alpha-Byte extrahieren (nur RGB)
        int accentRgb = accent & 0x00FFFFFF;

        // 2. Sauberes Launcher Panel (Dunkles Theme #111319 + feiner 1px Rahmen + 2px Akzent-Bodenkante)
        int panelBg      = 0xF5111319;
        int borderCol    = 0x77000000 | accentRgb;
        int bottomAccent = 0xFF000000 | accentRgb;
        RenderUtils.drawSleekPanel(graphics, x, y, modalWidth, modalHeight, panelBg, borderCol, bottomAccent);

        // 3. Header Bar (Höhe: 28px, dezenter Akzent-Tint 15%, 1px feiner Trennstrich)
        int headerHeight  = 28;
        int headerBg      = 0x24000000 | accentRgb;
        int headerDivider = 0x44000000 | accentRgb;

        graphics.fill(x + 2, y + 1, x + modalWidth - 2, y + headerHeight, headerBg);
        graphics.fill(x + 1, y + headerHeight, x + modalWidth - 1, y + headerHeight + 1, headerDivider);

        // Header Titel (Smallcaps-Font aus dem Launcher = scharfe moderne Schrift)
        Minecraft mc = Minecraft.getInstance();

        // Warn-Dreieck (§c = rot) + Header-Titel in SmallCaps
        Component warningIcon = Component.literal("! ").withStyle(s ->
                s.withFont(MODERN_FONT).withColor(0xFFEF4444).withItalic(false));
        Component headerTitle = Component.literal("NORISK CLIENT ERFORDERLICH").withStyle(s ->
                s.withFont(SMALLCAPS_FONT).withColor(0xFFFFFFFF).withItalic(false));

        int headerTextY = y + (headerHeight - mc.font.lineHeight) / 2;
        graphics.text(mc.font, warningIcon, x + 10, headerTextY, 0xFFEF4444);
        graphics.text(mc.font, headerTitle, x + 24, headerTextY, 0xFFFFFFFF);

        // 4. Inhaltsbereich
        int textY = y + 38;

        // Subtitel (Warnung gelb)
        Component subTitle = Component.literal("Hinweis: NoRisk Client nicht gefunden").withStyle(s ->
                s.withFont(MODERN_FONT).withBold(true).withColor(0xFFFFD166).withItalic(false));
        graphics.centeredText(mc.font, subTitle, centerX, textY, 0xFFFFD166);

        // Zeile 1: Addon-Name + "benötigt den NoRisk Client"
        textY += 17;
        Component line1 = Component.empty()
                .append(Component.literal("Das Addon ").withStyle(s ->
                        s.withFont(MODERN_FONT).withColor(0xFFE5E7EB).withItalic(false)))
                .append(Component.literal(this.addonName).withStyle(s ->
                        s.withFont(MODERN_FONT).withBold(true).withColor(0xFF000000 | accentRgb).withItalic(false)))
                .append(Component.literal(" benoetigt den ").withStyle(s ->
                        s.withFont(MODERN_FONT).withColor(0xFFE5E7EB).withItalic(false)))
                .append(Component.literal("NoRisk Client (NRC)").withStyle(s ->
                        s.withFont(MODERN_FONT).withBold(true).withColor(0xFF000000 | accentRgb).withItalic(false)))
                .append(Component.literal(",").withStyle(s ->
                        s.withFont(MODERN_FONT).withColor(0xFFE5E7EB).withItalic(false)));
        graphics.centeredText(mc.font, line1, centerX, textY, 0xFFE5E7EB);

        // Zeile 2: Erklärung
        textY += 12;
        Component line2 = Component.literal("um Module, Themes und Menues zu aktivieren.").withStyle(s ->
                s.withFont(MODERN_FONT).withColor(0xFF9CA3AF).withItalic(false));
        graphics.centeredText(mc.font, line2, centerX, textY, 0xFF9CA3AF);

        // Zeile 3: Beruhigung (grün)
        textY += 12;
        Component line3 = Component.literal("Du kannst Minecraft trotzdem weiterspielen!").withStyle(s ->
                s.withFont(MODERN_FONT).withColor(0xFF34D399).withItalic(false));
        graphics.centeredText(mc.font, line3, centerX, textY, 0xFF34D399);

        // 5. Kompatibilitäts-Badge (Clean Pill)
        textY += 16;
        int badgeW = 320;
        int badgeH = 15;
        int badgeX = centerX - badgeW / 2;
        RenderUtils.drawSleekPill(graphics, badgeX, textY, badgeW, badgeH, 0x33000000, 0x1AFFFFFF);

        Component badgeText = Component.literal("Kompatibel: Launcher, Standalone & Mini").withStyle(s ->
                s.withFont(MODERN_FONT).withColor(0xFF6B7280).withItalic(false));
        graphics.centeredText(mc.font, badgeText, centerX, textY + 3, 0xFF6B7280);

        // 6. Trennlinie vor Buttons
        int footerLineY = y + modalHeight - 40;
        graphics.fill(x + 12, footerLineY, x + modalWidth - 12, footerLineY + 1, 0x1AFFFFFF);

        // 7. Buttons rendern
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        closeToParent();
    }
}

