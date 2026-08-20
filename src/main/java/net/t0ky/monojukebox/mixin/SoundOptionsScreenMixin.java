package net.t0ky.monojukebox.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.network.chat.Component;
import net.t0ky.monojukebox.JukeboxConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends OptionsSubScreen {
    protected SoundOptionsScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addJukeboxMonoSlider(CallbackInfo ci) {
        if (this.list != null) {
            OptionInstance<Double> slider = new OptionInstance<>(
                    "options.jukebox_mono",
                    OptionInstance.noTooltip(),
                    (label, value) -> Component.literal("Jukebox Mono Effect: " + (value > 0 ? (int)(value * 100) + "%" : "Off")),
                    OptionInstance.UnitDouble.INSTANCE,
                    JukeboxConfig.blendFactor,
                    value -> {
                        JukeboxConfig.blendFactor = value;
                        JukeboxConfig.save();
                    }
            );

            this.list.addBig(slider);
        }
    }
}
