package net.melon.slabs.criteria;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
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

    protected Conditions conditionsFromJson(JsonObject json, EntityPredicate.Extended entityPredicate, AdvancementEntityPredicateDeserializer deserializer) {
		return new CreatedFrankenmelonCriterion.Conditions(id, entityPredicate);
	}

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier id, EntityPredicate.Extended entityPredicate) {
            super(id, entityPredicate);
        }

        public static CreatedFrankenmelonCriterion.Conditions create(Identifier id, EntityPredicate.Extended entityPredicate) {
            return new CreatedFrankenmelonCriterion.Conditions(id, entityPredicate);
        }

        public static CreatedFrankenmelonCriterion.Conditions create(Identifier id) {
            return new CreatedFrankenmelonCriterion.Conditions(id, EntityPredicate.Extended.EMPTY);
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