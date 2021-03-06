package zabi.minecraft.covens.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.registries.threads.SpinningThreadRecipe;

public class TileEntityThreadSpinner extends TileEntityBaseTickable implements IInventory, IAltarUser {
	
	public static final int MAX_TICKS = 200;
	public static final int POWER_PER_TICK = 6;
	
	private NonNullList<ItemStack> inputs = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	private ItemStack out = ItemStack.EMPTY;
	private int tickProcessed = 0;
	private String loadedRecipe = null;
	private TileEntityAltar te = null;
	private InvWrapper inventoryWrapper = new InvWrapper(this);

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		inputs.clear();
		ItemStackHelper.loadAllItems(tag.getCompoundTag("inputs"), inputs);
		out = new ItemStack(tag.getCompoundTag("output"));
		if (tag.hasKey("recipe")) loadedRecipe = tag.getString("recipe");
		else loadedRecipe = null;
		tickProcessed = tag.getInteger("ticks");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setTag("inputs", ItemStackHelper.saveAllItems(new NBTTagCompound(), inputs));
		tag.setTag("output", out.writeToNBT(new NBTTagCompound()));
		if (loadedRecipe!=null) tag.setString("recipe", loadedRecipe);
		tag.setInteger("ticks", tickProcessed);
	}

	@Override
	protected void tick() {
		if (loadedRecipe!=null && canStackResults()) {
			if (te==null || te.isInvalid()) te = TileEntityAltar.getClosest(getPos(), getWorld());
			if (te==null || te.isInvalid()) {
				loadedRecipe = null;
				return;
			}
			if (te.consumePower(POWER_PER_TICK, false)) {
				tickProcessed++;
				if (tickProcessed>=MAX_TICKS) {
					ItemStack result = SpinningThreadRecipe.REGISTRY.getValue(new ResourceLocation(loadedRecipe)).getResult();
					if (out.isEmpty()) out = result;
					else {
						out.setCount(out.getCount()+result.getCount());
					}
					for (int i=0; i<inputs.size(); i++) decrStackSize(i+1, 1);
				}
				markDirty();
			}
		} else {
			tickProcessed=0;
		}
	}
	
	private boolean canStackResults() {
		if (out.isEmpty()) return true;
		ItemStack recipeResult = SpinningThreadRecipe.REGISTRY.getValue(new ResourceLocation(loadedRecipe)).getResult();
		if (ItemStack.areItemStacksEqual(out, recipeResult)) {
			int sum = out.getCount() + recipeResult.getCount();
			return sum<=recipeResult.getMaxStackSize();
		}
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inventoryWrapper;
		return super.getCapability(capability, facing);
	}

	@Override
	public String getName() {
		return "ThreadSpinner";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean isEmpty() {
		if (!out.isEmpty()) return false;
		for (ItemStack i:inputs) if (!i.isEmpty()) return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index==0) return out;
		if (index>4) return null;
		return inputs.get(index-1);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack res = getStackInSlot(index).splitStack(count);
		if (getStackInSlot(index).isEmpty()) setInventorySlotContents(index, ItemStack.EMPTY);
		checkRecipe();
		markDirty();
		return res;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return decrStackSize(index, getStackInSlot(index).getCount());
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index==0) {
			out=stack;
			return;
		}
		if (index>4) {
			Log.w("Slot doesn't exist: "+index);
			return;
		}
		inputs.set(index-1, stack);
		checkRecipe();
		markDirty();
	}

	private void checkRecipe() {
		SpinningThreadRecipe recipe = SpinningThreadRecipe.getRecipe(inputs);
		if (recipe!=null) {
			loadedRecipe = recipe.getRegistryName().toString();
		} else {
			loadedRecipe = null;
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return (index!=0 && getStackInSlot(index).isEmpty());
	}

	@Override
	public int getField(int id) {
		if (id==0) return tickProcessed;
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if (id==0) tickProcessed = value;
		markDirty();
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {
		out = ItemStack.EMPTY;
		inputs.clear();
		checkRecipe();
		markDirty();
	}

	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}
	
}
