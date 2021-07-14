package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProviderBuilder>, BiomeProviderBuilder {
    @Value("resolution")
    @Default
    protected int resolution = 1;
    @Value("blend.noise")
    @Default
    protected NoiseSeeded blend = NoiseSeeded.zero(2);
    @Value("blend.amplitude")
    @Default
    protected double blendAmp = 0d;

    @Override
    public BiomeProviderBuilder get() {
        return this;
    }
}