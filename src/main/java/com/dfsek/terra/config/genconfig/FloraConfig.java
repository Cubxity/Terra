package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FloraConfig extends TerraConfigObject implements Flora {
    private static final Map<String, FloraConfig> floraConfig = new HashMap<>();
    private Palette<BlockData> floraPalette;
    private String id;
    
    Set<Material> spawnable;
    Set<Material> replaceable;

    public FloraConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("blocks")) throw new InvalidConfigurationException("No blocks defined in custom flora!");
        if(!contains("id")) throw new InvalidConfigurationException("Flora ID unspecified!");
        if(!contains("name")) throw new InvalidConfigurationException("Flora name unspecified!");
        if(!contains("spawnable")) throw new InvalidConfigurationException("Flora spawnable blocks unspecified!");

        spawnable = new HashSet<>();
        replaceable = new HashSet<>();

        try {
            for(String s : getStringList("spawnable")) {
                spawnable.add(Bukkit.createBlockData(s).getMaterial());
            }
            Bukkit.getLogger().info("[Terra] Added " + spawnable.size() + " items to spawnable list.");
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid material ID in spawnable list: " + e.getMessage());
        }
        try {
            for(String s : getStringList("replaceable")) {
                replaceable.add(Bukkit.createBlockData(s).getMaterial());
            }
            Bukkit.getLogger().info("[Terra] Added " + spawnable.size() + " items to replaceable list.");
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid material ID in replaceable list: " + e.getMessage());
        }

        Palette<BlockData> p = new RandomPalette<>(new Random(getInt("seed", 4)));

        floraPalette  = PaletteConfig.getPalette(getMapList("blocks"), p);
        if(!contains("id")) throw new InvalidConfigurationException("Flora ID unspecified!");
        this.id = getString("id");
        floraConfig.put(id, this);
    }

    public String getID() {
        return id;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        List<Block> blocks = new ArrayList<>();
        for(int y : range) {
            Block check = chunk.getBlock(x, y, z);
            if(spawnable.contains(check.getType())) {
                blocks.add(check);
            }
        }
        return blocks;
    }

    @Override
    public boolean plant(Location location) {
        int size = floraPalette.getSize();
        for(int i = 0; i < size; i++) {
            if(!replaceable.contains(location.clone().add(0, i+1, 0).getBlock().getType())) return false;
        }
        for(int i = 0; i < size; i++) {
            location.clone().add(0, i+1, 0).getBlock().setBlockData(floraPalette.get(size-(i+1), location.getBlockX(), location.getBlockZ()), false);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Flora with name ID " + getID();
    }

    public static FloraConfig fromID(String id) {
        return floraConfig.get(id);
    }
}