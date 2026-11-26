package gg.norisk.addon.popup;

import gg.norisk.addon.popup.detector.NoRiskDetector;
import gg.norisk.addon.popup.sync.LauncherThemeSync;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Universeller Haupteinstiegspunkt für alle Fabric 26.x Versionen.
 *
 * Verhält sich immer absolut sicher:
 *  1. Startet IMMER, egal ob NoRisk Client installiert ist oder nicht.
 *  2. Erkennt NoRisk Client in allen Varianten (Launcher, Standalone, Mini).
 *  3. Bei fehlendem NRC: Öffnet das stylische NoRisk-Launcher-Popup auf dem TitleScreen.
 *  4. Bei vorhandenem NRC: Startet sofort die NoRisk-Addon-Funktionen.
 */
public final class PopupTemplateMod implements ClientModInitializer {
    public static final String MOD_ID = "norisk-popup-example";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[NRC-Popup-Template] Initialisiere universelles Addon Template...");

        // Live-Sync mit dem Launcher anstoßen (Akzentfarbe + Border-Radius)
        int accent = LauncherThemeSync.getAccentColor();
        int radius = LauncherThemeSync.getBorderRadius();
        LOGGER.info("[NRC-Popup-Template] Launcher-Theme synchronisiert: Akzent = #{} | Radius = {}px",
                String.format("%06X", accent), radius);

        // Sicherheitsprüfung auf NoRisk Client
        if (NoRiskDetector.isNoRiskPresent()) {
            LOGGER.info("[NRC-Popup-Template] NoRisk Client erfolgreich erkannt: '{}'", NoRiskDetector.getDetectedVariant());
            // NoRisk Features sicher initialisieren
            NoRiskIntegration.init();
        } else {
            LOGGER.warn("[NRC-Popup-Template] NoRisk Client wurde nicht gefunden! Addon läuft im Standby-Modus.");
            LOGGER.warn("[NRC-Popup-Template] Auf dem Titelbildschirm wird das NoRisk-Popup angezeigt (falls nicht deaktiviert).");
        }
    }
}

