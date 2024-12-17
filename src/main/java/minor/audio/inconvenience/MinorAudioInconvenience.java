package minor.audio.inconvenience;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import minor.audio.inconvenience.ModConfig;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(value = MinorAudioInconvenience.MODID, dist = Dist.CLIENT)
public class MinorAudioInconvenience {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "minoraudioinconvenience";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ModConfig CONFIG = ModConfig.createAndLoad();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MinorAudioInconvenience(IEventBus modEventBus, ModContainer modContainer) {
    }
}
