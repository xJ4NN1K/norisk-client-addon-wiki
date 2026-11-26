# 5. Kompatibilität, Tests und Fehlersuche

## Kompatibilität ist eine Matrix

Notiere pro Release genau, was du geprüft hast:

| Minecraft | Loader | NRC-Profil/Build | Modversion | Resultat |
|---|---|---|---|---|
| Beispiel: 1.x.y | Fabric x.y.z | NRC-Build ... | 0.1.0 | Start / Modul / HUD / Update getestet |

Launcher-Profile, Standalone und Mini können unterschiedliche Clientbestandteile laden. Auch gleich benannte Klassen können sich ändern. „Startet auf mehreren Versionen“ ist erst belegt, wenn jede Kombination getestet wurde.

## Laufzeitprüfung

Wenn die Mod auch außerhalb von NRC geladen werden kann:

1. Deklariere erforderliche Minecraft-/Fabric-Abhängigkeiten akkurat.
2. Vermeide harte Klassenreferenzen auf NRC im gemeinsamen Entrypoint, falls die NRC-Integration optional ist.
3. Prüfe zur Laufzeit, ob die erwartete NRC-Funktion existiert und zu deiner unterstützten API-Version passt.
4. Fehlt sie, deaktiviere nur die NRC-spezifische Integration und gib eine klare Meldung aus.
5. Unterstütze nie eine Kombination nur deshalb, weil eine Klasse mit passendem Namen gefunden wurde.

Ein Fallback-Popup ersetzt keine technischen Checks und sollte nicht bei jedem Tick erneut aufgehen.

## Testplan pro Zielversion

- Projekt baut sauber mit `gradlew build`.
- Fabric-Mod-Metadaten und Loader-Auflösung stimmen.
- Start in einer frischen Fabric-Entwicklungsinstanz.
- Start in einem frischen NRC-Launcher-Profil.
- Start in jeder beanspruchten Standalone-/Mini-Variante.
- Modul lässt sich mehrfach aktivieren/deaktivieren, ohne doppelte Listener.
- Welt betreten/verlassen, Disconnect, Hauptmenü, Reload und Neustart.
- UI bei unterschiedlichen GUI-Skalierungen/Fenstergrößen.
- Konfiguration fehlt, ist leer oder enthält einen ungültigen/alten Wert.
- Kombination mit den tatsächlich vorausgesetzten Mods.
- Auf der nächsten Zielversion erneut bauen und testen; keine Kompatibilität voraussetzen.

Bei jedem Build-Update prüfe besonders Mixin-Logs, Methodensignaturen und Mod-Metadaten. Halte reproduzierbare Testprofile und dokumentiere die getesteten Builds.

## Häufige Fehler

| Symptom | Typische Ursache | Nächster Schritt |
|---|---|---|
| `MixinTargetAlreadyLoadedException` | Zielklasse wurde vor Mixin-Vorbereitung geladen. | Direkte/statische NRC-Referenzen beim frühen Entrypoint entfernen; Initialisierung verzögern; tatsächlichen Mixin-Startablauf prüfen. |
| `Mixin apply failed` oder Injection nicht gefunden | Zielmethode, Descriptor, Mapping oder Injection-Punkt hat sich geändert. | Stacktrace lesen; Zielklasse im passenden Build dekompilieren/prüfen; Mixin für nicht unterstützte Builds deaktivieren, nicht blind Injection-Regeln lockern. |
| `NoClassDefFoundError` | Abhängigkeit fehlt im Profil oder wurde falsch deklariert. | `fabric.mod.json`, Loader-Log und Abhängigkeitsauflösung prüfen; nur nötige Runtime-Abhängigkeiten deklarieren. |
| `NoSuchMethodError` / `NoSuchFieldError` | Addon gegen andere API-/NRC-Version gebaut als zur Laufzeit vorhanden. | API-Versionen angleichen; Versionsprüfung/Fallback hinzufügen; Matrix aktualisieren. |
| Addon erscheint nicht im Modulmenü | Registrierung fehlt/zu früh, falsche Kategorie oder NRC-Modul-API nicht verfügbar. | Entrypoint-Log, Registrierung und konkrete Klassen im Zielbuild prüfen. |
| ADDON-Badge fehlt, Spiel startet aber | Optionaler Mixin-Hook hat kein Ziel getroffen oder UI-Implementierung geändert. | Logausgabe und Bildschirm nach Update prüfen; Badge-Hook für den Build anpassen. |
| Kotlin-/owo-Klassen fehlen | Profil enthält Laufzeitbibliothek nicht, Version inkompatibel, oder Dependency fehlt. | Projektabhängigkeiten und NRC-Profil prüfen; nicht doppelt bundlen. |
| Funktion läuft im Menü, crasht in Welt/Disconnect | Welt-/Spielerwert war `null` oder alter Listener lief weiter. | Lifecycle-Übergänge und Listener-Aufräumen testen. |
| Zwei Mods überschreiben gleiche internals | Mixin-Konflikt oder gegenseitige UI-Manipulation. | Integrationen minimieren; Konflikt mit sauberem Profil reproduzieren; kooperative APIs bevorzugen. |

## Crashbericht sammeln

Für einen Fehlerbericht notiere Modversion, Minecraft-Version, Loader, NRC-Profil/Build, Reproduktionsschritte und den relevanten Stacktrace. Entferne Tokens, Accountdaten und private Serverinformationen vor dem Teilen. Ein Screenshot allein reicht für Mixin- und Klassenladefehler meistens nicht.

