package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellInfuseLife extends Spell {

	public SpellInfuseLife(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		if (caster!=null && rtrace.typeOfHit==Type.ENTITY && rtrace.entityHit instanceof EntityLivingBase) {
			caster.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 1f);
			((EntityLivingBase)rtrace.entityHit).heal(1f);
		}
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return caster!=null;
	}

}
