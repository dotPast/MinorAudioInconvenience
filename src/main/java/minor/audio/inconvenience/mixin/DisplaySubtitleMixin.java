package minor.audio.inconvenience.mixin;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SubtitleOverlay.class)
public class DisplaySubtitleMixin {
    @Shadow @Final private List<SubtitleOverlay.Subtitle> subtitles;

    @Inject(method = "onPlaySound", at = @At(value = "RETURN"))
    private void showAll(SoundInstance sound, WeighedSoundEvents event, float range, CallbackInfo ci) {
        if (MinorAudioInconvenience.CONFIG.showAllSounds()) {
            this.subtitles.add(
                    new SubtitleOverlay.Subtitle(
                            Component.literal(sound.getLocation().toString()),
                            range,
                            new Vec3(sound.getX(), sound.getY(), sound.getZ())
                    )
            );
        }
    }
}
