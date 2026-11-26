# 1. NoRisk Client verstehen

## Was NRC ist

NoRisk Client (NRC) ist ein kostenloses Minecraft-Ökosystem mit Launcher und Client-Funktionen. Der Launcher verwaltet Minecraft-Profile mit getrennten Versionen, Mods und Einstellungen. Der Java-Client ergänzt Minecraft unter anderem um Module, HUD-Editor, Social-Funktionen und Cosmetics. Das offizielle Angebot umfasst außerdem einen Bedrock-Client; Java-Fabric-Mods laufen nicht automatisch auf Bedrock.

Aktuelle Produkt- und Download-Informationen stehen auf [norisk.gg](https://norisk.gg/) und [norisk.gg/downloads](https://norisk.gg/downloads). Funktionen, Versionen und Plattformunterstützung ändern sich. Für technische Aussagen zählt immer die ausgewählte Minecraft-Version und das zugehörige Profil.

## NRC-Funktionen auf einen Blick

Die genaue Auswahl hängt von Minecraft-Version, Betriebssystem und Profil ab. Die offizielle Website stellt aktuell folgende Bereiche des Java-Clients und Launchers heraus:

- **Profile und Versionen:** Profile halten Spielversion, Mods, Einstellungen und Welten getrennt. So kann man Builds testen, ohne das Hauptprofil zu verändern.
- **Mod-Verwaltung:** Der Launcher bewirbt eine gemeinsame Suche nach Mods aus Modrinth und CurseForge und löst passende Loader, Versionen und Abhängigkeiten für das ausgewählte Profil auf.
- **Module und HUD:** Eingebaute Client-Module bieten optionale Spiel- und Anzeige-Funktionen. Der HUD-Editor ordnet sichtbare Anzeigen auf dem Bildschirm an. Diese internen Module sind nicht automatisch eine öffentliche Addon-API.
- **Cosmetics:** Outfits, Capes und Emotes ändern die Darstellung. Welche Inhalte verfügbar oder sichtbar sind, hängt von Konto, Client-Version und Shop-Angebot ab.
- **Social-Funktionen:** Freunde, In-Game-Chat, Hosted Worlds und McReal verbinden Spielende. Hosted Worlds ermöglicht, eine eigene Welt für Freunde zugänglich zu machen; prüfe die aktuellen Dienstbedingungen und nicht die Annahme, es handle sich um einen dauerhaft laufenden eigenen Server.
- **Minigames:** Die Website zeigt kleine integrierte Arcade-Spiele als Teil des Ökosystems.
- **NoRisk+:** Optionales kostenpflichtiges Angebot für Zusatzvorteile. Preise und Vorteile können sich ändern; die Grundnutzung des Launchers/Clients ist laut offizieller Website kostenlos.

Die [Hauptseite](https://norisk.gg/) beschreibt die aktuell angebotenen Funktionen. Die [Download-Seite](https://norisk.gg/downloads) listet unterstützte Desktop-Systeme. Für technische Addons sind diese Produktbeschreibungen eine Orientierung, keine API-Dokumentation.

## Vier Dinge, die oft verwechselt werden

| Begriff | Bedeutung |
|---|---|
| Launcher | Installiert/verwaltet Profile und startet das ausgewählte Spiel. |
| NRC Java Client | Der clientseitige Code, der mit Minecraft läuft und NRC-Funktionen bereitstellt. |
| Fabric-Mod | Ein separat ladbares JAR mit Fabric-Metadaten. Eine NRC-Integration kann zusätzlich erforderlich sein. |
| NRC-Konfigurationsdaten | JSON-/Datenbeiträge wie Modpacks, Übersetzungen oder Server-Mappings; sie implementieren keine Java-Modul-Logik. |

Die Begriffe **Launcher-Profil**, **NRC-Version**, **Minecraft-Version**, **Fabric Loader** und **Standalone/Mini** beschreiben verschiedene Achsen. Ein Addon muss ermitteln bzw. dokumentieren, welche Kombination es tatsächlich unterstützt.

## Was bedeutet „eigenes Modul“?

Ein Fabric-Mod kann eigene Funktionen und Menüs mitbringen, ohne ein NRC-internes Module zu sein. Wenn es darüber hinaus als Karte/Schalter im NRC-Modulmenü erscheinen, HUD-Positionierung nutzt oder NRC-Werte/Editor-Widgets einbettet, greift es üblicherweise auf NRC-interne Klassen oder Community-Integrationspunkte zu. Diese können sich mit Updates ändern.

Öffentliche Nutzer- oder Server-APIs sind davon getrennt. [NoRisk Server API Docs](https://docs.norisk.gg/) erklären serverseitige Bukkit-/Paper-/Velocity-/Fabric-Integrationen und Netzwerkfunktionen. Das ist nicht automatisch eine API zum Erstellen clientseitiger NRC-Module.

## Wie bekommt man NRC?

Installiere den Launcher ausschließlich über die [offizielle Download-Seite](https://norisk.gg/downloads). Erstelle darin ein Profil mit der gewünschten Minecraft-Version und starte es einmal. So werden Dateien und Abhängigkeiten des Profils durch den Launcher eingerichtet. Verwende für Entwicklung eine Kopie bzw. ein separates Testprofil, nicht dein Hauptprofil.

Baue keine Anleitung darauf auf, einen proprietären NRC-JAR von einer fremden Seite herunterzuladen. Wenn ein Projekt eine NRC-Bibliothek zum Kompilieren benötigt, muss es auf eine offiziell bereitgestellte Maven-Abhängigkeit oder auf eine vom Nutzer lokal bereitgestellte, rechtmäßig nutzbare Datei verweisen. Prüfe die aktuelle Lizenz und Verteilungsbedingungen.

## Versions- und Profilvarianten

NRC unterstützt verschiedene Minecraft-Versionen und Profile; Mini-, Standalone- und Launcher-Setups können aber unterschiedliche Mod-Mengen oder Einstiegspunkte haben. Das Vorhandensein einer NRC-Klasse in einem Profil beweist nicht, dass sie in allen Profilen existiert.

Deshalb:

- Nenne eine konkrete Zielkombination wie `Minecraft-Version + Loader + NRC-Build`.
- Behandle NRC-Integrationen als optional, falls die Mod auch außerhalb von NRC starten soll.
- Prüfe Fähigkeiten zur Laufzeit und biete bei fehlenden NRC-Klassen eine verständliche Erklärung.
- Teste Launcher-Profil, Standalone und Mini separat, wenn du diese Varianten unterstützen willst.
- Behaupte keine universelle Update-Kompatibilität allein aufgrund eines erfolgreichen Builds.

## Wo passt mein Beitrag hin?

- **Eigene Mod-Logik:** dein eigenes GitHub-Repository und Release-Artefakt.
- **Modpack/Installationskonfiguration:** offizielles Repository [NoRiskClient/nrc-configs](https://github.com/NoRiskClient/nrc-configs), Beitrag gemäß `CLIENT_CONTRIBUTING.md`.
- **Servername, Icon, Hintergrund, Metadaten:** [NoRiskClient/nrc-server-mappings](https://github.com/NoRiskClient/nrc-server-mappings).
- **NRC selbst oder offizielle Schnittstelle:** offizielle [NoRiskClient-Projekte](https://github.com/NoRiskClient) bzw. Issue-/Support-Kanäle. Ein Addon-JAR ersetzt keinen Pull Request am Client.

Server-Mappings enthalten Metadaten über Server und können clientseitige Modullisten konfigurieren; sie sind weder Server-Plugins noch Addon-Code.

