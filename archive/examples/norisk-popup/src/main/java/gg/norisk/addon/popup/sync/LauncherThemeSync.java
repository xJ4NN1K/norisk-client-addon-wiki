package gg.norisk.addon.popup.sync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Synchronisiert Farbe und Corner-Radius live mit dem NoRisk Launcher (0.6.x).
 *
 * Quellen:
 *  1. appearance.json (GameDir, LauncherPaths, config)
 *  2. WebView2 LevelDB-LocalStorage des NoRisk Launchers (norisk-theme-storage)
 *  3. Fallback: NoRisk Cyan (#00B9E8) und Radius 8px
 */
public final class LauncherThemeSync {
    private static final Logger LOGGER = LoggerFactory.getLogger("NRC-ThemeSync");

    // Default NoRisk Werte (entspricht noriskclient-launcher-0.6.29)
    public static final int DEFAULT_ACCENT_COLOR = 0x00B9E8; // Cyan
    public static final int DEFAULT_BORDER_RADIUS = 8;        // 8px

    private static final String[] LAUNCHER_STORAGE_IDS = {
            "gg.norisk.NoRiskClientLauncherV3",
            "gg.norisk.NoRiskClientV3",
            "noriskclient-launcher"
    };

    private static volatile int cachedAccentColor = DEFAULT_ACCENT_COLOR;
    private static volatile int cachedBorderRadius = DEFAULT_BORDER_RADIUS;
    private static volatile String cachedAccentName = "Cyan";
    private static volatile long lastSyncTime = 0L;
    private static volatile boolean isSyncing = false;

    static {
        // Initialer synchroner Lesevorgang beim Klassenladen
        try {
            readFromStorage();
        } catch (Throwable ignored) {}
    }

    private LauncherThemeSync() {}

    /**
     * Gibt die aktuelle Launcher-Akzentfarbe zurück (RGB 0xRRGGBB).
     */
    public static int getAccentColor() {
        checkAutoRefresh();
        return cachedAccentColor;
    }

    /**
     * Gibt den Launcher-Corner-Radius zurück (z.B. 8, 12, etc.).
     */
    public static int getBorderRadius() {
        checkAutoRefresh();
        return cachedBorderRadius;
    }

    /**
     * Erzeugt eine ARGB-Farbe mit der aktuellen Akzentfarbe und bestimmtem Alpha (0-255).
     */
    public static int getAccentWithAlpha(int alpha) {
        int rgb = getAccentColor();
        return ((alpha & 0xFF) << 24) | (rgb & 0xFFFFFF);
    }

    /**
     * Dunkelt eine RGB-Farbe um einen Faktor ab (0.0 = unverändert, 1.0 = schwarz).
     */
    public static int darken(int rgb, float amount) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        r = Math.max(0, (int) (r * (1.0f - amount)));
        g = Math.max(0, (int) (g * (1.0f - amount)));
        b = Math.max(0, (int) (b * (1.0f - amount)));

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Hellt eine RGB-Farbe um einen Faktor auf (0.0 = unverändert, 1.0 = weiß).
     */
    public static int lighten(int rgb, float amount) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        r = Math.min(255, (int) (r + (255 - r) * amount));
        g = Math.min(255, (int) (g + (255 - g) * amount));
        b = Math.min(255, (int) (b + (255 - b) * amount));

        return (r << 16) | (g << 8) | b;
    }

    /**
     * Mischt zwei Farben mit gegebenem Verhältnis (ratio 0.0 = c1, 1.0 = c2).
     */
    public static int blend(int c1, int c2, float ratio) {
        float inv = 1.0f - ratio;
        int a = (int) (((c1 >> 24) & 0xFF) * inv + ((c2 >> 24) & 0xFF) * ratio);
        int r = (int) (((c1 >> 16) & 0xFF) * inv + ((c2 >> 16) & 0xFF) * ratio);
        int g = (int) (((c1 >> 8) & 0xFF) * inv + ((c2 >> 8) & 0xFF) * ratio);
        int b = (int) ((c1 & 0xFF) * inv + (c2 & 0xFF) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static void checkAutoRefresh() {
        long now = System.currentTimeMillis();
        if (!isSyncing && (now - lastSyncTime > 1500)) {
            lastSyncTime = now;
            isSyncing = true;
            CompletableFuture.runAsync(() -> {
                try {
                    readFromStorage();
                } catch (Throwable ignored) {
                } finally {
                    isSyncing = false;
                }
            });
        }
    }

    private static void readFromStorage() {
        // 1. Suche appearance.json
        File jsonFile = findAppearanceJson();
        if (jsonFile != null && jsonFile.exists()) {
            try {
                String text = Files.readString(jsonFile.toPath(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(text).getAsJsonObject();
                if (obj.has("accent")) {
                    Integer parsed = parseHex(obj.get("accent").getAsString());
                    if (parsed != null) cachedAccentColor = parsed;
                }
                if (obj.has("radius")) {
                    cachedBorderRadius = Math.max(0, Math.min(24, obj.get("radius").getAsInt()));
                } else if (obj.has("borderRadius")) {
                    cachedBorderRadius = Math.max(0, Math.min(24, obj.get("borderRadius").getAsInt()));
                }
                return;
            } catch (Throwable ignored) {}
        }

        // 2. Suche in WebView2 LocalStorage des Launchers
        try {
            readFromWebViewStorage();
        } catch (Throwable ignored) {}
    }

    private static File findAppearanceJson() {
        List<File> candidates = new ArrayList<>();

        // LauncherPaths Reflection (falls NoRisk Abstraction geladen ist)
        try {
            Class<?> pathsClass = Class.forName("gg.norisk.abstraction.path.LauncherPaths");
            Object instance = pathsClass.getField("INSTANCE").get(null);
            File root = (File) pathsClass.getMethod("getRoot").invoke(instance);
            if (root != null) {
                candidates.add(new File(root, "appearance.json"));
                candidates.add(new File(new File(root, "meta"), "appearance.json"));
            }
        } catch (Throwable ignored) {}

        // Minecraft GameDir / norisk Verzeichnis
        try {
            Path gameDir = FabricLoader.getInstance().getGameDir();
            candidates.add(gameDir.resolve("appearance.json").toFile());
            candidates.add(gameDir.resolve("norisk").resolve("appearance.json").toFile());
            candidates.add(gameDir.resolve("config").resolve("appearance.json").toFile());
        } catch (Throwable ignored) {}

        for (File f : candidates) {
            if (f != null && f.isFile()) return f;
        }
        return null;
    }

    private static void readFromWebViewStorage() {
        List<Path> storageDirs = getWebViewStorageDirs();
        for (Path dir : storageDirs) {
            File[] files = dir.toFile().listFiles((d, n) -> n.endsWith(".log") || n.endsWith(".ldb"));
            if (files == null || files.length == 0) continue;
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            String themeJson = findStateJson(files, "norisk-theme-storage");
            if (themeJson == null) continue;

            try {
                JsonObject root = JsonParser.parseString(themeJson).getAsJsonObject();
                JsonObject state = root.getAsJsonObject("state");
                if (state != null) {
                    if (state.has("accentColor") && state.get("accentColor").isJsonObject()) {
                        JsonObject ac = state.getAsJsonObject("accentColor");
                        if (ac.has("value")) {
                            Integer col = parseHex(ac.get("value").getAsString());
                            if (col != null) cachedAccentColor = col;
                        }
                        if (ac.has("name")) {
                            cachedAccentName = ac.get("name").getAsString();
                        }
                    }
                    if (state.has("borderRadius")) {
                        cachedBorderRadius = Math.max(0, Math.min(24, state.get("borderRadius").getAsInt()));
                    }
                    return;
                }
            } catch (Throwable ignored) {}
        }
    }

    private static List<Path> getWebViewStorageDirs() {
        List<Path> list = new ArrayList<>();
        String local = System.getenv("LOCALAPPDATA");
        if (local != null) {
            for (String id : LAUNCHER_STORAGE_IDS) {
                list.add(Path.of(local, id, "EBWebView", "Default", "Local Storage", "leveldb"));
            }
        }
        return list.stream().filter(p -> p.toFile().isDirectory()).toList();
    }

    private static String findStateJson(File[] files, String key) {
        for (File f : files) {
            try {
                byte[] raw = Files.readAllBytes(f.toPath());
                String content = new String(raw, StandardCharsets.ISO_8859_1);
                int idx = content.lastIndexOf(key);
                if (idx >= 0) {
                    int start = content.indexOf("{\"state\"", idx + key.length());
                    if (start >= 0 && start - (idx + key.length()) < 40) {
                        String json = extractBalancedJson(content, start);
                        if (json != null) return json;
                    }
                }
            } catch (Throwable ignored) {}
        }
        return null;
    }

    private static String extractBalancedJson(String s, int start) {
        int depth = 0;
        boolean inStr = false;
        boolean esc = false;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inStr) {
                if (esc) esc = false;
                else if (c == '\\') esc = true;
                else if (c == '"') inStr = false;
                continue;
            }
            if (c == '"') inStr = true;
            else if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return s.substring(start, i + 1);
            }
        }
        return null;
    }

    public static Integer parseHex(String hex) {
        if (hex == null) return null;
        String h = hex.trim();
        if (h.startsWith("#")) h = h.substring(1);
        if (h.length() == 3) {
            h = "" + h.charAt(0) + h.charAt(0) + h.charAt(1) + h.charAt(1) + h.charAt(2) + h.charAt(2);
        }
        if (h.length() >= 6) {
            h = h.substring(0, 6);
            try {
                return Integer.parseInt(h, 16) & 0xFFFFFF;
            } catch (Throwable ignored) {}
        }
        return null;
    }
}

