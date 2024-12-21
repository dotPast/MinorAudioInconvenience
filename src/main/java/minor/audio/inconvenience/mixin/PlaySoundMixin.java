package minor.audio.inconvenience.mixin;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(ClientLevel.class)
public class PlaySoundMixin {
    @ModifyArgs(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"))
    private void injectVolume(Args args) {
        if (MinorAudioInconvenience.CONFIG.enabled()) {
            SimpleSoundInstance sound = args.get(0);

            float volume = 1;
            float pitch = 1;

            try {
                volume = sound.getVolume();
            } catch (Exception ignored) {
            }

            try {
                pitch = sound.getPitch();
            } catch (Exception ignored) {
            }

            for (String soundOverride : MinorAudioInconvenience.CONFIG.soundList()) {
                String[] splitSoundOverride = soundOverride.split("=");
                try {
                    String[] soundOverrideId = splitSoundOverride[0].split(":");

                    if (Objects.equals(sound.getLocation().getNamespace(), soundOverrideId[0]) && Objects.equals(sound.getLocation().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            args.set(
                    0,
                    new SimpleSoundInstance(
                            SoundEvent.createVariableRangeEvent(sound.getLocation()), sound.getSource(), volume, pitch, RandomSource.create(), sound.getX(), sound.getY(), sound.getZ()
                    )
            );
        }
    }

    @ModifyArgs(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;playDelayed(Lnet/minecraft/client/resources/sounds/SoundInstance;I)V"))
    private void injectVolumeNoDelay(Args args) {
        if (MinorAudioInconvenience.CONFIG.enabled()) {
            SimpleSoundInstance sound = args.get(0);

            float volume = 1;
            float pitch = 1;

            try {
                volume = sound.getVolume();
            } catch (Exception ignored) {
            }

            try {
                pitch = sound.getPitch();
            } catch (Exception ignored) {
            }

            for (String soundOverride : MinorAudioInconvenience.CONFIG.soundList()) {
                String[] splitSoundOverride = soundOverride.split("=");
                try {
                    String[] soundOverrideId = splitSoundOverride[0].split(":");

                    if (Objects.equals(sound.getLocation().getNamespace(), soundOverrideId[0]) && Objects.equals(sound.getLocation().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            args.set(
                    0,
                    new SimpleSoundInstance(
                            SoundEvent.createVariableRangeEvent(sound.getLocation()), sound.getSource(), volume, pitch, RandomSource.create(), sound.getX(), sound.getY(), sound.getZ()
                    )
            );
        }
    }
}
