package net.melon.slabs.criteria;

import net.melon.slabs.MelonSlabs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;

public class MelonSlabsCriteria {
	public static final CreatedFrankenmelonCriterion CREATED_FRANKENMELON = Criteria.register("melonslabs:created_frankenmelon", new CreatedFrankenmelonCriterion());

	public static void loadClass() {

	}
}