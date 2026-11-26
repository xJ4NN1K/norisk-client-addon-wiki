# 4. UI, Werte, HUD und Konfiguration

## UI

Nutze zuerst Fabric/Minecrafts öffentliche Screen- und Widget-APIs oder die offiziell dokumentierte UI-Bibliothek, die dein Zielprofil sicher enthält. Ein NRC-Modul kann je nach Build eigene owo-lib-basierte Editor-Callbacks besitzen; konkrete Methoden wie `buildCustomUi()` und Komponentenklassen sind an die NRC-/owo-lib-Version gebunden.

UI-Grundregeln:

- UI-Komponenten nur auf dem Client-Thread ändern.
- Screenwechsel über die Minecraft-Client-API ausführen.
- Bei geschlossenem Screen Listener und Ressourcen lösen.
- Texte übersetzbar machen statt nur deutsche Literale zu verwenden.
- Nach Updates gezielt prüfen: Buttons, Fokus, Skalierung, kleine Fenster, Screen-Stack und HUD-Editor.
- Gib externen Dialogen einen verständlichen Titel, Erklärung und erreichbare Tastaturaktionen.

## Modulwerte

Schalter, Zahlenbereiche, Enum-Auswahl und Farben sind übliche Einstellungsarten. Ob NRC dafür `BooleanValue`, `NumericValue`, `EnumValue` oder Farbklassen anbietet, hängt von der Version ab. Werte sollten einen Namen, gültige Grenzen, Standardwert und eine kurze Beschreibung haben. Eingaben validieren und außerhalb des erlaubten Wertebereichs begrenzen.

Vermeide Reflection in private Felder wie `Value.module`, sofern es eine öffentliche Registrierungsfunktion gibt. Private Feldnamen können ohne Vorwarnung wechseln und auf eine Sicherheitsrichtlinie/Modulinkompatibilität stoßen.

## Konfiguration speichern

Für eigene Moddaten gibt es zwei Ansätze:

- Fabric-Config-API bzw. eine passende öffentliche Config-Bibliothek;
- kleine JSON-Datei im eigenen Unterordner des Minecraft-Config-Verzeichnisses.

Speichere nur eigene Einstellungen und lade mit Defaults, falls Datei oder Feld fehlen. Schreibe Dateien nicht bei jedem Tick. Nutze atomisches Ersetzen, wenn die Bibliothek es unterstützt. Fehler melden, ohne das Spiel zu beenden. Keine Zugangsdaten in Klartextkonfiguration speichern.

Wenn das NRC-Value-System deine Werte automatisch verwaltet, verwende den vorgesehenen Mechanismus, statt dieselben Einstellungen doppelt in einer privaten NRC-Datei zu schreiben.

## HUD

Ein eigenes HUD sollte seine Koordinaten, Skalierung und Sichtbarkeit speichern und bei verschiedenen Auflösungen testen. Berücksichtige GUI-Scale und sichere Bildschirmränder. Registriere den HUD-Eintrag über eine dokumentierte API, falls verfügbar. Ersetze Vanilla-HUD-Elemente nur, wenn der Nutzer diese Änderung wirklich erwartet.

In vorhandenen NRC-Beispielen werden `ModuleProvider` und `HudComponentRegistry` erwähnt. Das ist ein versionsgebundener Integrationshinweis. Suche nach gleichwertigen Klassen im exakten Zielbuild und starte ohne HUD-Referenz, wenn das Profil diese Registrierung nicht bereitstellt.

## Keybinds und Events

- Keybind nur einmal registrieren.
- Keine teuren Berechnungen oder Datei-/Netzwerkzugriffe pro Tick.
- Events beim Deaktivieren abmelden, falls die API keine automatische Lifecycle-Verwaltung bietet.
- Auf den Unterschied zwischen Welt-, Spieler-, Menü- und Verbindungszustand achten.
- Wenn mehrere Profile oder Loader angesprochen werden, jede Event-Bibliothek separat prüfen.

## Debugging

Nutze den Logger deiner Mod statt `System.out.println`. Logge den Mod-Identifier und die erkannte Minecraft-, Loader- und NRC-Version. Schreibe keine privaten Accountdaten oder vollständigen sensiblen Spiel-/Netzwerkdaten ins Log.

