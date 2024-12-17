package minor.audio.inconvenience;

import io.wispforest.owo.config.annotation.Config;

import java.util.List;

@Config(name = "minor-audio-inconvenience", wrapperName = "ModConfig")
public class ModConfigModel {
    public Boolean enabled = true;
    public Boolean showAllSounds = false;

    public List<String> soundList = List.of("minecraft:weather.rain=0");
}
