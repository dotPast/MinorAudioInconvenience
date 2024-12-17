package minor.audio.inconvenience;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.SliderComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.CollapsibleContainer;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddSoundsScreen extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout root) {
        root.surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        FlowLayout soundList = Containers.verticalFlow(Sizing.fill(), Sizing.content());
        FlowLayout header = Containers.horizontalFlow(Sizing.fill(), Sizing.content());
        FlowLayout soundLayout = Containers.verticalFlow(Sizing.content(), Sizing.content());

        TextBoxComponent searchBar = Components.textBox(Sizing.expand()).text("");

        generateEntries(soundLayout, searchBar.getValue());

        searchBar.onChanged().subscribe((query) -> {
            soundLayout.clearChildren();
            generateEntries(soundLayout, query);
        });

        header.child(Components.label(Component.translatable("text.menu.minor-audio-inconvenience.add.title")).verticalTextAlignment(VerticalAlignment.CENTER).sizing(Sizing.content(), Sizing.fixed(20)));
        header.child(Components.label(Component.translatable("text.menu.minor-audio-inconvenience.add.search").append(":")).verticalTextAlignment(VerticalAlignment.CENTER).sizing(Sizing.content(), Sizing.fixed(20)).margins(Insets.left(16)));
        header.child((io.wispforest.owo.ui.core.Component) searchBar);

        soundList.child(header);
        soundList.child(soundLayout);

        root.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(
                                Containers.verticalScroll(Sizing.fill(80), Sizing.fill(75), soundList)
                        )
                        .padding(Insets.of(10))
                        .surface(Surface.DARK_PANEL)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
        );
    }

    public void generateEntries(FlowLayout layout, String searchQuery) {
        Minecraft gameInstance = Minecraft.getInstance();
        SoundManager soundManager = gameInstance.getSoundManager();

        Map<String, CollapsibleContainer> containers = new HashMap<>();

        for (ResourceLocation soundID : soundManager.getAvailableSounds()) {
            if (searchQuery.length() == 0) {
                String soundNamespace = soundID.getNamespace();

                CollapsibleContainer container;

                if (containers.isEmpty() || !containers.containsKey(soundNamespace)) {
                    container = Containers.collapsible(Sizing.content(), Sizing.content(), Component.literal(soundNamespace), false);
                } else {
                    container = containers.get(soundNamespace);
                }

                container.child(generateSoundComponent(soundID));

                containers.put(soundNamespace, container);
            } else {
                if (soundID.getPath().contains(searchQuery)) {
                    String soundNamespace = soundID.getNamespace();

                    CollapsibleContainer container;

                    if (containers.isEmpty() || !containers.containsKey(soundNamespace)) {
                        container = Containers.collapsible(Sizing.content(), Sizing.content(), Component.literal(soundNamespace), false);
                    } else {
                        container = containers.get(soundNamespace);
                    }

                    container.child(generateSoundComponent(soundID));

                    containers.put(soundNamespace, container);
                }
            }
        }

        for (CollapsibleContainer container : containers.values()) {
            layout.child(container);
        }
    }

    public GridLayout generateSoundComponent(ResourceLocation soundID) {
        Minecraft gameInstance = Minecraft.getInstance();
        SoundManager soundManager = gameInstance.getSoundManager();

        GridLayout soundComponent = Containers.grid(Sizing.expand(), Sizing.content(), 1, 2);

        FlowLayout soundInfo = Containers.horizontalFlow(Sizing.expand(50), Sizing.content());
        FlowLayout soundInteraction = Containers.horizontalFlow(Sizing.expand(50), Sizing.content());

        ButtonComponent playButton = Components.button(Component.literal("▶"), button -> {
            SoundInstance soundInstance;
            if (gameInstance.getSingleplayerServer() != null || gameInstance.getCurrentServer() != null) {
                if (gameInstance.player != null) {
                    soundInstance = new SimpleSoundInstance(
                            new SoundEvent(soundID, null),
                            SoundSource.MASTER,
                            1f,
                            1f,
                            RandomSource.create(),
                            gameInstance.player.getX(),
                            gameInstance.player.getY(),
                            gameInstance.player.getZ()
                    );
                } else {
                    soundInstance = new AbstractSoundInstance(
                            new SoundEvent(soundID, null),
                            SoundSource.MASTER,
                            RandomSource.create()
                    ) {
                    };
                }
            } else {
                soundInstance = new AbstractSoundInstance(
                        new SoundEvent(soundID, null),
                        SoundSource.MASTER,
                        RandomSource.create()
                ) {
                };
            }

            soundManager.play(soundInstance);
        });
        
        playButton.setWidth(20);
        playButton.setHeight(20);

        soundInfo.child(
                (io.wispforest.owo.ui.core.Component) playButton 
        );

        soundInfo.child(
                Components.label(Component.literal(soundID.getPath())).verticalTextAlignment(VerticalAlignment.CENTER).sizing(Sizing.content(), Sizing.fixed(20))
        );

        SliderComponent volumeSlider = Components.slider(Sizing.fill(10)).message(
                (String progress) -> Component.translatable("text.menu.minor-audio-inconvenience.add.volumeslider").append(Component.literal(String.format(
                        ": %s",
                        (int) Math.ceil(Double.parseDouble(progress) * 100)
                )).append("%"))
        ).value(1).scrollStep(0.01);

        if (gameInstance.options.guiScale().get() == 4) {
            volumeSlider.setWidth(50);
            volumeSlider.message((String progress) -> Component.literal(String.valueOf((int) Math.ceil(Double.parseDouble(progress) * 100))).append("%"));
        } else {
            volumeSlider.setWidth(100);
        }

        soundInteraction.child(
                (io.wispforest.owo.ui.core.Component) volumeSlider
        );

        ButtonComponent addToConfigButton = Components.button(Component.literal("+"), button -> {
            Double volume = Math.ceil(volumeSlider.value() * 100) / 100;

            for (String configId : MinorAudioInconvenience.CONFIG.soundList()) {
                if (configId.split("=")[0].equals(soundID.toString())) {
                    MinorAudioInconvenience.CONFIG.soundList().remove(configId);
                    break;
                }
            }

            MinorAudioInconvenience.CONFIG.soundList().add(String.format("%s=%s", soundID, volume));
            MinorAudioInconvenience.CONFIG.save();
            button.setMessage(Component.literal("✔"));
        });

        for (String configId : MinorAudioInconvenience.CONFIG.soundList()) {
            String[] configSplit = configId.split("=");

            if (configSplit[0].equals(soundID.toString())) {
                volumeSlider.value(Double.parseDouble(configSplit[1]));
            }
        }

        addToConfigButton.setWidth(20);
        addToConfigButton.setHeight(20);

        soundInteraction.child(
                (io.wispforest.owo.ui.core.Component) addToConfigButton
        );

        soundComponent.child(soundInfo.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER), 0, 0);
        soundComponent.child(soundInteraction.alignment(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER), 0, 1);

        return soundComponent;
    }
}