package minor.audio.inconvenience.mixin;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(SoundManager.class)
public class PlaySoundMixin {
    @ModifyVariable(method = "play", at = @At("HEAD"), argsOnly = true)
    private SoundInstance injectVolume(SoundInstance sound) {
        SoundInstance modifiedSound = sound;
        if (MinorAudioInconvenience.CONFIG.enabled()) {
            float volume = 1;
            float pitch = 1;

            try {
                volume = modifiedSound.getVolume();
            } catch (Exception ignored) {
            }

            try {
                pitch = modifiedSound.getPitch();
            } catch (Exception ignored) {
            }

            for (String soundOverride : MinorAudioInconvenience.CONFIG.soundList()) {
                String[] splitSoundOverride = soundOverride.split("=");
                try {
                    String[] soundOverrideId = splitSoundOverride[0].split(":");

                    if (Objects.equals(modifiedSound.getLocation().getNamespace(), soundOverrideId[0]) && Objects.equals(modifiedSound.getLocation().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            modifiedSound = new SimpleSoundInstance(
                    SoundEvent.createVariableRangeEvent(sound.getLocation()), sound.getSource(), volume, pitch, RandomSource.create(), sound.getX(), sound.getY(), sound.getZ()
            );
        }

        return modifiedSound;
    }
}
