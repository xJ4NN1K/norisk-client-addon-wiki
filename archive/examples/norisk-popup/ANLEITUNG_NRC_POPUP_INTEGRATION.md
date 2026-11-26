# 📘 NoRisk Client (NRC) Universal Popup & Addon Vorlage
### Kompatibel mit allen 26.x Versionen für Fabric (Launcher, Standalone, Mini & Vanilla)

Dieses Projekt dient als **universelle Referenz-Vorlage** für alle NoRisk Client Addons. Es stellt sicher, dass jedes Addon:
1. **Immer sauber startet** – egal ob NoRisk Client vorhanden ist oder nicht (keine Spielabbrüche, kein Absturz beim Hochfahren).
2. **Alle NoRisk Client Versionen unterstützt** (NoRisk Launcher Profil `nrcclient`, NRC Standalone `nrcstandalone`, NRC Mini `nrcmini` und zukünftige Versionen).
3. **Ein 1:1 synchronisiertes Popup im Design des NoRisk Launchers** anzeigt, falls NoRisk nicht gefunden wurde.
4. **Die 3 geforderten Buttons bereitstellt**:
   - `🌐 Zum Download`: Öffnet die offizielle NoRisk-Webseite (`https://norisk.gg`).
   - `Nicht erneut anzeigen`: Merkt sich die Entscheidung dauerhaft in der Datei `.minecraft/config/norisk-addons-notice.json`.
   - `Okay`: Schließt das Popup und lässt den Spieler Minecraft normal weiterspielen.
5. **Farben & Eckenabrundung (Border-Radius) live mit dem NoRisk Launcher synchronisiert** (liest `appearance.json` und WebView2 LocalStorage).
6. **Clean – Keine Minecraft-Pixel**: Vollständig modern gestaltete Vektor- und Glasflächen ohne die pixeligen Retro-Button-Sprites von Vanilla Minecraft.
7. **100% sichtbare, gestochen scharfe Schrift**: Korrekter 32-Bit ARGB-Alphakanal für alle Texte.

---

## ⚡ 1. Gelöste Probleme & Root-Cause-Analyse

### A. Warum fehlte der Text? ("text ist auch nicht da")
In der neuen Minecraft 26.x Render-Pipeline (`GuiGraphicsExtractor`) werden Textfarben als 32-Bit ARGB interpretiert:
* Schreibt man `0xFFFFFF` oder `0xE0E0E0`, fehlt das oberste Alpha-Byte (`0x00......`). Alpha `0x00` bedeutet **0% Deckkraft** (vollkommen unsichtbar).
* 👉 **Lösung**: Alle Textfarben müssen den Alpha-Kanal `0xFF` gesetzt haben (z.B. `0xFFFFFFFF` für Weiß, `0xFFEAEAEA` für Hellgrau).

### B. Warum sah es pixelig aus? ("Clean keine pixel")
Wenn Buttons von `Button` oder `AbstractButton` erben, ruft Minecrafts internes `extractWidgetRenderState` automatisch `extractDefaultSprite()` auf. Dadurch wird die alte, pixelige Minecraft-Button-Textur gerendert.
* 👉 **Lösung**: `LauncherButton` erbt direkt von `AbstractWidget`. Dadurch wird **keine** Vanilla-Textur geladen. `RenderUtils.drawModernButton` zeichnet saubere, moderne, flache Buttons mit feinem 1px Rahmen und 3D-Bodenkante exakt wie im Launcher.

### C. Warum crashten Addons früher ohne NoRisk?
1. **`Class.forName()` im Mixin-Plugin**:
   Wurde `Class.forName("gg.norisk.ui.api.module.Module")` aufgerufen, wurde die NoRisk-Klasse in die JVM geladen, bevor Mixin sie transformieren konnte (`MixinTargetAlreadyLoadedException`).
   👉 **Lösung**: `NoRiskDetector` prüft per FabricLoader-ID und `cl.getResource("...Module.class") != null` – **ohne** ClassLoading!
