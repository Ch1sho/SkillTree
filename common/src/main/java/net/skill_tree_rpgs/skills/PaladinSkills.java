package net.skill_tree_rpgs.skills;

import net.minecraft.util.Identifier;
import net.skill_tree_rpgs.SkillTreeMod;
import net.skill_tree_rpgs.effect.SkillEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PaladinSkills {
    public static final String NAMESPACE = SkillTreeMod.NAMESPACE;
    // Intentional package visibility
    public static final List<Skills.Entry> ENTRIES = new ArrayList<>();
    private static Skills.Entry add(Skills.Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final String FLASH_HEAL = "paladins:flash_heal";
    public static final String DIVINE_PROTECTION = "paladins:divine_protection";
    public static final String JUDGEMENT = "paladins:judgement";
    public static final String BATTLE_BANNER = "paladins:battle_banner";
    public static final String BLESSED_STRIKES = "paladins:blessed_strikes";
    public static final String IMMOLATION = "paladins:immolation";

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_1 = add(paladin_tier_2_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_1() {
        var effect = SkillEffects.DIVINE_STRENGTH;

        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_1");
        var title = "Divine Strength";
        var description = "Flash Heal increases Attack Damage by {bonus} for {effect_duration} sec.";

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FLASH_HEAL;

        var impact = SpellBuilder.Impacts.effectSet(SkillEffects.DIVINE_STRENGTH.id.toString(), 8, 0);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.05F, 0.1F)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA()),
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), SkillsCommon.MIGHT_COLOR)
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_spell_1_modifier_2 = add(paladin_tier_2_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_2_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_1_modifier_2");
        var title = "Cleanse";
        var cleanseCount = 1;
        var description = "Flash Heal attempts to cure the target, by reducing the strength of a harmful effect.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = FLASH_HEAL;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        var impact = SpellBuilder.Impacts.effectCleanse();
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SkillsCommon.HEAL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.25F, 0.3F
                ).color(Color.HOLY.toRGBA())
        };
        impact.action.status_effect.amplifier = cleanseCount;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_1 = add(paladin_tier_3_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_1");
        var title = "Pursuit of Justice";
        var description = "Divine Protection also increases your movement speed by {bonus}, for {effect_duration} sec.";
        var effect = SkillEffects.PURSUIT_OF_JUSTICE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = DIVINE_PROTECTION;

        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_1_modifier_2 = add(paladin_tier_3_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_3_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_1_modifier_2");
        var title = "Blessed Protection";
        var description = "Divine Protection provides {effect_amplifier_add} extra effect stack.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = DIVINE_PROTECTION;
        modifier.effect_amplifier_add = 1;
        modifier.effect_amplifier_cap_add = 1;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_2_modifier_1 = add(paladin_tier_3_spell_2_modifier_1());
    private static Skills.Entry paladin_tier_3_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_2_modifier_1");
        var title = "Empowered Judgement";
        var description = "Increases the damage of Judgement by {power_multiplier}.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = JUDGEMENT;
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.2F;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_spell_2_modifier_2 = add(paladin_tier_3_spell_2_modifier_2());
    private static Skills.Entry paladin_tier_3_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_spell_2_modifier_2");
        var title = "Judgement of Command";
        var description = "Judgement taunts enemies hit, forcing them to attack you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = JUDGEMENT;

        var impact = SpellBuilder.Impacts.taunt();
        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_aggro.id(), Color.RAGE),
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_1_modifier_1 = add(paladin_tier_4_spell_1_modifier_1());
    private static Skills.Entry paladin_tier_4_spell_1_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_1_modifier_1");
        var title = "Persistent Banner";
        var description = "Increases the duration of Battle Banner by {spawn_duration_add} sec.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BATTLE_BANNER;
        modifier.spawn_duration_add = 4;
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_1_modifier_2 = add(paladin_tier_4_spell_1_modifier_2());
    private static Skills.Entry paladin_tier_4_spell_1_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_1_modifier_2");
        var title = "Protective Banner";
        var description = "Battle Banner also reduces damage taken by {bonus}.";
        var effect = SkillEffects.BANNER_PROTECTION;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent( Math.abs( effect.config().firstModifier().value ) );
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BATTLE_BANNER;
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(), 2, 0);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    // ===================================================================================
    // Weak "root" spell-improvement nodes (structural parents of the two powerful mutex
    // nodes). Patterns come from the shared palette in SkillsCommon, picked per spell.
    // ===================================================================================

    public static final Skills.Entry paladin_tier_2_spell_1_root = add(SkillsCommon.powerRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_2_spell_1_root", FLASH_HEAL, "Flash Heal", 0.1F));
    public static final Skills.Entry paladin_tier_3_spell_1_root = add(SkillsCommon.lingerRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_3_spell_1_root", DIVINE_PROTECTION, "Divine Protection", 2F));
    public static final Skills.Entry paladin_tier_4_spell_1_root = add(SkillsCommon.cooldownRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_4_spell_1_root", BATTLE_BANNER, "Battle Banner", 5F));
    public static final Skills.Entry paladin_tier_2_spell_2_root = add(SkillsCommon.channelRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_2_spell_2_root", BLESSED_STRIKES, "Blessed Strikes", 1));
    public static final Skills.Entry paladin_tier_3_spell_2_root = add(SkillsCommon.critRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_3_spell_2_root", JUDGEMENT, "Judgement", 0.05F));
    public static final Skills.Entry paladin_tier_4_spell_2_root = add(SkillsCommon.radiusRoot(
            Skills.Category.PALADIN, SpellSchools.HEALING,
            "paladin_tier_4_spell_2_root", IMMOLATION, "Immolation", 1F));

    public static final Skills.Entry paladin_tier_2_spell_2_modifier_1 = add(paladin_tier_2_spell_2_modifier_1()); // Zeal
    private static Skills.Entry paladin_tier_2_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_2_modifier_1");
        var effect = SkillEffects.ZEAL;
        var title = "Zeal";
        var description = "Empowered strikes of Blessed Strikes fill you with " + effect.title
                + ", increasing your Healing Power by {bonus}, stacking up to {effect_amplifier_cap} times, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BLESSED_STRIKES;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;

        // Joins the stashed payload: runs on each seal-spending melee strike. The strike's damage
        // establishes harmful intent on the victim, so the buff rides on `apply_to_caster` — one
        // Zeal stack per empowered strike, feeding the paladin's Healing Power based spells.
        var impact = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 15, 1, 4);
        impact.action.apply_to_caster = true;
        spell.modifiers = List.of(modifier);
        modifier.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_spell_2_modifier_2 = add(paladin_tier_2_spell_2_modifier_2()); // Seal of Light
    private static Skills.Entry paladin_tier_2_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_spell_2_modifier_2");
        var title = "Seal of Light";
        var description = "Empowered strikes of Blessed Strikes also heal you for {heal}.";

        // PHYSICAL_MELEE school so the tooltip's {heal} estimation resolves against the same base
        // school the impact uses at runtime (Blessed Strikes' own school).
        var spell = SpellBuilder.createSpellModifier();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = BLESSED_STRIKES;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;

        // Joins the stashed payload: heals the paladin on each seal-spending strike. Same hybrid
        // power split as the base spell's damage (25% melee / 75% healing).
        var heal = SpellBuilder.Impacts.heal(0.25F);
        heal.action.apply_to_caster = true;
        heal.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 3F));
        heal.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SkillsCommon.HEAL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        8, 0.15F, 0.25F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        modifier.impacts = List.of(heal);
        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }
    public static final Skills.Entry paladin_tier_4_spell_2_modifier_1 = add(paladin_tier_4_spell_2_modifier_1());
    private static Skills.Entry paladin_tier_4_spell_2_modifier_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_2_modifier_1");
        var title = "Condemn";
        var description = "Immolation drags struck enemies towards you.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = SpellSchools.HEALING;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = IMMOLATION;

        // Radial pull: -Z in the ORIGIN frame points towards the blast centre (the caster),
        // with a small upward pop so victims are lifted off their footing.
        var pull = SpellBuilder.Impacts.velocity(
                Spell.Impact.Action.Velocity.Frame.ORIGIN, new Vector3f(0, 0.3F, -0.6F));
        pull.action.velocity.reset_velocity = true;
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(pull);

        spell.modifiers = List.of(modifier);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_4_spell_2_modifier_2 = add(paladin_tier_4_spell_2_modifier_2());
    private static Skills.Entry paladin_tier_4_spell_2_modifier_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_4_spell_2_modifier_2");
        var title = "Consecration";
        var description = "Immolation consecrates the ground beneath you, dealing {damage} damage to enemies, for {cloud_duration} sec.";

        var spell = SkillsCommon.createModifierAlikePassiveSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.specificSpellCast(IMMOLATION);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.aoe_source_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        // The consecrated-ground cloud formerly attached to Circle of Healing, re-homed
        // here at Immolation's own radius.
        var radius = 5.0F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = radius;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 5;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        12, 0.05F, 0.1F)
                        .color(SkillsCommon.HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.GROUND,
                        12, 0.05F, 0.15F)
                        .color(SkillsCommon.HOLY_COLOR),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        12, 0.05F, 0.15F)
                        .color(SkillsCommon.HOLY_COLOR).extent(radius),
        };
        spell.deliver.clouds = List.of(cloud);

        var impact = SpellBuilder.Impacts.damage(0.2F, 0.1F);
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        10, 0.4F, 0.4F)
                        .color(SkillsCommon.HOLY_COLOR),
        };
        impact.sound = new Sound(SkillSounds.priest_consecration_impact.id());
        spell.impacts = List.of(impact);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_1 = add(paladin_tier_1_passive_1()); // Redoubt
    private static Skills.Entry paladin_tier_1_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_1");
        var title = "Redoubt";
        var description = "Blocking with shield grants {bonus} armor, stacking up to {effect_amplifier_cap} times, lasting {effect_duration} sec.";

        var effect = SkillEffects.REDOUBT;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectAdd(SkillEffects.REDOUBT.id.toString(), 8, 1, 2);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.3F)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA())
        };
        impact.sound = new Sound(SkillSounds.paladin_redoubt.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_1_passive_2 = add(paladin_tier_1_passive_2()); // Vengeance
    private static Skills.Entry paladin_tier_1_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_1_passive_2");
        var effect = SkillEffects.VENGEANCE;
        var title = "Vengeance";
        var description = "Critical strikes grant " + effect.title
                + ", increasing Attack Damage by {bonus}, stacking up to {effect_amplifier_cap} times, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(effect.config().firstModifier().value);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        // Critical melee hits only — regular attacks and melee weapon skills alike.
        // No internal cooldown: every crit stacks and refreshes Vengeance.
        var attackTrigger = SpellBuilder.Triggers.meleeAttackImpact();
        attackTrigger.melee = new Spell.Trigger.MeleeCondition();
        attackTrigger.melee.critical = true;
        var skillTrigger = SpellBuilder.Triggers.meleeSkillImpact();
        skillTrigger.impact = new Spell.Trigger.ImpactCondition();
        skillTrigger.impact.critical = true;
        spell.passive.triggers = List.of(attackTrigger, skillTrigger);

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10, 1, 2);
        buff.action.apply_to_caster = true;
        buff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.3F)
                        .color(SkillsCommon.MIGHT_COLOR.toRGBA()),
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_fist.id(), SkillsCommon.MIGHT_COLOR)
        };
        buff.sound = new Sound(SkillSounds.paladin_crusader_activate.id());
        spell.impacts = List.of(buff);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_passive_1 = add(paladin_tier_2_passive_1()); // Conviction
    private static Skills.Entry paladin_tier_2_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_1");
        var title = "Conviction";
        var description = "Upon rolling, you have {trigger_chance} chance to reset the cooldown of Blessed Strikes and Flash Heal.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        trigger.chance = 0.5F;
        spell.passive.triggers = List.of(trigger);

        // Resets both tier 2 book spells — the retribution and the protection pick alike
        var impact = SpellBuilder.Impacts.resetCooldownActive(BLESSED_STRIKES);

        impact.particles = new ParticleBatch[]{
                SpellBuilder.Particles.popUpSign(SpellEngineParticles.sign_hourglass.id(), Color.HOLY),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.3F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SpellEngineSounds.SPELL_COOLDOWN_IMPACT.id());
        var flashHealReset = SpellBuilder.Impacts.resetCooldownActive(FLASH_HEAL);
        spell.impacts = List.of(impact, flashHealReset);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_2_passive_2 = add(paladin_tier_2_passive_2()); // Blessing of Freedom
    private static Skills.Entry paladin_tier_2_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_2_passive_2");
        var title = "Blessing of Freedom";
        var description = "Rolling breaks you free, removing all movement impairing effects.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.roll();
        spell.passive.triggers = List.of(trigger);

        // ALL-selector dispel: strips every harmful movement-impairing effect at once
        // (classification-based, so modded slows/snares are covered too)
        var impact = SpellBuilder.Impacts.effectRemoveMovementImpairing();
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        15, 0.2F, 0.3F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 10F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_1 = add(paladin_tier_3_passive_1()); // Ardent Defender (hp boost on low HP)
    private static Skills.Entry paladin_tier_3_passive_1() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_1");
        var effect = SkillEffects.ARDENT_DEFENDER;
        var title = "Ardent Defender";
        var healthThreshold = 0.3F;
        var description = "Upon taking damage below {threshold} health, your max health is increased by {bonus}, for {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var bonus = SpellTooltip.percent(Math.abs(effect.config().firstModifier().value));
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{threshold}", SpellTooltip.percent(healthThreshold));
        };

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.HEALTH;
        spell.range = 0;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger1 = SpellBuilder.Triggers.becomingLowHP(healthThreshold);
        trigger1.target_override = Spell.Trigger.TargetSelector.CASTER;
        var trigger2 = SpellBuilder.Triggers.damageIncomingFatal();
        trigger2.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger1, trigger2);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        buff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        30, 0.2F, 0.2F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        20, 0.3F, 0.3F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        30, 0.1F, 0.3F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        30, 0.1F, 0.2F)
                        .color(Color.HOLY.toRGBA()),
                SpellBuilder.Particles.area(SpellEngineParticles.aura_effect_415.id())
                        .scale(1.5F)
                        .color(Color.HOLY.toRGBA())
        };
        buff.sound = new Sound(SkillSounds.paladin_ardent_defender.id());
        var heal = SpellBuilder.Impacts.heal(0.5F);
        spell.impacts = List.of(buff, heal);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Skills.Entry(id, spell, title, description, mutator, EnumSet.of(Skills.Category.PALADIN));
    }

    public static final Skills.Entry paladin_tier_3_passive_2 = add(paladin_tier_3_passive_2()); // Divine Hammer
    private static Skills.Entry paladin_tier_3_passive_2() {
        var id = Identifier.of(NAMESPACE, "paladin_tier_3_passive_2");
        var title = "Divine Hammer";
        var description = "Melee attacks throw a hammer at the target, dealing {damage} damage, ricocheting {ricochet} to nearby enemies.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 5;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        var triggers = SpellBuilder.Triggers.meleeImpact();
        for (var trigger : triggers) {
            trigger.chance = 1F;
        }
        spell.passive.triggers = triggers;

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.direct_towards_target = true;
        spell.deliver.projectile.launch_properties.velocity = 0.6F;
        spell.deliver.projectile.projectile = new Spell.ProjectileData();
        spell.deliver.projectile.projectile.perks = new Spell.ProjectileData.Perks();
        spell.deliver.projectile.projectile.perks.ricochet_range = 8F;
        spell.deliver.projectile.projectile.perks.ricochet = 2;
        spell.deliver.projectile.projectile.perks.bounce = 3;

        var model = SpellBuilder.ProjectileModels.model("paladins:spell_projectile/judgement", 0.8F, LightEmission.RADIATE);
        model.rotate_degrees_per_tick = 20F;

        spell.deliver.projectile.projectile.client_data = new Spell.ProjectileData.Client();
        spell.deliver.projectile.projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(model);


        // Same hybrid power split as Judgement: 75% melee / 25% healing
        // (base PHYSICAL_MELEE weighs 1, healing weighs 1/3).
        var impact = SpellBuilder.Impacts.damage(0.5F, 0F);
        impact.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 1F / 3F));
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.HOLY,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.6F, 0.8F)
                        .color(SkillsCommon.HOLY_COLOR)
        };
        impact.sound = new Sound(SkillSounds.paladin_divine_hammer_impact.id());
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5F);

        return new Skills.Entry(id, spell, title, description, null, EnumSet.of(Skills.Category.PALADIN));
    }
}
