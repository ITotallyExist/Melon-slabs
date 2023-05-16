// package net.melon.slabs.rei_integration;

// import me.shedaniel.rei.api.common.category.CategoryIdentifier;
// import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
// import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
// import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
// import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;

// public class melonslabsCommonPlugin implements REIServerPlugin {
//     public static final CategoryIdentifier<AlloyForgingDisplay> ID = CategoryIdentifier.of(AlloyForgery.id("forging"));

//     @Override
//     public void registerMenuInfo(MenuInfoRegistry registry) {
//         registry.register(ID, AlloyForgeScreenHandler.class, SimpleMenuInfoProvider.of(AlloyForgeryMenuInfo::new));
//     }

//     @Override
//     public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
//         registry.register(ID, AlloyForgingDisplay.Serializer.INSTANCE);
//     }
// }
