package minor.audio.inconvenience.mixin.client;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SubtitlesHud.class)
public class DisplaySubtitleMixin {
    @Shadow
    @Final
    private List<SubtitlesHud.SubtitleEntry> entries;

    @Inject(method = "onSoundPlayed", at = @At(value = "RETURN"))
    private void showAll(SoundInstance sound, WeightedSoundSet soundSet, float range, CallbackInfo ci) {
        if (MinorAudioInconvenience.CONFIG.showAllSounds()) {
            this.entries.add(
                    new SubtitlesHud.SubtitleEntry(
                            Text.of(sound.getId().toString()),
                            range,
                            new Vec3d(sound.getX(), sound.getY(), sound.getZ())));
        }

    }
}
