package zabi.minecraft.covens.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.crafting.VanillaRecipes;
import zabi.minecraft.covens.common.entity.ModEntities;
import zabi.minecraft.covens.common.inventory.GuiHandler;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.network.NetworkModRegistry;
import zabi.minecraft.covens.common.proxy.Proxy;
import zabi.minecraft.covens.common.registries.chimney.ModChimneyRecipes;
import zabi.minecraft.covens.common.registries.ritual.ModRituals;
import zabi.minecraft.covens.common.tileentity.ModTileEntities;

@Mod(modid = Reference.MID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "[1.12]")
@Mod.EventBusSubscriber
public class Covens {
	
	@Mod.Instance
	public static Covens INSTANCE;
	
	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static Proxy proxy;
	
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MID);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		proxy.setup();
		ModCreativeTabs.registerTabs();
		ModBlocks.registerAll();
		ModItems.registerAll();
		NetworkModRegistry.registerMessages(network);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		ModEntities.registerAll();
		ModTileEntities.registerAll();
		ModChimneyRecipes.registerAll();
		ModRituals.registerAll();
		proxy.preInit(evt);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		VanillaRecipes.registerAll();
		proxy.init(evt);
	}
	
}
