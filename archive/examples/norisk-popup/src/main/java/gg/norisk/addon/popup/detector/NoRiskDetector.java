package gg.norisk.addon.popup.detector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Universeller und 100% crash-sicherer Detektor für den NoRisk Client (NRC).
 *
 * Unterstützt ALLE NoRisk-Versionen:
 *  - NoRisk Launcher Profil (Mod-ID: nrcclient)
 *  - NRC Mini (Mod-ID: nrcmini)
 *  - NRC Standalone (Mod-ID: nrcstandalone)
 *  - Zukünftige NoRisk Versionen (Mod-ID: noriskclient, norisk-client, nrcabstraction)
 *
 * SICHERHEITSHINWEIS (WICHTIG):
 * Verwendet KEIN Class.forName() während der Mixin-Phase!
 * Stattdessen werden Fabric-Loader-Abfragen und getResource()-Prüfungen genutzt,
 * wodurch der gefürchtete MixinTargetAlreadyLoadedException-Crash unmöglich wird.
 */
public final class NoRiskDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger("NRC-Detector");
    private static Boolean cachedPresent = null;
    private static String cachedVariant = null;

    private static final Path NOTICE_CONFIG_FILE =
            FabricLoader.getInstance().getConfigDir().resolve("norisk-addons-notice.json");

    private NoRiskDetector() {}

    /**
     * Prüft, ob irgendeine NoRisk Client Variante geladen ist.
     */
    public static boolean isNoRiskPresent() {
        if (cachedPresent != null) {
            return cachedPresent;
        }

        FabricLoader loader = FabricLoader.getInstance();

        // 1. Prüfung über Fabric Mod-IDs (schnell, sicher und 100% crash-frei)
        if (loader.isModLoaded("nrcclient")) {
            cachedVariant = "NoRisk Client (Launcher Profile)";
            cachedPresent = true;
            return true;
        }
        if (loader.isModLoaded("nrcmini")) {
            cachedVariant = "NRC Mini";
            cachedPresent = true;
            return true;
        }
        if (loader.isModLoaded("nrcstandalone")) {
            cachedVariant = "NRC Standalone";
            cachedPresent = true;
            return true;
        }
        if (loader.isModLoaded("nrcabstraction")) {
            cachedVariant = "NRC Abstraction Layer";
            cachedPresent = true;
            return true;
        }
        if (loader.isModLoaded("noriskclient") || loader.isModLoaded("norisk-client")) {
            cachedVariant = "NoRisk Client (Generic)";
            cachedPresent = true;
            return true;
        }

        // 2. Prüfung über Classpath-Ressourcen OHNE die Klasse zu initialisieren!
        // (Verhindert MixinTargetAlreadyLoadedException vollständig)
        ClassLoader cl = NoRiskDetector.class.getClassLoader();
        if (cl != null) {
            if (cl.getResource("gg/norisk/ui/api/module/Module.class") != null) {
                cachedVariant = "NRC Module Framework (Classpath)";
                cachedPresent = true;
                return true;
            }
            if (cl.getResource("gg/norisk/ui/modules/v3/V3Theme.class") != null) {
                cachedVariant = "NRC V3 UI (Classpath)";
                cachedPresent = true;
                return true;
            }
            if (cl.getResource("gg/norisk/client/v2/plugin/NrcMixinCanceller.class") != null) {
                cachedVariant = "NRC Mixin Canceller (Classpath)";
                cachedPresent = true;
                return true;
            }
        }

        cachedVariant = "Nicht installiert (Vanilla Fabric)";
        cachedPresent = false;
        return false;
    }

    /**
     * Gibt den Namen der erkannten NoRisk-Variante zurück.
     */
    public static String getDetectedVariant() {
        if (cachedVariant == null) {
            isNoRiskPresent();
        }
        return cachedVariant != null ? cachedVariant : "Unbekannt";
    }

    /**
     * Prüft, ob der Benutzer die Warnung dauerhaft deaktiviert hat ("Nicht erneut anzeigen").
     */
    public static boolean isNoticeDismissed() {
        try {
            File f = NOTICE_CONFIG_FILE.toFile();
            if (!f.exists()) return false;
            String content = Files.readString(NOTICE_CONFIG_FILE, StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
            return obj.has("dismissed") && obj.get("dismissed").getAsBoolean();
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Speichert den Zustand, ob das Popup erneut angezeigt werden soll.
     */
    public static void setNoticeDismissed(boolean dismissed) {
        try {
            JsonObject obj = new JsonObject();
            if (NOTICE_CONFIG_FILE.toFile().exists()) {
                try {
                    String existing = Files.readString(NOTICE_CONFIG_FILE, StandardCharsets.UTF_8);
                    obj = JsonParser.parseString(existing).getAsJsonObject();
                } catch (Throwable ignored) {}
            }
            obj.addProperty("dismissed", dismissed);
            obj.addProperty("lastUpdated", System.currentTimeMillis());
            Files.writeString(NOTICE_CONFIG_FILE, obj.toString(), StandardCharsets.UTF_8);
            LOGGER.info("[NRC-Detector] Notice-Status aktualisiert: dismissed = {}", dismissed);
        } catch (Throwable t) {
            LOGGER.error("[NRC-Detector] Fehler beim Speichern von notice config", t);
        }
    }
}

