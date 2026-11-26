# NoRisk Client Universal Popup & Mod Template

Standard-Vorlage für alle NoRisk Client Addons auf Fabric (Minecraft 26.x).

## Highlights
- **100% Startgarantie**: Startet sowohl mit als auch ohne NoRisk Client ohne Abstürze.
- **Unterstützt alle NoRisk Versionen**: NoRisk Launcher Profil (`nrcclient`), NRC Mini (`nrcmini`), NRC Standalone (`nrcstandalone`).
- **Launcher-Design Sync**: Akzentfarbe und Eckenabrundung (Border-Radius) synchronisieren automatisch mit dem NoRisk Launcher.
- **Keine Mixin-Crashes**: Keine `MixinTargetAlreadyLoadedException`, da keine Klassen vorzeitig via `Class.forName` geladen werden.
- **3 Buttons**:
  1. `🌐 Zum Download`: Öffnet `https://norisk.gg`
  2. `Nicht erneut anzeigen`: Speichert den Zustand in `.minecraft/config/norisk-addons-notice.json`
  3. `Okay`: Schließt das Fenster und kehrt zum Titelbildschirm zurück.

Ausführliche Dokumentation und Integrationsanleitung siehe:
👉 [ANLEITUNG_NRC_POPUP_INTEGRATION.md](file:///D:/NoRiskClient-Addons-Builder/Infomationen-f%C3%BCr-die-addons--mods/norisk-popup-example/ANLEITUNG_NRC_POPUP_INTEGRATION.md)

