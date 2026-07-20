package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.target.SpellTarget;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class WarriorSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final String THROW = "rogues:throw";
    public static final String CHARGE = "rogues:charge";
    public static final String MORTAL_STRIKE = "rogues:mortal_strike";
    public static final String THROW_NET = "rogues:throw_net";
    public static final String SHOUT = "rogues:shout";
    public static final String LAST_STAND = "rogues:last_stand";

    public static final Skills.Entry warrior_tier_2_spell_1_modifier_1 = add(warrior_tier_2_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_2_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_1_modifier_1");
        var title = "Bouncing Throw";
        var description = "Shattering Throw ricochets to {ricochet} additional target.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = THROW;
        modifier.projectile_perks = Spell.ProjectileData.Perks.EMPTY();
        modifier.projectile_perks.ricochet = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_spell_1_modifier_2 = add(warrior_tier_2_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_1_modifier_2");
        var title = "Punching Throw";
        var description = "Shattering Throw deals {knockback_multiply_base} more knockback.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var bonus = 0.5F;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = THROW;
        modifier.knockback_multiply_base = bonus;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_spell_1_modifier_1 = add(warrior_tier_3_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_1_modifier_1");
        var title = "Endurance";
        var description = "Charge lasts {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = CHARGE;
        modifier.effect_duration_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_spell_1_modifier_2 = add(warrior_tier_3_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_1_modifier_2");
        var title = "Concussion Blow";
        var description = "Next attack after using Charge, stuns the target for {effect_duration} sec.";
        var stashEffect = SkillEffects.CONCUSSION_BLOW;

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        var trigger = SpellBuilder.Triggers.specificSpellCast(CHARGE);
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.triggers = List.of(
                SpellBuilder.Triggers.meleeAttackImpact());
        spell.deliver.stash_effect.consumed_next_tick = true;

        var impact = SpellBuilder.Impacts.stun(2F);
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_1_modifier_1 = add(warrior_tier_4_spell_1_modifier_1());
    private static Skills.Entry warrior_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_1_modifier_1");
        var title = "Recklessness";
        var description = "Mortal Strike also grants you Recklessness for {effect_duration} sec, increasing critical strike chance by 100%%, but also the damage you take by 100%%.";
        var effect = SkillEffects.RECKLESSNESS;
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = MORTAL_STRIKE;

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 6, 0);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_crit.id(), Color.RAGE),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.1F, 0.25F)
                        .color(Color.RAGE.toRGBA())
        };
        impact.sound = Sound.of(SkillSounds.recklessness_impact.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_1_modifier_2 = add(warrior_tier_4_spell_1_modifier_2());
    private static Skills.Entry warrior_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_1_modifier_2");
        var title = "Impaling Spikes";
        var description = "Mortal Strike erupts a line of spikes from the ground, dealing {damage} damage and launching struck enemies into the air.";

        // Under-the-hood passive: triggered whenever Mortal Strike is cast, it lays a forward row of
        // spike-clouds from the caster's feet — the same eruption mechanic as Wizards' Frost Spikes.
        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast(MORTAL_STRIKE);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 0.9F;
        cloud.volume.area.vertical_range_multiplier = 2F;
        cloud.delay_ticks = 0;
        // Damage exactly once: impact ticks fall at ages 0 (spawn, no hit) and SPIKE_APEX_TICK;
        // trimming the lifetime to just under two intervals despawns the cloud before a second hit,
        // so only that single hit lands. Same cloud timing as Wizards' Frost Spikes.
        cloud.impact_tick_interval = SPIKE_APEX_TICK;
        cloud.time_to_live_seconds = (SPIKE_APEX_TICK * 2 - 1) / 20F;
        cloud.spawn = new Spell.Delivery.Cloud.Spawn();
        cloud.spawn.sound = new Sound(SkillSounds.rock_spike_impact.id());
        cloud.spawn.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        18, 0.1F, 0.4F),
                // Cosy campfire smoke drifting up from the eruption: it carries its own slow rise
                // and long lifetime, so near-zero batch speed lets it hang and linger around the base.
                new ParticleBatch(
                        "minecraft:campfire_cosy_smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        3, 0F, 0.02F)
        };
        cloud.spawn.model_fx = impalingSpikeModelFx();
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();

        // Four spike-clouds marching straight forward from the caster, 1.5 blocks apart, the first
        // 1.5 blocks out; each erupts 2 ticks after the previous one (like Frost Spikes).
        var row = SpellBuilder.Placements.ray(5, 1.5F, 1.5F);
        SpellBuilder.Placements.delayCascade(row, 3);
        SpellBuilder.Placements.delayUniform(row, 6);
        cloud.placement = row.get(0);
        cloud.placement_delay_stacks = false;
        cloud.additional_placements = List.copyOf(row.subList(1, row.size()));

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.5F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.5F)
                        .color(Color.RAGE.toRGBA())
        };

        // Vertical launch of struck enemies as the spikes burst upward. Harmful, so knockback
        // resistance applies; reset_velocity makes the pop consistent regardless of prior motion.
        var launch = SpellBuilder.Impacts.velocityUp(0.8F);
        launch.action.velocity.reset_velocity = true;
        launch.action.velocity.intent = SpellTarget.Intent.HARMFUL;

        spell.impacts = List.of(damage, launch);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    /// Tick at which the cloud lands its single hit. Drives the cloud's impact interval and
    /// lifetime (= 2*this - 1 ticks). Matches Wizards' Frost Spikes; the spike model's own
    /// rise/sink animation runs on its own longer timeline (see {@link #impalingSpikeModelFx}).
    private static final int SPIKE_APEX_TICK = 5;
    /// Blocks a spike model rests below ground at the start/end of its eruption, so it stays hidden.
    private static final float SPIKE_BURY_DEPTH = 1.6F;

    /// A single ice spike erupting from the ground (reusing Wizards' second Frost Spike model): it
    /// shoots up to full height over 20 ticks, then sinks back underground — a ~2s eruption that
    /// outlives the cloud's brief single hit so the animation reads clearly. Matches the per-spike
    /// timing of Wizards' Frost Spikes. Spawned per cloud node via {@code cloud.spawn.model_fx}.
    private static List<ModelEffect> impalingSpikeModelFx() {
        var spike = ModelEffectBuilder.Preset.spike(
                        ModelEffectBuilder.create(SkillTreeMod.NAMESPACE + ":spell_effect/stone_spike")
                                .light(LightEmission.NONE)
                                .initialTranslateY(0.5F),
                        20, 0, true, SPIKE_BURY_DEPTH)
                .build();
        return List.of(spike);
    }

    public static final Skills.Entry warrior_tier_3_spell_2_modifier_1 = add(warrior_tier_3_spell_2_modifier_1());
    private static Skills.Entry warrior_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_2_modifier_1");
        var title = "Battle Shout";
        var description = "Shout increases Attack Damage of allies by {bonus}, lasting {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        spell.tooltip.show_range = false;

        var effect = SkillEffects.BATTLE_SHOUT;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 12;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.include_caster = true;

        var trigger = SpellBuilder.Triggers.specificSpellCast(SHOUT);
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 6, 0);
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), Color.RAGE)
        };
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_spell_2_modifier_2 = add(warrior_tier_3_spell_2_modifier_2());
    private static Skills.Entry warrior_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_spell_2_modifier_2");
        var title = "Challenging Shout";
        var description = "Shout taunts all affected enemies.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = SHOUT;

        var impact = SpellBuilder.Impacts.taunt();
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), Color.RAGE)
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry warrior_tier_2_spell_1_root = add(SkillsCommon.powerRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_2_spell_1_root", THROW, "Shattering Throw", 0.1F));
    public static final Skills.Entry warrior_tier_3_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_3_spell_1_root", CHARGE, "Charge", 2F));
    public static final Skills.Entry warrior_tier_4_spell_1_root = add(SkillsCommon.meleeRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_4_spell_1_root", MORTAL_STRIKE, "Mortal Strike", 0.1F));
    public static final Skills.Entry warrior_tier_2_spell_2_root = add(SkillsCommon.reachRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_2_spell_2_root", THROW_NET, "Throw Net", 5F));
    public static final Skills.Entry warrior_tier_3_spell_2_root = add(SkillsCommon.radiusRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_3_spell_2_root", SHOUT, "Shout", 2F));
    public static final Skills.Entry warrior_tier_4_spell_2_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.WARRIOR, ExternalSpellSchools.PHYSICAL_MELEE,
            "warrior_tier_4_spell_2_root", LAST_STAND, "Last Stand", 5F));

    // ===================================================================================
    // Powerful mutex nodes for the second spell of tiers 2 and 4 (spell_2):
    // Throw Net (T2), Last Stand (T4). Tier 3 spell_2 (Shout) has its nodes above.
    // ===================================================================================

    public static final Skills.Entry warrior_tier_2_spell_2_modifier_1 = add(warrior_tier_2_spell_2_modifier_1());
    private static Skills.Entry warrior_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_2_modifier_1");
        var title = "Ricocheting Net";
        var description = "Throw Net ricochets to {ricochet} additional targets.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = THROW_NET;
        modifier.projectile_perks = new Spell.ProjectileData.Perks();
        modifier.projectile_perks.ricochet = 2;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_spell_2_modifier_2 = add(warrior_tier_2_spell_2_modifier_2());
    private static Skills.Entry warrior_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_spell_2_modifier_2");
        var title = "Reinforced Nets";
        var description = "Net Trap holds its victims {effect_duration_add} sec longer.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = THROW_NET;
        modifier.effect_duration_add = 2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_2_modifier_1 = add(warrior_tier_4_spell_2_modifier_1());
    private static Skills.Entry warrior_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_2_modifier_1");
        var title = "Juggernaut";
        var effect = SkillEffects.JUGGERNAUT;
        var description = "Each stack of Last Stand also grows you in size by {bonus}.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = LAST_STAND;

        // Mirrors the base buff's stacking: one Juggernaut stack per channel release, same
        // 10s duration, so size tracks the Last Stand stack count.
        var growth = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10, 1, 4);
        growth.sound = Sound.of(SkillSounds.warrior_stomp.id());
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(growth);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_4_spell_2_modifier_2 = add(warrior_tier_4_spell_2_modifier_2());
    private static Skills.Entry warrior_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_4_spell_2_modifier_2");
        var title = "Revenge";
        var effect = SkillEffects.REVENGE;
        var description = "Blocking, or taking damage mitigatable by armor during Last Stand, increases your attack speed by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var lastStandActive = SpellBuilder.TargetConditions.hasEffect(Identifier.of("rogues", "last_stand"));

        var damageTrigger = SpellBuilder.Triggers.damageTaken();
        damageTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        damageTrigger.caster_conditions = List.of(lastStandActive);
        damageTrigger.damage = new Spell.Trigger.DamageCondition();
        damageTrigger.damage.damage_type = "!#minecraft:bypasses_armor";

        var blockTrigger = SpellBuilder.Triggers.shieldBlock();
        blockTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        blockTrigger.caster_conditions = List.of(lastStandActive);

        spell.passive.triggers = List.of(damageTrigger, blockTrigger);

        var haste = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 3, 1, 2);
        haste.action.apply_to_caster = true;
        haste.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        10, 0.15F, 0.3F)
                        .color(Color.from(0xff6633).toRGBA())
        };
        haste.sound = Sound.of(SkillSounds.recklessness_impact.id());
        spell.impacts = List.of(haste);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_1_passive_1 = add(warrior_tier_1_passive_1());
    private static Skills.Entry warrior_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_1_passive_1");
        var title = "Killing Spree";
        var description = "Killing an enemy increases Attack Damage by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        var effect = SkillEffects.KILLING_SPREE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.passive.triggers = SpellBuilder.Triggers.meleeKills();

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(Color.RAGE.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_1_passive_2 = add(warrior_tier_1_passive_2());
    private static Skills.Entry warrior_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_1_passive_2");
        var effect = SkillEffects.VITALITY;
        var title = "Vitality";
        var description = "Blocking with shield has {trigger_chance} chance to increase your Evasion Chance by {bonus}, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        trigger.chance = 0.5F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 8, 1, 2);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(Color.NATURE.toRGBA())
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_passive_1 = add(warrior_tier_2_passive_1());
    private static Skills.Entry warrior_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_passive_1");
        var title = "Intercept";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Charge.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.resetCooldownActive(CHARGE);
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.RAGE)
        };
        impact.action.apply_to_caster = true;
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_2_passive_2 = add(warrior_tier_2_passive_2());
    private static Skills.Entry warrior_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_2_passive_2");
        var title = "Second Wind";
        var description = "Upon rolling, you have {trigger_chance} chance to restore 10%% of your total health.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.HEALTH;   // power = max HP → heal(0.1) = 10% HP
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.heal(0.1F);
        impact.action.apply_to_caster = true;
        impact.particles = SkillsCommon.leechImpactParticles();
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_passive_1 = add(warrior_tier_3_passive_1()); // Enrage (on damage taken, gain Enrage effect)
    private static Skills.Entry warrior_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_passive_1");
        var effect = SkillEffects.ENRAGE;
        var title = effect.title;
        var description = "Taking damage has {trigger_chance_1} chance to apply Enrage effect, increasing your Size and Attack Speed by {bonus} but also the damage you take, stacking up to {effect_amplifier_cap} times, lasting {stash_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.damageTaken();
        trigger.chance = 0.25F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var activateParticles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.3F, 0.5F)
                        .color(Color.RAGE.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.3F, 0.5F)
                        .invert()
                        .color(Color.RAGE.toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .origin(ParticleBatch.Origin.CENTER)
                        .scale(1.5F)
                        .color(Color.RAGE.toRGBA())
        };

        spell.release.particles = activateParticles;
        spell.release.sound = new Sound(SkillSounds.warrior_enrage.id());

        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 10F, List.of(
                SpellBuilder.Triggers.damageTaken()
        ));
        spell.deliver.stash_effect.consume = 0;

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10F, 1, 2);
        buff.action.apply_to_caster = true;
        buff.action.status_effect.refresh_duration = false;
        buff.particles = activateParticles;
        // buff.sound = new Sound(SkillTreeSounds.warrior_enrage.id());
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }

    public static final Skills.Entry warrior_tier_3_passive_2 = add(warrior_tier_3_passive_2()); // Shockwave (like Ardent Defender)
    private static Skills.Entry warrior_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "warrior_tier_3_passive_2");
        var title = "Shockwave";
        float healthThreshold = 0.3F;
        float radius = 5F;
        var description = "Taking damage below {threshold} causes a shockwave, stunning enemies nearby for {effect_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = radius;

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var threshold = SpellTooltip.percent(healthThreshold);
            return args.description().replace("{threshold}", threshold);
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();

        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.6F, 0.8F),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.4F, 0.4F),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.6F, 0.6F),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .scale(radius * 0.8F)
                        .color(Color.from(0xe6e6e6).toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.area_effect_658.id())
                        .scale(radius)
                        .color(Color.from(0xa6a6a6).toRGBA())
        };
        spell.release.sound = new Sound(SkillSounds.warrior_shockwave.id());

        var trigger = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var stun = SpellBuilder.Impacts.effectSet(SpellEngineEffects.STUN.id.toString(), 4, 0);
        stun.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.2F, 0.3F)
        };
        spell.impacts = List.of(stun);

        SpellBuilder.Cost.cooldown(spell, 30F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.WARRIOR));
    }
}
