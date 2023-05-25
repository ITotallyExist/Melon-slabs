package net.melon.slabs.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class JuicerScreenHandlerFactory  implements NamedScreenHandlerFactory{
    private final JuicerScreenHandler screenHandler;
    private final Text title;

    public JuicerScreenHandlerFactory(JuicerScreenHandler screenHandler, Text title) {
        this.screenHandler = screenHandler;
        this.title = title;
    }

    @Override
    public ScreenHandler createMenu(int var1, PlayerInventory var2, PlayerEntity var3) {
        return screenHandler;
    }

    @Override
    public Text getDisplayName() {
        return this.title;
    }
}
