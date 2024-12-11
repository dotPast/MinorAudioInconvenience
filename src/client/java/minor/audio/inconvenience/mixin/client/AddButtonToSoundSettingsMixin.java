package minor.audio.inconvenience.mixin.client;

import minor.audio.inconvenience.AddSoundsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public class AddButtonToSoundSettingsMixin extends Screen {
    protected AddButtonToSoundSettingsMixin(Text title) {
        super(title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        ButtonWidget openAddButtonsMenu = ButtonWidget.builder(
                Text.translatable("text.menu.minor-audio-inconvenience.add.enter"),
                button -> {
                    MinecraftClient.getInstance().setScreen(new AddSoundsScreen());
                }
        ).dimensions(6, 6, 180, 20).build();

        this.addDrawableChild(openAddButtonsMenu);
    }
}
