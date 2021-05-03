package com.dfsek.terra.fabric.mixin.inventory;

import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = ItemMeta.class, prefix = "vw$"))
public abstract class ItemStackMetaMixin {
    @Shadow
    public abstract boolean hasEnchantments();

    @Shadow
    public abstract ListTag getEnchantments();

    @Shadow
    public abstract void addEnchantment(net.minecraft.enchantment.Enchantment enchantment, int level);

    public Object vw$getHandle() {
        return this;
    }

    public Map<Enchantment, Integer> vw$getEnchantments() {
        if(!hasEnchantments()) return Collections.emptyMap();
        Map<Enchantment, Integer> map = new HashMap<>();

        getEnchantments().forEach(enchantment -> {
            CompoundTag eTag = (CompoundTag) enchantment;
            map.put(FabricAdapter.adapt(Registry.ENCHANTMENT.get(eTag.getInt("id"))), eTag.getInt("lvl"));
        });
        return map;
    }

    public void vw$addEnchantment(Enchantment enchantment, int level) {
        addEnchantment(FabricAdapter.adapt(enchantment), level);
    }
}