package gg.norisk.addon.popup.mixin;

import gg.norisk.addon.popup.detector.NoRiskDetector;
import gg.norisk.addon.popup.ui.NoRiskMissingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Überwacht den Titelbildschirm von Minecraft.
 * Zeigt das Popup genau dann an, wenn NoRisk Client fehlt und der Hinweis nicht deaktiviert wurde.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Unique
    private static boolean norisk$popupShownThisSession = false;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void norisk$onTitleScreenInit(CallbackInfo ci) {
        // Nur ausführen, falls NoRisk NICHT geladen ist und der Hinweis nicht deaktiviert wurde
        if (!NoRiskDetector.isNoRiskPresent() && !NoRiskDetector.isNoticeDismissed() && !norisk$popupShownThisSession) {
            norisk$popupShownThisSession = true;
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.gui != null) {
                // Zeigt das Popup mit Übergabe des aktuellen TitleScreen als Parent
                mc.gui.setScreen(new NoRiskMissingScreen(this, "NRC Universal Addon"));
            }
        }
    }
}

