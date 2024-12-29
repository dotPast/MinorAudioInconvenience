package minor.audio.inconvenience.mixin.client;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(SoundSystem.class)
public class PlaySoundMixin {
    @ModifyVariable(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), argsOnly = true)
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

                    if (Objects.equals(modifiedSound.getId().getNamespace(), soundOverrideId[0]) && Objects.equals(modifiedSound.getId().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            modifiedSound = new PositionedSoundInstance(
                    SoundEvent.of(sound.getId()), sound.getCategory(), volume, pitch, Random.create(), sound.getX(), sound.getY(), sound.getZ()
            );
        }

        return modifiedSound;
    }
}
