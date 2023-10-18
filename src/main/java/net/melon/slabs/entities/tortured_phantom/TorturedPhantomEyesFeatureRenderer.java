package net.melon.slabs.entities.tortured_phantom;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

public class TorturedPhantomEyesFeatureRenderer <T extends TorturedPhantomEntity>
extends EyesFeatureRenderer<T, TorturedPhantomEntityModel<T>> {
    private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier("textures/entity/phantom_eyes.png"));

    public TorturedPhantomEyesFeatureRenderer(FeatureRendererContext<T, TorturedPhantomEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}

