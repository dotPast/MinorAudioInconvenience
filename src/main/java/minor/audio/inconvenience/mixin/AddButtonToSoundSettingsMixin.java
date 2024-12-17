package minor.audio.inconvenience.mixin;

import minor.audio.inconvenience.AddSoundsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public class AddButtonToSoundSettingsMixin extends Screen {
    protected AddButtonToSoundSettingsMixin(Component title) {
        super(title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        Button openAddButtonsMenu = Button.builder(
                Component.translatable("text.menu.minor-audio-inconvenience.add.enter"),
                button -> Minecraft.getInstance().setScreen(new AddSoundsScreen())

        ).bounds(6, 6, 180, 20).build();

        this.addRenderableWidget(openAddButtonsMenu);
    }
}
