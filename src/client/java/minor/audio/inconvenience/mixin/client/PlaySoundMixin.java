package minor.audio.inconvenience.mixin.client;

import minor.audio.inconvenience.MinorAudioInconvenience;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;
import java.util.Optional;

@Mixin(ClientWorld.class)
public class PlaySoundMixin {
    @ModifyArgs(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;I)V"))
    private void injectVolume(Args args) {
        if (MinorAudioInconvenience.CONFIG.enabled()) {
            PositionedSoundInstance sound = args.get(0);

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

                    if (Objects.equals(sound.getId().getNamespace(), soundOverrideId[0]) && Objects.equals(sound.getId().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            args.set(
                    0,
                    new PositionedSoundInstance(
                            new SoundEvent(sound.getId(), Optional.empty()), sound.getCategory(), volume, pitch, Random.create(), sound.getX(), sound.getY(), sound.getZ()
                    )
            );
        }
    }

    @ModifyArgs(method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)V"))
    private void injectVolumeNoDelay(Args args) {
        if (MinorAudioInconvenience.CONFIG.enabled()) {
            PositionedSoundInstance sound = args.get(0);

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

                    if (Objects.equals(sound.getId().getNamespace(), soundOverrideId[0]) && Objects.equals(sound.getId().getPath(), soundOverrideId[1])) {
                        volume = Float.parseFloat(splitSoundOverride[1]);
                    }
                } catch (Exception ignored) {
                    MinorAudioInconvenience.LOGGER.error("Format of '{}' sound override is invalid.", soundOverride);
                }
            }

            args.set(
                    0,
                    new PositionedSoundInstance(
                            new SoundEvent(sound.getId(), Optional.empty()), sound.getCategory(), volume, pitch, Random.create(), sound.getX(), sound.getY(), sound.getZ()
                    )
            );
        }
    }
}