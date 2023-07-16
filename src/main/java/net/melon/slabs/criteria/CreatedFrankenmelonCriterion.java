package net.melon.slabs.criteria;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
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

    protected Conditions conditionsFromJson(JsonObject json, LootContextPredicate lootCtxPredicate, AdvancementEntityPredicateDeserializer deserializer) {
		return new CreatedFrankenmelonCriterion.Conditions(id, lootCtxPredicate);
	}

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier id, LootContextPredicate lootCtxPredicate) {
            super(id, lootCtxPredicate);
        }

        public static CreatedFrankenmelonCriterion.Conditions create(Identifier id, LootContextPredicate lootContextPredicate) {
            return new CreatedFrankenmelonCriterion.Conditions(id, lootContextPredicate);
        }

        public static CreatedFrankenmelonCriterion.Conditions create(Identifier id) {
            return new CreatedFrankenmelonCriterion.Conditions(id, LootContextPredicate.EMPTY);
         }

        public boolean matches(Boolean input) {
            return input;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
            JsonObject json = new JsonObject();
            return json;
        }
    }
}