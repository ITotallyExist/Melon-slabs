package net.melon.slabs.screens;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class JuicerScreen extends HandledScreen<JuicerScreenHandler> {
    private BlockPos pos;
    
    //A path to the gui texture
    private static final Identifier TEXTURE = new Identifier("melonslabs", "textures/gui/juicer.png");
    
    public JuicerScreen(JuicerScreenHandler handler, PlayerInventory inventory, Text title) {
        this(handler, inventory, title, BlockPos.ORIGIN);

    }
    public JuicerScreen(JuicerScreenHandler handler, PlayerInventory inventory, Text title, BlockPos pos) {
        super(handler, inventory, title);
        this.pos = pos;
        //System.out.println("hi2");

    }

    public BlockPos getBlockPos(){
        return this.pos;
    }
    
    public static Identifier getTexture(){
        return TEXTURE;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
 
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
 
    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}