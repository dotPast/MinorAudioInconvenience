package cc.braindead.minoraudioinconvenience.fabric;

import net.fabricmc.api.ModInitializer;

import cc.braindead.minoraudioinconvenience.MinorAudioInconvenience;

public final class MinorAudioInconvenienceFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        MinorAudioInconvenience.init();
    }
}
