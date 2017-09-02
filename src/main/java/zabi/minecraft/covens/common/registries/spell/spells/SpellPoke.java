package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellPoke extends Spell {
	
	public SpellPoke(int cost, int color, EnumSpellType type, String name, String mod_id) {
		super(cost, color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		if (rtrace.typeOfHit==Type.ENTITY && rtrace.entityHit instanceof EntityLivingBase) {
			if (caster!=null) rtrace.entityHit.attackEntityFrom(new EntityDamageSource(DamageSource.MAGIC.getDamageType(), caster), 1f);
			else rtrace.entityHit.attackEntityFrom(DamageSource.MAGIC, 0.5f);
		}
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return true;
	}

}
