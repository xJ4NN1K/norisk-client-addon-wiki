package gg.norisk.addon.popup;

import gg.norisk.addon.popup.detector.NoRiskDetector;
import gg.norisk.ui.modules.api.ModuleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Isolierte NoRisk-Integrationsklasse.
 *
 * ARCHITEKTUR-MUSTER (WICHTIG):
 * Diese Klasse darf NUR aufgerufen werden, wenn {@link NoRiskDetector#isNoRiskPresent()} true zurückgibt!
 * Durch diese Trennung versucht die JVM im Vanilla-/Standalone-Fabric-Modus NIEMALS,
 * die NoRisk-Klassen (gg.norisk.*) in den ClassLoader zu laden.
 * Das garantiert 100%ige Absturzfreiheit (kein NoClassDefFoundError)!
 */
public final class NoRiskIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger("NRC-Integration");

    private NoRiskIntegration() {}

    /**
     * Wird nur aufgerufen, wenn NoRisk Client vorhanden ist.
     * Hier können eigene NoRisk-Module, HUD-Elemente, Themes und Hooks registriert werden.
     */
    public static void init() {
        LOGGER.info("[NRC-Popup-Template] Initialisiere NoRisk Client Integration für: {}", NoRiskDetector.getDetectedVariant());

        try {
            // Beispiel: NoRisk Module registrieren
            // ModuleProvider.Companion.register(MY_MODULE);
            LOGGER.info("[NRC-Popup-Template] NoRisk-Module erfolgreich bereitgestellt!");
        } catch (Throwable t) {
            LOGGER.error("[NRC-Popup-Template] Fehler bei der NoRisk Modul-Registrierung", t);
        }
    }
}

