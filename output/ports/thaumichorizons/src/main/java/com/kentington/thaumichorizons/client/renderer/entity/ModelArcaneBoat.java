package com.kentington.thaumichorizons.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/** Five-panel boat hull with the original 64x32 texture layout. */
public final class ModelArcaneBoat extends ModelBase {
    private final ModelRenderer[] sides = new ModelRenderer[5];

    public ModelArcaneBoat() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        for (int i = 0; i < sides.length; i++) sides[i] = new ModelRenderer(this, 0, 0);
        sides[0].addBox(-12, -8, -3, 24, 16, 4);
        sides[0].setRotationPoint(0, 4, 0);
        sides[0].rotateAngleX = (float) Math.PI / 2;
        sides[1].addBox(-10, -7, -1, 20, 6, 2);
        sides[1].setRotationPoint(-11, 4, 0);
        sides[1].rotateAngleY = (float) Math.PI * 3 / 2;
        sides[2].addBox(-10, -7, -1, 20, 6, 2);
        sides[2].setRotationPoint(11, 4, 0);
        sides[2].rotateAngleY = (float) Math.PI / 2;
        sides[3].addBox(-12, -7, -1, 24, 6, 2);
        sides[3].setRotationPoint(0, 4, -7);
        sides[3].rotateAngleY = (float) Math.PI;
        sides[4].addBox(-12, -7, -1, 24, 6, 2);
        sides[4].setRotationPoint(0, 4, 7);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount,
            float age, float yaw, float pitch, float scale) {
        for (ModelRenderer side : sides) side.render(scale);
    }
}
