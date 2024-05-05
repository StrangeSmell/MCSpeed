package com.strangesmell.mcspeed;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpeedBoatModel2<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MCSpeed.MODID, "speed"), "main");
	private final ModelPart frontlun;
	private final ModelPart afterlun;
	private final ModelPart bashou;
	private final ModelPart pengqi;
	private final ModelPart dangban;
	private final ModelPart qianzhao;
	private final ModelPart site;
	private final ModelPart weiyi;
	private final ModelPart base;
	private final ModelPart taban;

	public SpeedBoatModel2(ModelPart root) {
		this.frontlun = root.getChild("frontlun");
		this.afterlun = root.getChild("afterlun");
		this.bashou = root.getChild("bashou");
		this.pengqi = root.getChild("pengqi");
		this.dangban = root.getChild("dangban");
		this.qianzhao = root.getChild("qianzhao");
		this.site = root.getChild("site");
		this.weiyi = root.getChild("weiyi");
		this.base = root.getChild("base");
		this.taban = root.getChild("taban");
	}

	public static LayerDefinition createBodyModel() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition frontlun = partdefinition.addOrReplaceChild("frontlun", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = frontlun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -2.0F, -10.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(8, 13).addBox(-1.0F, -2.0F, 7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(62, 37).addBox(0.0F, -1.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition afterlun = partdefinition.addOrReplaceChild("afterlun", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r2 = afterlun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 24).addBox(19.0F, -3.0F, 7.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(21.0F, -1.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(26, 20).addBox(19.0F, -3.0F, -10.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition bashou = partdefinition.addOrReplaceChild("bashou", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r3 = bashou.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).addBox(6.0F, -8.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 0).addBox(7.0F, -8.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 9).addBox(7.0F, -7.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(7.0F, -8.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(7.0F, -9.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition pengqi = partdefinition.addOrReplaceChild("pengqi", CubeListBuilder.create().texOffs(16, 27).addBox(4.0F, -1.0F, -25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 9).addBox(5.0F, -2.0F, -25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 2).addBox(4.0F, -3.0F, -25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -2.0F, -25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition dangban = partdefinition.addOrReplaceChild("dangban", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r4 = dangban.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(52, 54).addBox(3.0F, -4.0F, 6.0F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 0).addBox(3.0F, -4.0F, -10.0F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition qianzhao = partdefinition.addOrReplaceChild("qianzhao", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r5 = qianzhao.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 13).addBox(-5.0F, -4.0F, -3.0F, 23.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(16, 55).addBox(5.0F, -10.0F, -5.0F, 2.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(22, 39).addBox(2.0F, -8.0F, -6.0F, 4.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(52, 62).addBox(3.0F, -9.0F, -6.0F, 3.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(70, 62).addBox(0.0F, -7.0F, -5.0F, 4.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 70).addBox(-2.0F, -6.0F, -4.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(14, 19).addBox(-3.0F, -5.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(13, 22).addBox(-3.0F, -5.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 28).addBox(-4.0F, -5.0F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 27).addBox(-4.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(37, 29).addBox(-4.0F, -5.0F, -6.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 35).addBox(-5.0F, -4.0F, -7.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(-5.0F, -4.0F, 3.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 39).addBox(-6.0F, -3.0F, -7.0F, 4.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-5.0F, -2.0F, -10.0F, 3.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-7.0F, -2.0F, -9.0F, 2.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(54, 20).addBox(-8.0F, -2.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition site = partdefinition.addOrReplaceChild("site", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r6 = site.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(72, 20).addBox(10.0F, -5.0F, -3.0F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(16.0F, -10.0F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition weiyi = partdefinition.addOrReplaceChild("weiyi", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r7 = weiyi.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(21.0F, -8.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 20).addBox(21.0F, -9.0F, -9.0F, 4.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r8 = base.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(18, 67).addBox(23.0F, -2.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(32, 69).addBox(18.0F, -2.0F, -6.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 54).addBox(18.0F, -3.0F, -6.0F, 6.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, -6.0F, 20.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition taban = partdefinition.addOrReplaceChild("taban", CubeListBuilder.create().texOffs(13, 25).addBox(-6.0F, -5.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 13).addBox(-6.0F, -4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 15).addBox(4.0F, -4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 17).addBox(4.0F, -5.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		frontlun.render(poseStack, buffer, packedLight, packedOverlay);
		afterlun.render(poseStack, buffer, packedLight, packedOverlay);
		bashou.render(poseStack, buffer, packedLight, packedOverlay);
		pengqi.render(poseStack, buffer, packedLight, packedOverlay);
		dangban.render(poseStack, buffer, packedLight, packedOverlay);
		qianzhao.render(poseStack, buffer, packedLight, packedOverlay);
		site.render(poseStack, buffer, packedLight, packedOverlay);
		weiyi.render(poseStack, buffer, packedLight, packedOverlay);
		base.render(poseStack, buffer, packedLight, packedOverlay);
		taban.render(poseStack, buffer, packedLight, packedOverlay);
	}
}