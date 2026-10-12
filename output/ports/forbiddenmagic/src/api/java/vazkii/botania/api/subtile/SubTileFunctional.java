/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2014, 8:03:44 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

/**
 * The basic class for a Functional Flower.
 */
public class SubTileFunctional extends SubTileEntity {

	public static final int RANGE = 10;

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	public int mana;

	public int redstoneSignal = 0;

	int sizeLastCheck = -1;
	TileEntity linkedPool = null;
	public int knownMana = -1;

	BlockPos cachedPoolCoordinates = null;

	/**
	 * If set to true, redstoneSignal will be updated every tick.
	 */
	public boolean acceptsRedstone() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		linkPool();

		if(linkedPool != null && isValidBinding()) {
			IManaPool pool = (IManaPool) linkedPool;
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.recieveMana(-manaToRemove);
			addMana(manaToRemove);
		}

		if(acceptsRedstone()) {
			redstoneSignal = 0;
			for(EnumFacing dir : EnumFacing.values()) {
				int redstoneSide = fox.spiteful.forbidden.compat.LegacyWorldAccess.getIndirectPowerLevelTo(supertile.getWorld(), supertile.getPos().getX() + dir.getXOffset(), supertile.getPos().getY() + dir.getYOffset(), supertile.getPos().getZ() + dir.getZOffset(), dir.ordinal());
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if(supertile.getWorld().isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			Color color = new Color(getColor());
			if(Math.random() > particleChance)
				BotaniaAPI.internalHandler.sparkleFX(supertile.getWorld(), supertile.getPos().getX() + 0.3 + Math.random() * 0.5, supertile.getPos().getY() + 0.5 + Math.random()  * 0.5, supertile.getPos().getZ() + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
		}
	}

	public void linkPool() {
		boolean needsNew = false;
		if(linkedPool == null) {
			needsNew = true;

			if(cachedPoolCoordinates != null) {
				needsNew = false;
				if(fox.spiteful.forbidden.compat.LegacyWorldAccess.blockExists(supertile.getWorld(), cachedPoolCoordinates.getX(), cachedPoolCoordinates.getY(), cachedPoolCoordinates.getZ())) {
					needsNew = true;
					TileEntity tileAt = fox.spiteful.forbidden.compat.LegacyWorldAccess.getTileEntity(supertile.getWorld(), cachedPoolCoordinates.getX(), cachedPoolCoordinates.getY(), cachedPoolCoordinates.getZ());
					if(tileAt != null && tileAt instanceof IManaPool && !tileAt.isInvalid()) {
						linkedPool = tileAt;
						needsNew = false;
					}
					cachedPoolCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = fox.spiteful.forbidden.compat.LegacyWorldAccess.getTileEntity(supertile.getWorld(), linkedPool.getPos().getX(), linkedPool.getPos().getY(), linkedPool.getPos().getZ());
			if(tileAt != null && tileAt instanceof IManaPool)
				linkedPool = tileAt;
		}

		if(needsNew && ticksExisted == 1) { // Only for new flowers
			IManaNetwork network = BotaniaAPI.internalHandler.getManaNetworkInstance();
			int size = network.getAllPoolsInWorld(supertile.getWorld()).size();
			if(BotaniaAPI.internalHandler.shouldForceCheck() || size != sizeLastCheck) {
				BlockPos coords = new BlockPos(supertile.getPos().getX(), supertile.getPos().getY(), supertile.getPos().getZ());
				linkedPool = network.getClosestPool(coords, supertile.getWorld(), RANGE);
				sizeLastCheck = size;
			}
		}
	}

	public void linkToForcefully(TileEntity pool) {
		linkedPool = pool;
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		knownMana = mana;
		player.world.playSound(null, player.getPosition(), net.minecraft.util.SoundEvent.REGISTRY.getObject(new net.minecraft.util.ResourceLocation("botania:ding")), net.minecraft.util.SoundCategory.PLAYERS, 0.1F, 1F);

		return super.onWanded(player, wand);
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);

		int x = cmp.getInteger(TAG_POOL_X);
		int y = cmp.getInteger(TAG_POOL_Y);
		int z = cmp.getInteger(TAG_POOL_Z);

		cachedPoolCoordinates = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);

		if(cachedPoolCoordinates != null) {
			cmp.setInteger(TAG_POOL_X, cachedPoolCoordinates.getX());
			cmp.setInteger(TAG_POOL_Y, cachedPoolCoordinates.getY());
			cmp.setInteger(TAG_POOL_Z, cachedPoolCoordinates.getZ());
		} else {
			int x = linkedPool == null ? 0 : linkedPool.getPos().getX();
			int y = linkedPool == null ? -1 : linkedPool.getPos().getY();
			int z = linkedPool == null ? 0 : linkedPool.getPos().getZ();

			cmp.setInteger(TAG_POOL_X, x);
			cmp.setInteger(TAG_POOL_Y, y);
			cmp.setInteger(TAG_POOL_Z, z);
		}
	}

	@Override
	public BlockPos getBinding() {
		if(linkedPool == null)
			return null;
		return new BlockPos(linkedPool.getPos().getX(), linkedPool.getPos().getY(), linkedPool.getPos().getZ());
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		int range = 10;
		range *= range;

		double dist = (x - supertile.getPos().getX()) * (x - supertile.getPos().getX()) + (y - supertile.getPos().getY()) * (y - supertile.getPos().getY()) + (z - supertile.getPos().getZ()) * (z - supertile.getPos().getZ());
		if(range >= dist) {
			TileEntity tile = fox.spiteful.forbidden.compat.LegacyWorldAccess.getTileEntity(player.world, x, y, z);
			if(tile instanceof IManaPool) {
				linkedPool = tile;
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedPool != null && !linkedPool.isInvalid() && fox.spiteful.forbidden.compat.LegacyWorldAccess.getTileEntity(supertile.getWorld(), linkedPool.getPos().getX(), linkedPool.getPos().getY(), linkedPool.getPos().getZ()) == linkedPool;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = I18n.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = getColor();
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, knownMana, getMaxMana(), name, res, BotaniaAPI.internalHandler.getBindDisplayForFlowerType(this), isValidBinding());
	}

}
