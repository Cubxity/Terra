package com.dfsek.terra.addons.generation.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

@Addon("generation-stage-flora")
@Version("1.0.0")
@Author("Terra")
public class FloraGenerationAddon extends TerraAddon implements EventListener {

    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager().registerListener(this, this);
    }

    public void onPackLoad(ConfigPackPreLoadEvent event) {
        event.getPack().applyLoader(FloraLayer.class, FloraLayerLoader::new);
        event.getPack().getOrCreateRegistry(GenerationStageProvider.class).register("FLORA", pack -> new FloraGenerationStage(main));
    }

    public void onBiomeLoad(ConfigurationLoadEvent event) {
        if(SeededTerraBiome.class.isAssignableFrom(event.getType().getTypeClass())) {
            event.getLoadedObject(SeededTerraBiome.class).getContext().put(event.load(new BiomeFloraTemplate()).get());
        }
    }
}