package com.emeraldodin.minecraft.pcmod.entities;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class ItemPreviewEntity extends Entity {
    private static final TrackedData<ItemStack> PREVIEWED_STACK =
            DataTracker.registerData(ItemPreviewEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public ItemPreviewEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public ItemPreviewEntity(World world, double x, double y, double z, ItemStack stack) {
        this(EntityList.ITEM_PREVIEW, world);
        this.setPosition(x, y, z);
        this.getDataTracker().set(PREVIEWED_STACK, stack);
    }

    public ItemPreviewEntity(World world, double x, double y, double z) {
        this(EntityList.ITEM_PREVIEW, world);
        this.setPosition(x, y, z);
    }

    @Override
    public void tick() {
        if (this.world.isClient) {
            if (this != PCModClient.thePreviewEntity) {
                this.kill();
            }
        } else {
            this.kill();
        }
    }

    public void setItem(ItemStack is) {
        this.getDataTracker().set(PREVIEWED_STACK, is);
    }

    public ItemStack getPreviewedItemStack() {
        return this.getDataTracker().get(PREVIEWED_STACK);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(PREVIEWED_STACK, new ItemStack(Items.REDSTONE_BLOCK));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        if (tag.contains("Item")) {
            this.getDataTracker().set(PREVIEWED_STACK, ItemStack.fromNbt(tag.getCompound("Item")));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        tag.put("Item", this.getDataTracker().get(PREVIEWED_STACK).writeNbt(new NbtCompound()));
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