2. **`nrcclient` in `depends` der `fabric.mod.json`**:
   👉 **Lösung**: NoRisk-Mod-IDs stehen in `"suggests"` oder `"recommends"`, **niemals** in `"depends"`.
3. **Direkte Importe im Entrypoint**:
   👉 **Lösung**: Trennung in `NoRiskIntegration.java`, die nur geladen wird, wenn `NoRiskDetector.isNoRiskPresent() == true`.

---

## 🎨 2. Design-Spezifikation (1:1 NoRisk Launcher 0.6.29)

Das Design basiert exakt auf `noriskclient-launcher-0.6.29.zip` (`Modal.tsx`, `Button.tsx`, `useThemeStore.ts`):

| Element | NoRisk Launcher Stil | In-Game Umsetzung (`norisk-popup-example`) |
|---|---|---|
| **Backdrop** | `bg-black/70 backdrop-blur-md` | `0xB8000000` (72% abgedunkelter Vollbild-Hintergrund) |
| **Panel-Fläche** | Dark Panel `#111319` | `RenderUtils.drawCleanCard` mit glatter Füllung `0xF5111319` |
| **Border-Radius** | `borderRadius` (Standard 8px) | Dynamisch aus Launcher-Storage synchronisiert |
| **Akzentfarbe** | Ausgewählte Farbe (z.B. Cyan `#00B9E8`) | Dynamisch aus `appearance.json` / WebView2 LevelDB ausgelesen |
| **Glow & 3D Kante** | Top Glow + Solid 3D Bottom | Oberer 2px Glow-Strich (`0x9900B9E8`) + untere 3px massive 3D-Kante |
| **Header Bar** | `${accentColor}30` getönt mit Trennlinie | Getönter Header `0x3300B9E8` mit Divider `0x5500B9E8` |
| **Titel** | SmallCaps/Bold mit Warnsymbol | `§c⚠ §f§lNORISK CLIENT ERFORDERLICH` in `0xFFFFFFFF` |
| **Primär-Button** | Gefüllt mit Akzentfarbe | "🌐 Zum Download" mit Akzentfarbe, Hover-Aufhellung & 3D-Kante |
| **Sekundär-Button** | Gedimmt / Transluzent | "Nicht erneut anzeigen" mit transluzentem Panel `0x551E2330` |
| **Neutral-Button** | Rahmen-Button | "Okay" mit feinem Rahmen |
| **Schließen-Button** | `✕` oben rechts im Header | Schnelles Schließen zum Titelbildschirm |

---

## 📦 3. Wie du dieses System in jedes NRC-Addon übernimmst

Kopiere einfach den Ordner `gg/norisk/addon/popup/` in das jeweilige Addon:
1. `detector/NoRiskDetector.java`
2. `sync/LauncherThemeSync.java`
3. `ui/NoRiskMissingScreen.java`
4. `ui/LauncherButton.java`
5. `ui/RenderUtils.java`
6. `mixin/TitleScreenMixin.java`
7. `mixin/NoRiskSafeMixinPlugin.java`

Passe in `TitleScreenMixin.java` nur den Addon-Namen an:
```java
mc.gui.setScreen(new NoRiskMissingScreen(this, "Dein Addon Name"));
```

Und in `fabric.mod.json`:
```json
{
  "depends": {
    "fabricloader": ">=0.19.2",
    "fabric-api": "*",
    "minecraft": ">=26.0 <27.0"
  },
  "suggests": {
    "nrcclient": "*",
    "nrcmini": "*",
    "nrcstandalone": "*"
  }
}
```

---

## 🚀 4. Bauen

Im Ordner `norisk-popup-example`:
```bash
.\gradlew.bat build
```
Das fertige Mod-Jar befindet sich unter:
`norisk-popup-example/build/libs/norisk-popup-example-1.0.0.jar`

