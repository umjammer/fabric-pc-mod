package com.emeraldodin.minecraft.pcmod.item;

import com.emeraldodin.minecraft.pcmod.entities.FlatScreenEntity;

import com.emeraldodin.minecraft.pcmod.main.PCMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ItemList {
    public static final FlatScreenItem ITEM_FLATSCREEN = new FlatScreenItem(new Item.Settings().group(ItemGroup.TOOLS), FlatScreenEntity.class, SoundEvents.BLOCK_METAL_PLACE);

    public static void init() {
        registerItem("flatscreen", ITEM_FLATSCREEN);
    }

    private static Item registerItem(String id, Item it) {
        Registry.register(Registry.ITEM, new Identifier(PCMod.MODID, id), it);
        return it;
    }
}
