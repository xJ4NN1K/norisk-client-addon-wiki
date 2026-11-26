# 2. Entwicklungsumgebung und Abhängigkeiten

## Benötigte Werkzeuge

1. **JDK:** Installiere ein JDK, das zur Minecraft-Zielversion und zum offiziellen Fabric-Projekt-Template passt. Die benötigte Java-Hauptversion kann sich je nach Minecraft-Version unterscheiden. `java -version` und `./gradlew --version` helfen beim Prüfen.
2. **IntelliJ IDEA Community** (oder eine Java-IDE mit Gradle-Unterstützung).
3. **Git**, um Änderungen nachvollziehbar zu verwalten.
4. **Fabric Loom / Gradle Wrapper:** Nutze Wrapper-Dateien aus einem gepflegten Fabric-Template. Der Wrapper lädt die im Projekt festgelegte Gradle-Version; normalerweise musst du Gradle nicht global installieren.
5. **Minecraft/Fabric-Dokumentation:** Fabric Developer Guides und Fabric Wiki, siehe [Quellen](QUELLEN.md).

## Projekt anlegen

- Erzeuge ein frisches Projekt aus dem offiziellen Fabric-Template, wähle Minecraft- und Loader-Version und importiere es als Gradle-Projekt.
- Baue zuerst das unveränderte Template. Dadurch erkennst du JDK- oder Gradle-Probleme, bevor NRC-spezifischer Code hinzukommt.
- Setze eindeutige Werte für `mod id`, Version, Maven-Koordinaten/Paket und `fabric.mod.json`.
- Stelle sicher, dass es eine clientseitige Mod ist (`environment: "client"` bzw. nur Client-Entrypoints, sofern passend zum Template).

## NRC-Abhängigkeit finden

Beginne ohne NRC-interne Abhängigkeit und nutze Fabric-APIs, sofern sie reichen. Falls du NRC-Klassen direkt referenzierst:

1. Suche nach einer offiziellen und versionierten NRC-Maven-Artefaktquelle in aktuellen NoRisk-Projekten/Dokumentation.
2. Prüfe, dass die Abhängigkeit für die exakte Minecraft-/NRC-Version existiert und du sie weitergeben darfst.
3. Wenn keine veröffentlichte SDK-Abhängigkeit existiert, behandle ein lokales Compile-JAR als private Entwicklungsabhängigkeit. Schreibe eine klare Anleitung, wie der Nutzer es aus einer offiziellen Installation beziehen darf; committe und veröffentliche es nicht ungeprüft.
4. Trenne `compileOnly`/`modCompileOnly` (nur zum Kompilieren) von `include`/Bundling. Ein NRC-JAR oder eine andere Mod darf nicht versehentlich doppelt in dein Artefakt gepackt werden.
5. Deklariere gewöhnliche Bibliotheken korrekt als Abhängigkeit. Prüfe Lizenz, Version, Loader-Kompatibilität und ob NRC sie bereits mitbringt.

Die konkret nötigen Abhängigkeiten hängen davon ab, welche Klasse/Funktion du nutzt. Zum Beispiel ist owo-lib nur erforderlich, wenn dein Code tatsächlich dessen UI-Elemente verwendet. Fabric API oder Kotlin ebenfalls nur dann, wenn dein Mod sie nutzt. Verlasse dich nicht auf eine statische Liste von Bibliotheken aus einem älteren Beispielprojekt.

## Gradle-Grundbefehle

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
.\gradlew.bat clean
```

`runClient` richtet eine Entwicklungsinstanz ein, falls das Template die passende Fabric-Konfiguration enthält. Das ist nützlich, aber kein Ersatz für einen Test im echten NRC-Profil. Das fertige JAR liegt üblicherweise unter `build/libs/`; kontrolliere den tatsächlichen Namen im Build.

## Entwicklungs- und Laufzeitabhängigkeiten

- **Minecraft-/Fabric-Klassen:** passend zur Zielversion, vom Loom-Projekt verwaltet.
- **Fabric API:** nur falls verwendet; Version muss zur Minecraft-Version passen.
- **NRC-Integrationsklassen:** nur falls verwendet und für den Ziel-Build verfügbar.
- **owo-lib/Kotlin/weitere Mod-Libraries:** nur falls verwendet. Prüfe, ob die Laufzeit sie in jedem unterstützten NRC-Profil mitbringt oder ob deine Mod sie deklarieren muss.
- **Mixin:** nur wenn ein vorhandener API-Hook nicht reicht. Mixins an interne NRC-Klassen sind besonders update-empfindlich.

`fabric.mod.json` beschreibt Mod-ID, Entry-Points, Abhängigkeiten und Mixins. Eine falsche Minecraft-/Loader-Bedingung kann verhindern, dass das JAR geladen wird. Richte die dortigen `depends` nach der tatsächlich getesteten Matrix aus.

## Sicherheit und Lizenz

Lade Build-Abhängigkeiten aus offiziellen Projekt-Repositories. Prüfe fremde JARs vor dem Einsatz. Kopiere keine NRC- oder Drittanbieter-Binaries in öffentliche Repositories, wenn Nutzungs- und Verteilungsrechte nicht eindeutig geklärt sind. GitHub-Secrets, Tokens und private Zugangsdaten gehören niemals in Quellcode oder Git-Historie.

