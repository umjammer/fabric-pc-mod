package com.emeraldodin.minecraft.pcmod.entities;

import com.emeraldodin.minecraft.pcmod.main.PCMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityList {
	public static EntityType<ItemPreviewEntity> ITEM_PREVIEW;
	public static EntityType<FlatScreenEntity> FLATSCREEN;

	public static void init() {
		FLATSCREEN = Registry.register(Registry.ENTITY_TYPE,
				new Identifier(PCMod.MODID, "flat_screen"),
				FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<FlatScreenEntity>) FlatScreenEntity::new)
						.dimensions(new EntityDimensions(0.8f, 0.8f, true)).trackRangeBlocks(60).trackedUpdateRate(2).forceTrackedVelocityUpdates(true).build());
		ITEM_PREVIEW = Registry.register(Registry.ENTITY_TYPE,
				new Identifier(PCMod.MODID, "item_preview"),
				FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<ItemPreviewEntity>) ItemPreviewEntity::new)
						.dimensions(new EntityDimensions(1,1, true)).trackRangeBlocks(60).trackedUpdateRate(2).forceTrackedVelocityUpdates(true).build());
	}
}