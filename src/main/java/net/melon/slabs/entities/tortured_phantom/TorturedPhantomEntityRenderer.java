package net.melon.slabs.entities.tortured_phantom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class TorturedPhantomEntityRenderer 
extends MobEntityRenderer<TorturedPhantomEntity, TorturedPhantomEntityModel<TorturedPhantomEntity>> {
    private static final Identifier TEXTURE = new Identifier("melonslabs:textures/entity/tortured_phantom.png");

    public TorturedPhantomEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TorturedPhantomEntityModel(context.getPart(EntityModelLayers.PHANTOM)), 0.75f);
        this.addFeature(new TorturedPhantomEyesFeatureRenderer<TorturedPhantomEntity>(this));
    }

    @Override
    public Identifier getTexture(TorturedPhantomEntity phantomEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(TorturedPhantomEntity phantomEntity, MatrixStack matrixStack, float f) {
        int i = phantomEntity.getPhantomSize();
        float g = 1.0f + 0.15f * (float)i;
        matrixStack.scale(g, g, g);
        matrixStack.translate(0.0f, 1.3125f, 0.1875f);
    }

    @Override
    protected void setupTransforms(TorturedPhantomEntity phantomEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
        super.setupTransforms(phantomEntity, matrixStack, f, g, h, i);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(phantomEntity.getPitch()));
    }
}

