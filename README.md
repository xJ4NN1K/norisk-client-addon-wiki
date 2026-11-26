# NoRisk Client Addon- und Modding-Wiki

Ein deutschsprachiger Praxisleitfaden für eigene Fabric-Mods, die mit dem NoRisk Client (NRC) zusammenarbeiten. Er erklärt, was NRC ist, wie du eine Entwicklungsumgebung vorbereitest, Module und HUDs entwirfst, NRC-APIs vorsichtig anbindest und Mods testest und verteilst.

> **Wichtig:** NoRisk Client ist ein sich ändernder Client mit mehreren Minecraft-Versionen und Profilvarianten. Eine interne NRC-Klasse ist keine stabile öffentliche Addon-API. Codebeispiele für NRC-Module sind deshalb versionsgebundene Startpunkte und müssen gegen genau die NRC-Version geprüft werden, für die du baust. „Funktioniert in jeder Version“ lässt sich ohne offizielle, versionierte Addon-Schnittstelle nicht versprechen.

## Einstieg

1. [NRC verstehen](docs/01-nrc-verstehen.md)
2. [Werkzeuge und Abhängigkeiten installieren](docs/02-entwicklungsumgebung.md)
3. [Ein eigenes Modul erstellen](docs/03-eigenes-modul.md)
4. [UI, Werte, HUD und Konfiguration](docs/04-ui-hud-konfiguration.md)
5. [Kompatibilität, Tests und Fehlersuche](docs/05-kompatibilitaet-und-fehlersuche.md)
6. [Veröffentlichen und rechtliche Hinweise](docs/06-veroeffentlichen.md)
7. [Offizielle Quellen und lokale Wissensbasis](docs/QUELLEN.md)

## Schnellstart

- Installiere ein JDK passend zur Minecraft-/Fabric-Zielversion, Git und IntelliJ IDEA Community.
- Starte von einem Fabric-Mod-Template und nutze dessen `gradlew`-Wrapper. Fabric Loom, Loader und API-Versionen gehören zusammen und müssen auf die Zielversion abgestimmt sein.
- Lege NRC-spezifische Abhängigkeiten nur aus einer nachvollziehbaren, rechtmäßig nutzbaren Quelle bei. Prüfe zuerst, ob der gewünschte Effekt mit der normalen Fabric-API oder einer öffentlichen NRC-Schnittstelle möglich ist.
- Baue mit `./gradlew build` bzw. `.\gradlew.bat build` und teste in einer separaten NRC-Instanz derselben Minecraft-Version.
- Füge NRC-Klassen nicht beim frühen Mod-Start unbedacht in statische Felder oder Mixin-Targets ein. Sie können geladen werden, bevor optionale Ziele verfügbar sind.

## Was dieses Wiki abdeckt

- NRC Launcher, Profile, Java-Client, eingebaute Module, Cosmetics und weitere sichtbare Funktionen auf hoher Ebene.
- Eigene Fabric-Client-Mods, die sich in NRC integrieren.
- NRC-Module, HUDs, Menüs, Badges und optionale Dialoge als versionsabhängige Community-Integrationen.
- NRC-Konfigurationsdaten und Server-Mappings als getrennte Beitragswege.

Es ist **kein** offizieller SDK-Vertrag und ersetzt keine Dokumentation, Lizenz oder aktuelle Build-Dateien. Bei widersprüchlichen Details gelten die offiziellen Projektquellen und die tatsächliche Zielversion.

## GitHub

Repository: [xJ4NN1K/norisk-client-addon-wiki](https://github.com/xJ4NN1K/norisk-client-addon-wiki)

Zum lokalen Klonen: `git clone https://github.com/xJ4NN1K/norisk-client-addon-wiki.git`


