package net.melon.slabs.rei_integration.common;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import net.melon.slabs.rei_integration.JuicerDisplay;
import net.melon.slabs.screens.JuicerScreenHandler;

public class JuicerMenuInfo implements SimplePlayerInventoryMenuInfo<JuicerScreenHandler, JuicerDisplay>{
    protected final JuicerDisplay display;

    
    public JuicerMenuInfo(JuicerDisplay display) {
        this.display = display;
    }


    @Override
    public Iterable<SlotAccessor> getInputSlots(MenuInfoContext<JuicerScreenHandler, ?, JuicerDisplay> context) {
        List<SlotAccessor> list = new ArrayList<SlotAccessor>(4);
        for (int i = 0; i < 5; i++) {
            list.add(SlotAccessor.fromContainer(context.getMenu().getJuicerInventory(), i));
        }

        return list;
    }

    @Override
    public JuicerDisplay getDisplay() {
        return display;
    }
    
}
