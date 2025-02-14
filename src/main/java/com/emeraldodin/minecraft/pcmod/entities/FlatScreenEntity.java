package com.emeraldodin.minecraft.pcmod.entities;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;
import com.emeraldodin.minecraft.pcmod.client.utils.VNCControlRunnable;
import com.emeraldodin.minecraft.pcmod.item.ItemList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlatScreenEntity extends Entity {
    private static final TrackedData<Float> LOOK_AT_POS_X =
            DataTracker.registerData(FlatScreenEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> LOOK_AT_POS_Y =
            DataTracker.registerData(FlatScreenEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> LOOK_AT_POS_Z =
            DataTracker.registerData(FlatScreenEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final TrackedData<String> OWNER_UUID =
            DataTracker.registerData(FlatScreenEntity.class, TrackedDataHandlerRegistry.STRING);

    public FlatScreenEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    public FlatScreenEntity(World world, double x, double y, double z) {
        this(EntityList.FLATSCREEN, world);
        this.setPosition(x, y, z);
    }

    public FlatScreenEntity(World world, Double x, Double y, Double z, Vec3d lookAt, String uuid) {
        this(EntityList.FLATSCREEN, world);
        this.setPosition(x, y, z);
        this.getDataTracker().set(LOOK_AT_POS_X, (float) lookAt.x);
        this.getDataTracker().set(LOOK_AT_POS_Y, (float) lookAt.y);
        this.getDataTracker().set(LOOK_AT_POS_Z, (float) lookAt.z);
        this.getDataTracker().set(OWNER_UUID, uuid);
    }

    public Vec3d getLookAtPos() {
        return new Vec3d(this.getDataTracker().get(LOOK_AT_POS_X),
                this.getDataTracker().get(LOOK_AT_POS_Y),
                this.getDataTracker().get(LOOK_AT_POS_Z));
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(LOOK_AT_POS_X, 0f);
        this.getDataTracker().startTracking(LOOK_AT_POS_Y, 0f);
        this.getDataTracker().startTracking(LOOK_AT_POS_Z, 0f);
        this.getDataTracker().startTracking(OWNER_UUID, "");
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        this.getDataTracker().set(LOOK_AT_POS_X, tag.getFloat("LookAtX"));
        this.getDataTracker().set(LOOK_AT_POS_Y, tag.getFloat("LookAtY"));
        this.getDataTracker().set(LOOK_AT_POS_Z, tag.getFloat("LookAtZ"));
        this.getDataTracker().set(OWNER_UUID, tag.getString("Owner"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        tag.putFloat("LookAtX", this.getDataTracker().get(LOOK_AT_POS_X));
        tag.putFloat("LookAtY", this.getDataTracker().get(LOOK_AT_POS_Y));
        tag.putFloat("LookAtZ", this.getDataTracker().get(LOOK_AT_POS_Z));
        tag.putString("Owner", this.getDataTracker().get(OWNER_UUID));
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (!player.world.isClient) {
            if (player.isSneaking()) {
                this.kill();
                player.world.spawnEntity(new ItemEntity(player.world,
                        this.getPos().x, this.getPos().y, this.getPos().z,
                        new ItemStack(ItemList.ITEM_FLATSCREEN)));
            }
        } else {
            if (PCModClient.isOnClient && !player.isSneaking()) {
                if (this.getOwnerUUID().equals(player.getUuid().toString())) {
                    PCModClient.openPCFocusGUI();
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void tick() {
        if (getOwnerUUID().isEmpty()) {
            this.kill();
        }
    }

    @Override
    public boolean collides() {
        return true;
    }

    public String getOwnerUUID() {
        return this.getDataTracker().get(OWNER_UUID);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}