package net.melon.slabs.criteria;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CreatedFrankenmelonCriterion extends AbstractCriterion<CreatedFrankenmelonCriterion.Conditions> {
    private final Identifier id;

    public Identifier getId() {
        return id;
    }

    public CreatedFrankenmelonCriterion(Identifier id) {
		this.id = id;
	}

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> conditions.matches(true));
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject json, Optional<LootContextPredicate> lootCtxPredicate, AdvancementEntityPredicateDeserializer deserializer) {
		return new CreatedFrankenmelonCriterion.Conditions(lootCtxPredicate);
	}

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Optional<LootContextPredicate> lootCtxPredicate) {
            super(lootCtxPredicate);
        }

        public static CreatedFrankenmelonCriterion.Conditions create(Optional<LootContextPredicate> lootContextPredicate) {
            return new CreatedFrankenmelonCriterion.Conditions(lootContextPredicate);
        }

        public boolean matches(Boolean input) {
            return input;
        }

    }
}