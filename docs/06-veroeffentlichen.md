# 6. Veröffentlichen und Beiträge

## Eigenes Addon verteilen

1. Dokumentiere die getesteten Minecraft-, Loader- und NRC-Versionen.
2. Baue ein Produktions-JAR mit `gradlew build` und prüfe Dateiname und Metadaten.
3. Veröffentliche Quellcode, Build-Anleitung, Changelog, Abhängigkeiten und Lizenz im eigenen Git-Repository.
4. Weise darauf hin, wenn NRC erforderlich ist oder die Integration interne NRC-Klassen nutzt.
5. Verlinke den Client auf [norisk.gg](https://norisk.gg/) bzw. die [offizielle Download-Seite](https://norisk.gg/downloads).
6. Nenne Drittanbieter-Abhängigkeiten/Lizenzen und prüfe die Rechte aller mitgelieferten Medien.
7. Installationshinweis: JAR in den `mods`-Ordner des richtigen Profils legen und Minecraft-/Loader-Version abgleichen.

Veröffentliche keine fremden NRC-, Minecraft- oder Drittanbieter-Binaries, solange die Weitergaberechte nicht geklärt sind.

## Modrinth und CurseForge

Vor einem Upload die aktuelle Plattformhilfe lesen. Projektart, Version, Loader, Abhängigkeiten, Lizenz, Beschreibung und Datei müssen zusammenpassen. Veröffentliche nur Assets, die du verwenden darfst.

- **Modrinth:** Die Plattform bietet getrennte Projekt- und Medienfelder und verlangt passende Inhalts-Offenlegungen. Primär oder vollständig KI-generierte Bilder dürfen laut aktueller Richtlinie nicht auf der Projektseite verwendet werden. [Aktuelle Regeln zu KI und Projektbildern](https://support.modrinth.com/en/articles/16551575-disclosure-and-usage-of-ai), [Rechte bei Modpacks](https://support.modrinth.com/en/articles/8797527-obtaining-modpack-permissions).
- **CurseForge:** Im Authors-Dashboard ein Minecraft-Projekt erstellen und nach Moderation veröffentlichen. Das Projektlogo soll laut Einreichungsleitfaden ein eigenes quadratisches PNG mit mindestens 400×400 Pixeln sein. Die Datei `docs/assets/norisk-client-install.webp` ist ein breites CTA-Banner und erfüllt diese Logo-Anforderung nicht. Verwende sie nur als passende Seiten-Grafik, wenn du die nötigen Marken-/Nutzungsrechte besitzt. [Einreichungs- und Bildhinweise](https://support.curseforge.com/support/solutions/articles/9000199552).

Details zum Banner und eine Vorschau enthält die [kurze Veröffentlichungshilfe](veroeffentlichen.html).

## Offizielle NRC-Daten-Repositories

### Modpack-Konfiguration

Lies den aktuellen [CLIENT_CONTRIBUTING.md](https://github.com/NoRiskClient/nrc-configs/blob/main/CLIENT_CONTRIBUTING.md) im offiziellen [`nrc-configs`](https://github.com/NoRiskClient/nrc-configs). Nutze die aktuellen Templates, Schemas und Pull-Request-Schritte. JSON-Konfiguration fügt nicht beliebigen Java-Code in den Client ein.

### Server-Mappings

Nutze Anleitung und Schema des offiziellen [`nrc-server-mappings`](https://github.com/NoRiskClient/nrc-server-mappings). Server-Mappings sind Metadaten und Assets, keine Server-Plug-ins oder Addons.

### NRC-Code oder SDK

Schlage Änderungen in den offiziellen Repositories oder Issue-Kanälen von [NoRiskClient auf GitHub](https://github.com/NoRiskClient) vor. Behandle undokumentierte interne Klassen nicht als stabile API.

## Faire und sichere Addons

Erkläre, was dein Addon tut und welche Daten es verarbeitet. Respektiere Spiel-/Serverregeln und Drittanbieter-Lizenzen. Umgehe keine Anti-Cheat-, Zugriffs- oder Schutzmechanismen. Teile keine Zugangsdaten oder privaten Logs.

