package zabi.minecraft.covens.common.ritual;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;

public class Ritual extends IForgeRegistryEntry.Impl<Ritual> {
	
	private int time, circles, altarStartingPower, tickPower;
	private NonNullList<ItemStack> input, output;
	public static final IForgeRegistry<Ritual> REGISTRY = new RegistryBuilder<Ritual>().setName(new ResourceLocation(Reference.MID, "rituals")).setType(Ritual.class).setIDRange(0, 200).create();
	
	/**
	 * Constructs a new ritual. To be registered within the registry
	 * 
	 * @param input a NonNullList<ItemStack> with all and every itemstack required to be a valid ritual
	 * @param output a NonNullList<ItemStack> with all and every itemstack that should get dropped on the ground when the ritual stops
	 * @param timeInTicks the time in ticks that the ritual takes to stop. Negative values will have the ritual going on indefinitely. Zero means that the effect/crafting is applied immediately
	 * @param circles is the byte annotation to define what circles are needed. It follows this pattern 332211TT where 33, 22, 11 are the glyph type of the nth circle, and TT the number of required circles, 0 being 1, 2 being 3. 3 (11) will always return a failed circle
	 * 
	 */
	public Ritual(@Nonnull NonNullList<ItemStack> input, @Nonnull NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		this.time = timeInTicks;
		this.input = input;
		this.output = output;
		this.circles = circles;
		this.altarStartingPower = altarStartingPower;
		this.tickPower = powerPerTick;
		if (input.size()==0) throw new IllegalArgumentException("Cannot have an empty input in a ritual");
	}
	
	public boolean isValid(EntityPlayer player, World world, BlockPos pos) {
		return true;
	}
	
	public void onUpdate(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data, int ticks) {}
	public void onFinish(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data) {}
	public void onStopped(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data) {}
	public void onStarted(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data) {}
	
	public int getTime() {
		return time;
	}
	
	public NonNullList<ItemStack> getOutput() {
		NonNullList<ItemStack> copy = NonNullList.<ItemStack>create();
		for (ItemStack i:output) copy.add(i);
		return copy;
	}
	
	public boolean isValidInput(List<ItemStack> recipe, boolean circles) {
		if (recipe.size()!=input.size()) return false;
		for (ItemStack is_recipe:input) {
			boolean found = false;
			for (ItemStack is_present:recipe){
				if (is_recipe.getItem() == is_present.getItem() && is_recipe.getMetadata()==is_present.getMetadata()) {
					found = true;
					continue;
				}
			}
			if (!found) return false;
		}
		return circles;
	}
	
	public int getCircles() {
		return circles;
	}
	
	public int getRequiredStartingPower() {
		return altarStartingPower;
	}
	
	public int getRunningPower() {
		return tickPower;
	}
	
}
