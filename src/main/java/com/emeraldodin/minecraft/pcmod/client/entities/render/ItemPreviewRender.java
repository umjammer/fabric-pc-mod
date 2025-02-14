package com.emeraldodin.minecraft.pcmod.client.entities.render;

import com.emeraldodin.minecraft.pcmod.Utils;
import com.emeraldodin.minecraft.pcmod.entities.ItemPreviewEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class ItemPreviewRender extends EntityRenderer<ItemPreviewEntity>{
	public ItemPreviewRender(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(ItemPreviewEntity entity) {
		return null;
	}

	@Override
	public void render(ItemPreviewEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
		MinecraftClient mcc = MinecraftClient.getInstance();
		matrices.push();

		matrices.translate(0, 0.5, 0);
		Vec3d v = mcc.player.getPos();
		Quaternion look = Utils.lookAt(entity.getPos(), new Vec3d(v.x, entity.getY(), v.z));
		matrices.multiply(look);
		matrices.push();
		MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getPreviewedItemStack(), Mode.NONE, 200, 0, matrices, vertexConsumers, 0);
		matrices.pop();
		matrices.pop();
	}
}
