package minor.audio.inconvenience.fabric;

import net.fabricmc.api.ModInitializer;
import minor.audio.inconvenience.MinorAudioInconvenience;

public class MAIFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		MinorAudioInconvenience.init();
	}
}
