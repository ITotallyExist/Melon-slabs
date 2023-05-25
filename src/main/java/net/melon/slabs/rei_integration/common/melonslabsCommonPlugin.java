package net.melon.slabs.rei_integration.common;


import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import net.melon.slabs.rei_integration.JuicerCategory;
import net.melon.slabs.screens.JuicerScreenHandler;

public class melonslabsCommonPlugin implements REIServerPlugin {
    
    @Override
    public void registerMenuInfo(MenuInfoRegistry registry){
        registry.register(JuicerCategory.JUICER_DISPLAY, JuicerScreenHandler.class, SimpleMenuInfoProvider.of(JuicerMenuInfo::new));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
	    registry.register(JuicerCategory.JUICER_DISPLAY, new JuicerDisplaySerializer());
    }
}
