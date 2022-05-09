package net.melon.slabs.criteria;

import net.melon.slabs.MelonSlabs;
import net.melon.slabs.mixin.CriteriaMixin;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.util.Identifier;

public class MelonSlabsCriteria {
	public static final CreatedFrankenmelonCriterion CREATED_FRANKENMELON = register(new CreatedFrankenmelonCriterion(new Identifier(MelonSlabs.MOD_ID,"created_frankenmelon")));


	private static <T extends Criterion<?>> T register(T criterion) {
		return CriteriaMixin.callRegister(criterion);
	}

	public static void loadClass() {

	}
}