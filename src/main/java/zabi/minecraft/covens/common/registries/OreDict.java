package zabi.minecraft.covens.common.registries;

import net.minecraftforge.oredict.OreDictionary;
import zabi.minecraft.covens.common.block.ModBlocks;

public class OreDict {
	public static void registerAll() {
		OreDictionary.registerOre("logWood", ModBlocks.log_elder);
		OreDictionary.registerOre("logWood", ModBlocks.log_juniper);
		OreDictionary.registerOre("logWood", ModBlocks.log_yew);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_elder);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_juniper);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_yew);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_elder);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_juniper);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_yew);
		OreDictionary.registerOre("treeSapling", ModBlocks.sapling);
	}
}
