# 📜 NoRisk Client Addon -- Complete Guide & Documentation

Welcome to the official **NoRisk Addon Template**! This template
provides a clean foundation for developing your own addons and modules
for the **NoRisk Client (NRC)** on Minecraft 26.3+ (Fabric).

------------------------------------------------------------------------

## 📁 1. Project Structure Overview

    NoRisk-Addon Template/
    ├── ANLEITUNG.md                 <-- This quick reference with all explanations
    ├── README.md                    <-- Quick overview
    ├── build.gradle                 <-- Fabric Loom build configuration
    ├── gradle.properties            <-- Mod name, version, Minecraft version
    ├── settings.gradle              <-- Gradle project name
    ├── gradlew / gradlew.bat        <-- Gradle wrapper for building directly
    │
    ├── libs/                        <-- IMPORTANT: Contains NoRisk Client & owo-lib
    │   ├── norisk-client.jar        <-- NoRisk Client core
    │   ├── nrc-owolib.jar           <-- NoRisk UI framework (owo-lib)
    │   └── kotlin-stdlib.jar        <-- Kotlin runtime
    │
    └── src/main/
        ├── java/gg/norisk/addon/template/
        │   ├── TemplateAddon.java          <-- Main entry point (ClientModInitializer)
        │   ├── config/
        │   │   ├── TemplateConfig.java     <-- Loads and saves settings as JSON
        │   │   └── SampleEnum.java         <-- Example enum for mode selection
        │   ├── module/
        │   │   └── TemplateModule.java     <-- Your NoRisk module (settings, values, lifecycle)
        │   └── ui/
        │       ├── TemplateModuleEditor.java <-- THE CODE for buttons, toggles, sliders & UI
        │       └── ExampleScreen.java      <-- Custom full-screen GUI (opened by a button)
        │
        └── resources/
            ├── fabric.mod.json             <-- Mod metadata for Fabric & NoRisk
            ├── icon.png                    <-- Mod icon
            └── assets/norisk-addon-template/
                ├── lang/                   <-- Translation files (de_de.json, en_us.json)
                └── textures/gui/
                    └── module_icon.png     <-- Module icon in the NoRisk module list

------------------------------------------------------------------------

## 🚀 2. Quick Start: Build & Test the Mod

The project includes the Gradle wrapper. You do not need to install
Gradle separately:

1.  **Build the project:** Open a terminal / PowerShell in this folder
    and run:

    ``` powershell
    .\gradlew.bat build
    ```

2.  **Find the built mod:** After the build, the finished file is
    located at:

        build/libs/norisk-addon-template-1.0.0.jar

3.  **Install it in NoRisk Client:** Simply copy the `.jar` file into
    your NoRisk profile's `.minecraft/mods` folder.

4.  **Open it in the game:** By default, press **Right Shift** in NoRisk
    Client to open the NoRisk module menu. Your module will appear in
    the selected category!

------------------------------------------------------------------------

## 🧩 3. How Do I Create a New Module?

Every NoRisk module extends the `gg.norisk.ui.api.module.Module` class:

``` java
package gg.norisk.addon.template.module;

import gg.norisk.ui.api.module.Module;
import gg.norisk.ui.api.module.ModuleCategory;
import gg.norisk.addon.template.TemplateAddon;

public class MyNewModule extends Module {

    public MyNewModule() {
        super(
            "My Module Name",               // Name in the NoRisk menu
            ModuleCategory.QUALITY_OF_LIFE,  // Category
            true,                            // isToggleable (can be enabled/disabled)
            false,                           // isDraggable (e.g. a freely movable HUD element)
            true                             // Initial state (enabled/disabled)
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Called when the player enables the module in the menu
    }

    @Override
    public void onDisable() {
        super.onDisable();
        // Called when the player disables the module
    }
}
```

### Verfügbare Modul-Categoryn (`ModuleCategory`):

-   `ModuleCategory.VISUAL` -- Visual features, shaders, particles,
    glint.
-   `ModuleCategory.QUALITY_OF_LIFE` -- Useful client features &
    automation.
-   `ModuleCategory.HUD` -- On-screen displays (e.g. ArmorHUD, FPS,
    CPS).
-   `ModuleCategory.PVP` -- Combat features, hitboxes, crosshair, etc.
-   `ModuleCategory.COSMETIC` -- Cosmetic changes, capes, skins.
-   `ModuleCategory.HIDDEN` -- Invisible background modules.

### Register the module with NoRisk:

In `TemplateAddon.java` (der Hauptklasse):

``` java
@Override
public void onInitializeClient() {
    // Registers the module in the NRC module browser
    ModuleProvider.Companion.register(MODULE);
}
```

------------------------------------------------------------------------

## ⚙️ 4. Register Values & Settings (`Value<T>`)

NoRisk Client provides a powerful value system. Since NoRisk is written
in Kotlin, values are registered from Java using the helper method
`registerVal(...)`:

