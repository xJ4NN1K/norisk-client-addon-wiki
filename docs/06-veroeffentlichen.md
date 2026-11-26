# 6. Veröffentlichen und Beiträge

## Eigenes Addon verteilen

1. Entscheide, welche Minecraft-/Loader-/NRC-Versionen getestet wurden und dokumentiere sie.
2. Erzeuge ein reproduzierbares Release-JAR mit `gradlew build`; verifiziere Dateiname und Metadaten.
3. Veröffentliche Quellcode, Build-Anleitung, Changelog, Abhängigkeiten und Lizenz im eigenen Git-Repository.
4. Weise klar aus, wenn NRC erforderlich ist und ob NRC-interne Integration versionsgebunden ist.
5. Verlinke den Client ausschließlich auf [norisk.gg](https://norisk.gg/) bzw. [Downloads](https://norisk.gg/downloads).
6. Nenne Drittanbieter-Abhängigkeiten und deren Lizenzen.
7. Installationshinweis: JAR in den `mods`-Ordner des richtigen Profils legen, dabei Minecraft- und Loader-Version abgleichen. Profile können eigene Mods-Ordner haben.

Ein öffentlicher Release darf keine fremden NRC-/Minecraft-/Drittanbieter-Binaries enthalten, wenn Verteilungsrechte nicht geprüft sind.

## Offizielle Daten-Repositories beitragen

### Modpack-Konfiguration

Lies den aktuellen [CLIENT_CONTRIBUTING.md](https://github.com/NoRiskClient/nrc-configs/blob/main/CLIENT_CONTRIBUTING.md) im offiziellen [`nrc-configs`](https://github.com/NoRiskClient/nrc-configs). Nutze die dort aktuellen Templates, Schemas und Pull-Request-Schritte. Die JSON-Konfiguration bringt nicht automatisch beliebigen Java-Code in den Client.

### Server-Mappings

Nutze die aktuelle Anleitung und das Schema im offiziellen [`nrc-server-mappings`](https://github.com/NoRiskClient/nrc-server-mappings). Ein Server-Mapping ist Metadaten/Assets für Serverdarstellung und keine eigene Mod.

### NRC-Code oder SDK

Schlage die Änderung in den aktuellen offiziellen Repositories/Issue-Kanälen von [NoRiskClient auf GitHub](https://github.com/NoRiskClient) vor. Nutze keine undokumentierte interne Klasse unter der Annahme, die Maintainer garantierten Stabilität.

## Faire und sichere Addons

Erstelle Funktionen, die Spielregeln, Serverregeln und Drittanbieter-Lizenzen respektieren. Dokumentiere, was ein Feature tut und welche Daten es verarbeitet. Umgehe keine Anti-Cheat-, Zugriffs- oder Schutzmechanismen. Veröffentliche keine Zugangsdaten, privaten Logs oder Nutzerdaten.

