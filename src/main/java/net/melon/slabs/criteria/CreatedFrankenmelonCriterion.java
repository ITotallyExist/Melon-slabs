package net.melon.slabs.criteria;

import java.util.Optional;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion.Conditions;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import net.minecraft.advancement.criterion.CriterionConditions;


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


    public static class Conditions implements AbstractCriterion.Conditions {
        public Conditions(Optional<LootContextPredicate> lootCtxPredicate) {

        }

        public static CreatedFrankenmelonCriterion.Conditions create(Optional<LootContextPredicate> lootContextPredicate) {
            return new CreatedFrankenmelonCriterion.Conditions(lootContextPredicate);
        }

        public boolean matches(Boolean input) {
            return input;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'player'");
        }

    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConditionsCodec'");
    }
}