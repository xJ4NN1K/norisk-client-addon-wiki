# 3. Ein eigenes NRC-Modul erstellen

## Vorbemerkung

Ein Modul im internen NRC-Menü ist keine zugesicherte öffentliche API. Die folgenden Namen (`Module`, `ModuleCategory`, `ModuleProvider`) stammen aus vorhandenen Community-Beispielen und sind **keine Garantie**, dass Konstruktoren oder Registrierung in deinem NRC-Build gleich sind. Öffne die tatsächlich für deine Zielversion verfügbaren Klassen/Beispiele und gleiche jede Signatur ab.

Wenn es keine freigegebene Modul-API gibt, entwickle zunächst ein normales Fabric-Client-Modul mit eigener Konfiguration und eigener Oberfläche. Das ist robuster, aber es erscheint nicht automatisch in NRCs Modulmenü.

## Ablauf

1. **Klein anfangen:** eine harmlose Client-Funktion wählen, z. B. Anzeige eines Zustands oder lokaler HUD-Text.
2. **Client-Entrypoint erstellen:** Fabric ruft deinen Client-Entrypoint beim Laden auf.
3. **NRC-Verfügbarkeit erkennen:** keine NRC-Klasse in statischen Initialisierern laden, wenn das Mod auch ohne NRC starten soll.
4. **Modul instanziieren und registrieren:** erst wenn NRC sicher initialisiert ist und du die echte Zielversionssignatur kennst.
5. **Aktivieren/deaktivieren:** Listener/Callbacks beim Ausschalten sauber abmelden. Dauerhafte Tick-Listener dürfen nicht aktiv bleiben.
6. **Aufräumen:** beim Weltwechsel/Disconnect Referenzen und Ressourcen freigeben.
7. **Testen:** zuerst Vanilla/Fabric oder Development Client, danach jede unterstützte NRC-Profilvariante.

## Schematischer Modulaufbau

Das ist absichtlich ein **Strukturbeispiel**, kein direkt kompilierbarer API-Vertrag:

```java
public final class BeispielModul /* extends NRC-Module, falls API vorhanden */ {
    private boolean aktiviert;

    public void beimAktivieren() {
        aktiviert = true;
        // Listener genau einmal registrieren
    }

    public void beimDeaktivieren() {
        aktiviert = false;
        // Listener/Tasks wieder abmelden
    }
}
```

In vorhandenen NRC-Integrationen kann die Basisklasse beispielsweise `gg.norisk.ui.api.module.Module` heißen und die Registrierung über einen `ModuleProvider` erfolgen. Übernimm keinen konkreten Konstruktor oder `register(...)`-Aufruf, bevor du ihn im passenden Artefakt bzw. in einer offiziellen aktuellen Quelle geprüft hast.

### Eigenschaften eines guten Moduls

- eigener stabiler Identifier, kurzer Anzeigename und verständliche Beschreibung
- passende Kategorie; HUD-Einträge nur, wenn sie wirklich verschiebbar sein sollen
- eindeutiger Standardzustand und gespeicherte Benutzereinstellungen
- idempotente Enable-/Disable-Logik (mehrfaches Aktivieren erzeugt keine doppelten Listener)
- keine unnötigen Netzwerkzugriffe und keine Aktionen, die Serverregeln umgehen
- defensive Behandlung von `Minecraft.getInstance()`, Welt/Spieler-Nullzuständen und Disconnects

## Registrierung und frühe Klassenladung

Ein häufiger Fehler bei Mixins oder NRC-Addons ist `MixinTargetAlreadyLoadedException`: Ein Ziel wurde schon geladen, bevor der Mixin vorbereitet wurde. Das kann passieren, wenn dein Entrypoint NRC-UI-/Modulklassen zu früh direkt referenziert.

Praktische Maßnahmen:

- Halte den normalen Fabric-Entrypoint frei von statischen NRC-Feldtypen und statischer NRC-Initialisierung.
- Verzögere optionale NRC-Anbindung bis die Client-Seite bereit ist. Ein allgemeiner Client-Tick-Callback kann als später Einstieg dienen, muss aber gegen Mehrfachaufrufe geschützt und danach wieder abgemeldet werden.
- Wo nötig, isoliert ein kleines Bridge-Klassenpaar die optionalen NRC-Referenzen. Lade die Bridge erst nach dem Laufzeitcheck.
- Ein reflektiver `Class.forName`-Check kann Klasse-vorhanden erkennen, beweist aber nicht API-Kompatibilität. Danach erst benötigte Methoden/Versionen prüfen.
- Greife nicht aus einem beliebigen Hintergrundthread auf Minecraft-/NRC-UI zu.

Diese Muster reduzieren Risiken, garantieren aber keine Kompatibilität mit Änderungen im Client.

## Optionaler NoRisk-only-Hinweis

Wenn die Mod ohne NRC zwar startet, ihr Kernfeature dort aber nicht funktionieren kann, zeige einen einzelnen verständlichen Hinweis, statt beim Start abzustürzen. Bestehende Nutzerwünsche für ein einheitliches Popup sehen drei Aktionen vor:

- **NoRisk Client herunterladen** → `https://norisk.gg/`
- **Nicht erneut anzeigen** → lokale Präferenz speichern
- **Okay** → Dialog schließen

Nutze für alle Addons dasselbe Design aus dem lokalen Beispielordner `norisk-popup-example`. Der Dialog muss ohne NRC-Klassen selbst funktionieren; keine wiederholte Anzeige bei jedem Tick. Ein „nicht erneut anzeigen“ ist eine lokale Nutzereinstellung und darf nur den Hinweis unterdrücken, nicht die Kompatibilitätsprüfung.

## ADDON-Badge im Modulmenü

Gewünschtes Community-Design: eigene Modulkarte mit rotem/farbigen `ADDON`-Hinweis an der Stelle, wo eingebaute Module z. B. `NEW` anzeigen. Das ist eine UI-Änderung am internen NRC-Menü und damit versionsempfindlich.

Wenn du dafür einen Mixin brauchst:

- verwende nur die Zielklasse/Methode aus dem passenden NRC-Build;
- prüfe den Injection-Punkt und die tatsächliche Textkomponente;
- vermeide eine fragile Änderung anhand von Bildschirmtext, wenn es einen stabilen Hook gibt;
- behandle optionale Targets bewusst. `require = 0` verhindert einen harten Startabbruch bei fehlendem Injection, kann aber auch eine kaputte Integration verstecken; logge daher nachvollziehbar und teste, ob das Badge tatsächlich erscheint;
- `@Pseudo` und `remap = false` sind nur für nicht remappte optionale Ziele sinnvoll. Sie sind keine pauschale Lösung für alle Mixins.

Wenn die Karte nur über ein internes NRC-API registriert werden kann, prüfe, ob dieses API bereits ein Badge/Label-Feld anbietet, bevor du UI-Mixins schreibst.

