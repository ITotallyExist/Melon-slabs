package net.melon.slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;

//adds a public method to the tridententity class that allows you to get the itemstack of the entity so you can see if it is enchanted and shit
@Mixin(TridentEntity.class)
public interface TridentMixin {
    @Accessor ItemStack getTridentStack();
}