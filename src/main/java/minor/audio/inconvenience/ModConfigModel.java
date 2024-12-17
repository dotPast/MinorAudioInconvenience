package minor.audio.inconvenience;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

import java.util.List;

@Modmenu(modId = "minoraudioinconvenience")
@Config(name = "minor-audio-inconvenience", wrapperName = "ModConfig")
public class ModConfigModel {
    public Boolean enabled = true;
    public Boolean showAllSounds = false;

    public List<String> soundList = List.of("minecraft:weather.rain=0");
}
