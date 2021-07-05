package com.dfsek.terra.config;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.util.MaterialSet;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;
import com.dfsek.terra.config.loaders.LinkedHashMapLoader;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;

import java.util.LinkedHashMap;

public class GenericLoaders implements LoaderRegistrar {
    private final TerraPlugin main;

    public GenericLoaders(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(Range.class, new RangeLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(LinkedHashMap.class, new LinkedHashMapLoader())
                .registerLoader(BiomeSource.Type.class, (t, object, cf) -> BiomeSource.Type.valueOf((String) object));

        if(main != null) {
            registry.registerLoader(TerraAddon.class, main.getAddons())
                    .registerLoader(BlockType.class, (t, object, cf) -> main.getWorldHandle().createBlockData((String) object).getBlockType());
        }
    }
}