``` java
private <T extends Value<?>> T registerVal(String name, T val) {
    val.setName(name);
    try {
        Field f = Value.class.getDeclaredField("module");
        f.setAccessible(true);
        f.set(val, this);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    getValues().add(val);
    return val;
}
```

### The Most Important Value Types:

#### 1. Toggle (`BooleanValue`):

``` java
BooleanValue toggleVal = registerVal("myToggle",
    new BooleanValue(true, "Enable feature", Component.literal("Tooltip text"), val -> {
        // Callback when changed
        syncToConfig();
        return Unit.INSTANCE;
    })
);
```

#### 2. Numbers & Sliders (`NumericValue`):

``` java
// Integer range (e.g. 0 to 100 with step 1)
NumericValue intVal = registerVal("myInt",
    new NumericValue(50, new kotlin.ranges.IntRange(0, 100), 1, "Volume", null, onChange)
);

// Float / decimal range (e.g. 0.1f to 5.0f with step 0.1f)
NumericValue floatVal = registerVal("myFloat",
    new NumericValue(1.0f, kotlin.ranges.RangesKt.rangeTo(0.1f, 5.0f), 0.1f, "Speed", null, onChange)
);
```

#### 3. Selection / Mode (`EnumValue<T>`):

``` java
EnumValue<SampleEnum> enumVal = registerVal("meinMode",
    new EnumValue<>(SampleEnum.OPTION_A, "Mode", null, onChange, v -> true)
);
```

#### 4. Color Selection (`ColorAttributeValue`):

``` java
ColorAttributeValue colorVal = registerVal("myColor",
    new ColorAttributeValue(new Color(0x00C8FF), false, "Accent color", null, onChange)
);
```

------------------------------------------------------------------------

## 🎨 5. THE CODE: Create Custom Buttons & UIs

If your module overrides `buildCustomUi()`:

``` java
@Override
public Function1<FlowLayout, Unit> buildCustomUi() {
    return panel -> {
        TemplateModuleEditor.build(panel);
        return Unit.INSTANCE;
    };
}
```

...NoRisk builds the interface through `TemplateModuleEditor.java`. Here
are the exact code building blocks:

### 🔘 A. Standard Click Button (Perform an Action)

``` java
public static void actionButton(FlowLayout panel, String label, Runnable action) {
    ButtonComponent btn = UIComponents.button(Component.literal(label), b -> action.run());
    
    // Apply NRC standard styling (full width, 20px height)
    btn.horizontalSizing(Sizing.fill(100));
    btn.verticalSizing(Sizing.fixed(20));
    btn.margins(Insets.bottom(1));
    
    panel.child(btn);
}

// Usage:
actionButton(panel, "§e⚡ Test-Aktion ausführen", () -> {
    System.out.println("Button was clicked!");
});
```

------------------------------------------------------------------------

### ✔ B. NoRisk Toggle Button (On/Off with ✔/✗)

``` java
public static void toggle(FlowLayout panel, String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
    ButtonComponent[] ref = {null};
    Runnable refresh = () -> {
        if (ref[0] == null) return;
        boolean active = Boolean.TRUE.equals(getter.get());
        ref[0].setMessage(Component.literal((active ? "§a✔" : "§c✗") + " §7" + label));
    };

    ButtonComponent btn = UIComponents.button(Component.literal(""), b -> {
        setter.accept(!Boolean.TRUE.equals(getter.get()));
        refresh.run();
    });

    btn.horizontalSizing(Sizing.fill(100));
    btn.verticalSizing(Sizing.fixed(20));
    btn.margins(Insets.bottom(1));

    ref[0] = btn;
    panel.child(btn);
    refresh.run();
}

// Usage:
toggle(panel, "Particle effect", () -> config.exampleToggle, val -> {
    config.exampleToggle = val;
    config.save();
});
```

------------------------------------------------------------------------

### 🔄 C. Cycle-Button (Mode durchschalten)

``` java
ButtonComponent[] cycleBtnRef = {null};
Runnable[] refreshCycle = {null};

refreshCycle[0] = () -> {
    if (cycleBtnRef[0] == null) return;
    cycleBtnRef[0].setMessage(Component.literal("§7Mode: §b" + config.sampleEnum.getDisplayName()));
};

ButtonComponent cycleBtn = UIComponents.button(Component.literal(""), btn -> {
    SampleEnum[] values = SampleEnum.values();
    int next = (config.sampleEnum.ordinal() + 1) % values.length;
    config.sampleEnum = values[next];
    config.save();
    refreshCycle[0].run();
});

cycleBtn.horizontalSizing(Sizing.fill(100));
cycleBtn.verticalSizing(Sizing.fixed(20));
cycleBtn.margins(Insets.bottom(1));

cycleBtnRef[0] = cycleBtn;
panel.child(cycleBtn);
refreshCycle[0].run();
```

------------------------------------------------------------------------

