package gg.norisk.addon.popup.mixin;

import gg.norisk.addon.popup.detector.NoRiskDetector;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Steuert das Laden von Mixins abhaengig davon, ob NoRisk Client vorhanden ist.
 *
 * SICHERHEITSMERKMAL:
 * Prüft den NoRisk-Status ohne Class.forName() auf Mixin-Ziele,
 * wodurch "MixinTargetAlreadyLoadedException"-Crashes zuverlässig verhindert werden!
 */
public class NoRiskSafeMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Vanilla-Minecraft-Mixins (wie TitleScreenMixin) immer anwenden
        if (mixinClassName.endsWith("TitleScreenMixin")) {
            return true;
        }

        // Alle NoRisk-spezifischen Mixins nur anwenden, wenn NoRisk Client tatsächlich vorhanden ist
        return NoRiskDetector.isNoRiskPresent();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

