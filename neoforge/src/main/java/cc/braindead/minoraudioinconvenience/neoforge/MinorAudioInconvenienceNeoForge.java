package cc.braindead.minoraudioinconvenience.neoforge;

import net.neoforged.fml.common.Mod;

import cc.braindead.minoraudioinconvenience.MinorAudioInconvenience;

@Mod(MinorAudioInconvenience.MOD_ID)
public final class MinorAudioInconvenienceNeoForge {
    public MinorAudioInconvenienceNeoForge() {
        // Run our common setup.
        MinorAudioInconvenience.init();
    }
}
