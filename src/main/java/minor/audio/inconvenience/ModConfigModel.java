package minor.audio.inconvenience;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "minor-audio-inconvenience")
@Config(name = "minor-audio-inconvenience", wrapperName = "ModConfig")
public class ModConfigModel {
    public int anIntOption = 16;
    public boolean aBooleanToggle = false;

    public Choices anEnumOption = Choices.ANOTHER_CHOICE;

    public enum Choices {
        A_CHOICE, ANOTHER_CHOICE;
    }
}
