package fox.spiteful.forbidden.tiles;

import fox.spiteful.forbidden.Config;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

/**
 * Original Forbidden Magic Wrath Cage, adapted to Forge 1.12.2.
 * Old NBT keys are preserved for legacy saved worlds.
 */
public class TileEntityWrathCage extends TileEntity implements ITickable, IAspectContainer, IEssentiaTransport {
    private static final int CAPACITY = 64;
    private final WrathSpawnerLogic spawnLogic = new WrathSpawnerLogic(this);
    public int wrath;
    public int sloth;
    public int special;
    public byte mode;
    private Aspect aspect = Aspect.GREED;

    public WrathSpawnerLogic getSpawnerLogic() { return spawnLogic; }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        spawnLogic.readFromNBT(compound);
        wrath = clamp(compound.getShort("Wrath"));
        sloth = clamp(compound.getShort("Sloth"));
        special = clamp(compound.getShort("Special"));
        mode = (byte) Math.floorMod(compound.getByte("Mode"), 3);
        aspect = spawnLogic.getAspect();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        spawnLogic.writeToNBT(compound);
        compound.setShort("Wrath", (short) wrath);
        compound.setShort("Sloth", (short) sloth);
        compound.setShort("Special", (short) special);
        compound.setByte("Mode", mode);
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        if (world != null) world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) { readFromNBT(tag); }

    /** Persist and propagate fuel/mob/mode changes to observing players. */
    public void sync() {
        markDirty();
        if (world != null && !world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public void update() {
        if (world == null || !Config.wrathCage) return;
        if (!world.isBlockPowered(pos)) spawnLogic.updateSpawner();
        if (!world.isRemote && Config.wrathCost > 0 && spawnLogic.isMobSet()
                && (special < CAPACITY || wrath < CAPACITY || sloth < CAPACITY)) {
            drawEssentia();
        }
    }

    private void drawEssentia() {
        for (EnumFacing side : EnumFacing.values()) {
            TileEntity neighbour = ThaumcraftApiHelper.getConnectableTile(world,
                    pos.getX(), pos.getY(), pos.getZ(), side);
            if (!(neighbour instanceof IEssentiaTransport)) continue;
            IEssentiaTransport transport = (IEssentiaTransport) neighbour;
            EnumFacing otherSide = side.getOpposite();
            if (!transport.canOutputTo(otherSide) || transport.getEssentiaAmount(otherSide) <= 0
                    || transport.getSuctionAmount(otherSide) >= getSuctionAmount(side)
                    || getSuctionAmount(side) < transport.getMinimumSuction()) continue;
            Aspect target = getSuctionType(side);
            if (target == null || transport.getEssentiaType(otherSide) != target) continue;
            if (getFreeCapacity(target) <= 0) continue;
            int extracted = transport.takeEssentia(target, 1, otherSide);
            if (extracted > 0) {
                addToContainer(target, extracted);
                return;
            }
        }
    }

    public void checkAspect() {
        Aspect next = spawnLogic.getAspect();
        if (aspect != next) special = 0;
        aspect = next;
        sync();
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return spawnLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }

    private static int clamp(int n) { return Math.max(0, Math.min(CAPACITY, n)); }

    public AspectList getAspects() {
        AspectList list = new AspectList();
        if (Config.wrathCost > 0 && spawnLogic.isMobSet()) {
            if (mode == 0) list.add(aspect, special);
            else if (mode == 1) list.add(DarkAspects.WRATH, wrath);
            else list.add(DarkAspects.SLOTH, sloth);
        }
        return list;
    }

    public void setAspects(AspectList ignored) { /* Transport is input-only. */ }

    public boolean doesContainerAccept(Aspect tag) {
        return Config.wrathCost > 0 && spawnLogic.isMobSet() && tag != null
                && (tag == aspect || tag == DarkAspects.WRATH || tag == DarkAspects.SLOTH);
    }

    private int getFreeCapacity(Aspect tag) {
        if (!doesContainerAccept(tag)) return 0;
        if (tag == aspect) return CAPACITY - special;
        if (tag == DarkAspects.WRATH) return CAPACITY - wrath;
        if (tag == DarkAspects.SLOTH) return CAPACITY - sloth;
        return 0;
    }

    public int addToContainer(Aspect tag, int amount) {
        if (amount <= 0) return 0;
        int added = Math.min(amount, Math.max(0, getFreeCapacity(tag)));
        if (added > 0) {
            if (tag == aspect) special += added;
            else if (tag == DarkAspects.WRATH) wrath += added;
            else if (tag == DarkAspects.SLOTH) sloth += added;
            sync();
        }
        return amount - added; // TC4 IAspectContainer: overflow, NOT accepted quantity.
    }

    public boolean takeFromContainer(Aspect tag, int amount) { return false; }
    @Deprecated public boolean takeFromContainer(AspectList tags) { return false; }
    @Deprecated public boolean doesContainerContain(AspectList tags) { return false; }

    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        return amount >= 0 && containerContains(tag) >= amount;
    }

    public int containerContains(Aspect tag) {
        if (tag == aspect) return special;
        if (tag == DarkAspects.WRATH) return wrath;
        if (tag == DarkAspects.SLOTH) return sloth;
        return 0;
    }

    public boolean isConnectable(EnumFacing face) { return Config.wrathCost > 0; }
    public boolean canInputFrom(EnumFacing face) { return Config.wrathCost > 0; }
    public boolean canOutputTo(EnumFacing face) { return false; }
    public void setSuction(Aspect tag, int amount) { /* Fixed suction. */ }

    public Aspect getSuctionType(EnumFacing face) {
        if (Config.wrathCost <= 0 || !spawnLogic.isMobSet()) return null;
        if (mode == 0) return special < CAPACITY ? aspect : null;
        if (mode == 1) return wrath < CAPACITY ? DarkAspects.WRATH : null;
        return sloth < CAPACITY ? DarkAspects.SLOTH : null;
    }
    public int getSuctionAmount(EnumFacing face) {
        return getSuctionType(face) == null ? 0 : 256;
    }
    public int takeEssentia(Aspect tag, int amount, EnumFacing face) { return 0; }
    public int addEssentia(Aspect tag, int amount, EnumFacing face) {
        if (!canInputFrom(face) || tag != getSuctionType(face) || amount <= 0) return 0;
        return amount - addToContainer(tag, amount); // TC4 transport: accepted quantity.
    }
    public Aspect getEssentiaType(EnumFacing face) { return getSuctionType(face); }
    public int getEssentiaAmount(EnumFacing face) {
        Aspect type = getSuctionType(face);
        return mode == 0 ? special : mode == 1 ? wrath : sloth;
    }
    public int getMinimumSuction() { return 9000; }
    public boolean renderExtendedTube() { return false; }
}
