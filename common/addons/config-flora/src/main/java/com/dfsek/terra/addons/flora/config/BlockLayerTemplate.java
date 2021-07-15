package com.dfsek.terra.addons.flora.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

public class BlockLayerTemplate implements ObjectTemplate<BlockLayer> {
    @Value("layers")
    private int layers;

    @Value("materials")
    private ProbabilityCollection<BlockState> data;

    @Override
    public BlockLayer get() {
        return new BlockLayer(layers, data);
    }
}