### ◀ ▶ D. Numeric Stepper with Minus/Plus Buttons

``` java
public static void numRow(FlowLayout panel, String labelText,
                          Supplier<Float> getter, Consumer<Float> setter,
                          float min, float max, float step, String unit) {

    FlowLayout row = UIContainers.horizontalFlow(Sizing.fill(100), Sizing.fixed(20));
    row.gap(2);
    row.verticalAlignment(VerticalAlignment.CENTER);

    // Text on the left
    LabelComponent label = UIComponents.label(Component.literal("§8" + labelText));
    label.shadow(true);
    label.horizontalSizing(Sizing.expand(1));
    row.child(label);

    LabelComponent[] valRef = {null};
    Runnable refresh = () -> {
        if (valRef[0] == null) return;
        float v = getter.get();
        String txt = (step >= 1.0f) ? String.valueOf(Math.round(v)) : String.format("%.1f", v);
        valRef[0].text(Component.literal("§f" + txt + unit));
    };

    // ◀ Minus button
    ButtonComponent minusBtn = UIComponents.button(Component.literal("§8◀"), b -> {
        setter.accept(Math.max(min, getter.get() - step));
        refresh.run();
    });
    minusBtn.sizing(Sizing.fixed(14), Sizing.fixed(14));

    // Value display
    LabelComponent valLabel = UIComponents.label(Component.literal(""));
    valLabel.shadow(true);
    valLabel.horizontalSizing(Sizing.fixed(44));
    valLabel.horizontalTextAlignment(HorizontalAlignment.CENTER);
    valRef[0] = valLabel;

    // ▶ Plus button
    ButtonComponent plusBtn = UIComponents.button(Component.literal("§8▶"), b -> {
        setter.accept(Math.min(max, getter.get() + step));
        refresh.run();
    });
    plusBtn.sizing(Sizing.fixed(14), Sizing.fixed(14));

    row.child(minusBtn);
    row.child(valLabel);
    row.child(plusBtn);
    panel.child(row);
    refresh.run();
}
```

------------------------------------------------------------------------

### 🖥️ E. Button to Open a Custom GUI / New Screen

``` java
public static void openScreenButton(FlowLayout panel, String label, Supplier<Screen> screenSupplier) {
    ButtonComponent btn = UIComponents.button(Component.literal(label), b -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.gui != null) {
            mc.gui.setScreen(screenSupplier.get());
        }
    });
    btn.horizontalSizing(Sizing.fill(100));
    btn.verticalSizing(Sizing.fixed(22));
    btn.margins(Insets.top(2).bottom(1));
    panel.child(btn);
}

// Usage:
openScreenButton(panel, "§6◈  Open custom GUI  →", () -> {
    Screen currentScreen = Minecraft.getInstance().gui.screen();
    return new ExampleScreen(currentScreen);
});
```

------------------------------------------------------------------------

### 🎨 F. Color Picker (V3ColorSwatch)

``` java
int rgb = config.primaryColor;
V3ColorSwatch swatch = new V3ColorSwatch(new Color(0xFF000000 | (rgb & 0xFFFFFF), true), false, 16);
swatch.onChanged().subscribe(col -> {
    config.primaryColor = col.getRGB() & 0xFFFFFF;
    config.save();
});
panel.child(swatch);
```

------------------------------------------------------------------------

### 🔖 G. Section Headers (Header with Divider)

``` java
public static void section(FlowLayout panel, String title, int color) {
    // Horizontal divider
    FlowLayout divider = UIContainers.horizontalFlow(Sizing.fill(100), Sizing.fixed(1));
    divider.surface(Surface.flat(0x3300C8FF));
    divider.margins(Insets.top(6).bottom(1));
    panel.child(divider);

    // Label
    LabelComponent label = UIComponents.label(Component.literal("§l" + title));
    label.shadow(true);
    label.margins(Insets.bottom(2));
    panel.child(label);
}
```

------------------------------------------------------------------------

## 💾 6. Save & Load Settings (Config)

`TemplateConfig.java` automatically saves everything to
`.minecraft/config/norisk-addon-template.json`:

``` java
// Save settings:
TemplateAddon.CONFIG.exampleToggle = true;
TemplateAddon.CONFIG.save();

// Load on startup:
TemplateConfig config = TemplateConfig.load();
```

------------------------------------------------------------------------

## 🛠️ 7. How Do I Rename the Mod for My Own Project?

1.  **`gradle.properties`**:
    -   `archives_base_name=your-mod-name`
    -   `maven_group=your.package.name`
2.  **`settings.gradle`**:
    -   `rootProject.name = 'your-mod-name'`
3.  **`src/main/resources/fabric.mod.json`**:
    -   `"id": "your-mod-id"`
    -   `"name": "Your Mod Name"`
    -   `"entrypoints": { "client": ["your.package.name.MainAddon"] }`

Good luck developing your NoRisk Client addon! 🚀

