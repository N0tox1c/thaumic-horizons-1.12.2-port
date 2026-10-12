//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import com.kentington.thaumichorizons.common.lib.WandInteraction;
import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.EntityUtils;

public class TileSyntheticNode extends TileVisNode implements INode, IWandable {

    AspectList aspects;
    AspectList fractionalAspects;
    AspectList aspectsMax;
    private final NodeType nodeType;
    private final NodeModifier nodeModifier;
    public float rotation;
    float increment;
    public Entity drainEntity;
    public RayTraceResult drainCollision;
    public int drainColor;
    public Color targetColor;
    public Color color;

    public TileSyntheticNode() {
        this.aspects = new AspectList();
        this.fractionalAspects = new AspectList();
        this.aspectsMax = new AspectList();
        this.nodeType = NodeType.NORMAL;
        this.nodeModifier = null;
        this.rotation = 0.0f;
        this.increment = 1.0f;
        this.drainEntity = null;
        this.drainCollision = null;
        this.drainColor = 16777215;
        this.targetColor = new Color(16777215);
        this.color = new Color(16777215);
    }

    public void debug() {
        if (this.world.isRemote) {}
    }

    @Override
    public AspectList getAspects() {
        if (this.aspects.getAspects().length > 0 && this.aspects.getAspects()[0] != null) {
            return this.aspects;
        }
        return new AspectList();
    }

    public AspectList getMaxAspects() {
        return this.aspectsMax;
    }

    @Override
    public void setAspects(final AspectList aspects) {
        this.aspectsMax = aspects;
        this.aspects = new AspectList();
        this.fractionalAspects = new AspectList();
        for (final Aspect asp : aspects.getAspectsSortedAmount()) {
            this.aspects.add(asp, 0);
            this.fractionalAspects.add(asp, 0);
        }
    }

    @Override
    public boolean doesContainerAccept(final Aspect tag) {
        return true;
    }

    @Override
    public int addToContainer(final Aspect tag, final int amount) {
        this.aspects.add(tag, amount);
        int toReturn = amount;
        if (this.aspectsMax.getAmount(tag) < this.aspects.getAmount(tag)) {
            toReturn -= this.aspectsMax.getAmount(tag) - this.aspects.getAmount(tag);
            this.aspects.reduce(tag, this.aspectsMax.getAmount(tag) - this.aspects.getAmount(tag));
        }
        this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
        this.markDirty();
        return toReturn;
    }

    public boolean addFractionalToContainer(final Aspect tag, final int amount) {
        if (this.aspects.getAmount(tag) < this.aspectsMax.getAmount(tag)) {
            this.fractionalAspects.add(tag, amount);
            while (this.fractionalAspects.getAmount(tag) > 100) {
                this.addToContainer(tag, 1);
                this.fractionalAspects.remove(tag, 100);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(final Aspect tag, final int amount) {
        return this.aspects.reduce(tag, amount);
    }

    @Override
    public boolean takeFromContainer(final AspectList ot) {
        final Aspect[] toRemove = ot.getAspects();
        for (Aspect aspect : toRemove) {
            if (!this.aspects.reduce(aspect, ot.getAmount(aspect))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean doesContainerContainAmount(final Aspect tag, final int amount) {
        return this.aspects.getAmount(tag) >= amount;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        for (final Aspect asp : ot.getAspectsSortedAmount()) {
            if (this.aspects.getAmount(asp) <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int containerContains(final Aspect tag) {
        return this.aspects.getAmount(tag);
    }

    @Override
    public int onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player, final int x,
            final int y, final int z, final int side, final int md) {
        return -1;
    }

    @Override
    public ItemStack onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player) {
        WandInteraction.startUsing(player, wandstack);
        final ItemWandCasting wand = (ItemWandCasting) wandstack.getItem();
        wand.setObjectInUse(wandstack, this.pos.getX(), this.pos.getY(), this.pos.getZ());
        return wandstack;
    }

    @Override
    public void onUsingWandTick(final ItemStack wandstack, final EntityPlayer player, final int count) {
        boolean mfu = false;
        final ItemWandCasting wand = (ItemWandCasting) wandstack.getItem();
        final RayTraceResult movingobjectposition = WandInteraction.rayTrace(this.world, player, true);
        if (movingobjectposition == null
                || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
            player.stopActiveHand();
            return;
        } else {
            final int i = movingobjectposition.getBlockPos().getX();
            final int j = movingobjectposition.getBlockPos().getY();
            final int k = movingobjectposition.getBlockPos().getZ();
            if (i != this.pos.getX() || j != this.pos.getY() || k != this.pos.getZ()) {
                player.stopActiveHand();
            return;
            }
        }
        if (count % 5 == 0) {
            int tap = 1;
            if (ResearchManager.isResearchComplete(player.getName(), "NODETAPPER1")) {
                ++tap;
            }
            if (ResearchManager.isResearchComplete(player.getName(), "NODETAPPER2")) {
                ++tap;
            }
            final boolean preserve = !player.isSneaking()
                    && ResearchManager.isResearchComplete(player.getName(), "NODEPRESERVE")
                    && !wand.getRod(wandstack).getTag().equals("wood")
                    && !wand.getCap(wandstack).getTag().equals("iron");
            boolean success = false;
            Aspect aspect = null;
            if ((aspect = this.chooseRandomFilteredFromSource(wand.getAspectsWithRoom(wandstack), preserve)) != null) {
                final int amt = this.getAspects().getAmount(aspect);
                if (tap > amt) {
                    tap = amt;
                }
                if (preserve && tap == amt) {
                    --tap;
                }
                if (tap > 0) {
                    final int rem = wand.addVis(wandstack, aspect, tap, !this.world.isRemote);
                    if (rem < tap) {
                        this.drainColor = aspect.getColor();
                        if (!this.world.isRemote) {
                            this.takeFromContainer(aspect, tap - rem);
                            mfu = true;
                        }
                        success = true;
                    }
                }
            }
            if (success) {
                this.drainEntity = player;
                this.drainCollision = movingobjectposition;
                this.targetColor = new Color(this.drainColor);
            } else {
                this.drainEntity = null;
                this.drainCollision = null;
            }
            if (mfu) {
                this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
                this.markDirty();
            }
        }
        if (player.world.isRemote) {
            final int r = this.targetColor.getRed();
            final int g = this.targetColor.getGreen();
            final int b = this.targetColor.getBlue();
            final int r2 = this.color.getRed() * 4;
            final int g2 = this.color.getGreen() * 4;
            final int b2 = this.color.getBlue() * 4;
            this.color = new Color((r + r2) / 5, (g + g2) / 5, (b + b2) / 5);
        }
    }

    public Aspect chooseRandomFilteredFromSource(final AspectList filter, final boolean preserve) {
        final int min = preserve ? 1 : 0;
        final ArrayList<Aspect> validaspects = new ArrayList<>();
        for (final Aspect prim : this.aspects.getAspects()) {
            if (filter.getAmount(prim) > 0 && this.aspects.getAmount(prim) > min) {
                validaspects.add(prim);
            }
        }
        if (validaspects.size() == 0) {
            return null;
        }
        final Aspect asp = validaspects.get(this.world.rand.nextInt(validaspects.size()));
        if (asp != null && this.aspects.getAmount(asp) > min) {
            return asp;
        }
        return null;
    }

    @Override
    public void onWandStoppedUsing(final ItemStack wandstack, final World world, final EntityPlayer player,
            final int count) {
        this.drainEntity = null;
        this.drainCollision = null;
    }

    @Override
    public String getId() {
        return "SYNTHETIC";
    }

    @Override
    public AspectList getAspectsBase() {
        return this.aspectsMax;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NORMAL;
    }

    @Override
    public void setNodeType(final NodeType nodeType) {}

    @Override
    public void setNodeModifier(final NodeModifier nodeModifier) {}

    @Override
    public NodeModifier getNodeModifier() {
        return null;
    }

    @Override
    public int getNodeVisBase(final Aspect aspect) {
        return this.aspectsMax.getAmount(aspect);
    }

    @Override
    public void setNodeVisBase(final Aspect aspect, final short nodeVisBase) {}

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean isSource() {
        return false;
    }

    @Override
    public void update() {
        super.update();
        if (!this.world.isRemote) {
            for (final Aspect aspect : this.aspectsMax.getAspectsSortedAmount()) {
                if (aspect != null) {
                    if (this.aspects.getAmount(aspect) < this.aspectsMax.getAmount(aspect)) {
                        if (aspect.isPrimal()) {
                            final int drained = VisNetHandler
                                    .drainVis(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), aspect, 10);
                            if (drained > 0) {
                                this.addFractionalToContainer(aspect, drained);
                            }
                        } else {
                            final AspectList primalComponents = ResearchManager
                                    .reduceToPrimals(new AspectList().add(aspect, 1));
                            int actuallyDrained = 100;
                            for (final Aspect primal : primalComponents.getAspects()) {
                                final int drained2 = VisNetHandler
                                        .drainVis(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), primal, 10);
                                if (drained2 < actuallyDrained) {
                                    actuallyDrained = drained2;
                                }
                            }
                            if (actuallyDrained > 0) {
                                this.addFractionalToContainer(aspect, actuallyDrained);
                            }
                        }
                    }
                }
            }
        } else {
            this.rotation += this.increment;
            if (this.rotation > 360.0f) {
                this.rotation -= 360.0f;
            }
        }
    }

    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        final NBTTagList tlist = new NBTTagList();
        nbttagcompound.setTag("AspectsMax", tlist);
        for (final Aspect aspect : this.aspectsMax.getAspects()) {
            if (aspect != null) {
                final NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", this.aspectsMax.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        final NBTTagList tlist2 = new NBTTagList();
        nbttagcompound.setTag("Aspects", tlist2);
        for (final Aspect aspect2 : this.aspects.getAspects()) {
            if (aspect2 != null) {
                final NBTTagCompound f2 = new NBTTagCompound();
                f2.setString("key", aspect2.getTag());
                f2.setInteger("amount", this.aspects.getAmount(aspect2));
                tlist2.appendTag(f2);
            }
        }
        final NBTTagList tlist3 = new NBTTagList();
        nbttagcompound.setTag("AspectsFractional", tlist3);
        for (final Aspect aspect3 : this.fractionalAspects.getAspects()) {
            if (aspect3 != null) {
                final NBTTagCompound f3 = new NBTTagCompound();
                f3.setString("key", aspect3.getTag());
                f3.setInteger("amount", this.fractionalAspects.getAmount(aspect3));
                tlist3.appendTag(f3);
            }
        }
        if (this.drainEntity != null && this.drainEntity instanceof EntityPlayer) {
            nbttagcompound.setString("drainer", this.drainEntity.getName());
        }
        nbttagcompound.setInteger("draincolor", this.drainColor);
    }

    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        final AspectList al = new AspectList();
        final NBTTagList tlist = nbttagcompound.getTagList("AspectsMax", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            final NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                al.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
        this.aspectsMax = al.copy();
        final AspectList al2 = new AspectList();
        final NBTTagList tlist2 = nbttagcompound.getTagList("Aspects", 10);
        for (int i = 0; i < tlist2.tagCount(); ++i) {
            final NBTTagCompound rs2 = tlist2.getCompoundTagAt(i);
            if (rs2.hasKey("key")) {
                al2.add(Aspect.getAspect(rs2.getString("key")), rs2.getInteger("amount"));
            }
        }
        this.aspects = al2.copy();
        final AspectList al3 = new AspectList();
        final NBTTagList tlist3 = nbttagcompound.getTagList("AspectsFractional", 10);
        for (int k = 0; k < tlist3.tagCount(); ++k) {
            final NBTTagCompound rs3 = tlist3.getCompoundTagAt(k);
            if (rs3.hasKey("key")) {
                al3.add(Aspect.getAspect(rs3.getString("key")), rs3.getInteger("amount"));
            }
        }
        this.fractionalAspects = al3.copy();
        final String de = nbttagcompound.getString("drainer");
        if (de != null && de.length() > 0 && this.getWorld() != null) {
            this.drainEntity = this.getWorld().getPlayerEntityByName(de);
            if (this.drainEntity != null) {
                this.drainCollision = new RayTraceResult(new Vec3d(this.drainEntity.posX, this.drainEntity.posY, this.drainEntity.posZ), EnumFacing.DOWN, this.pos);
            }
        }
        this.drainColor = nbttagcompound.getInteger("draincolor");
    }

    public void addEssence(final EntityPlayer player) {
        final ItemStack heldItem = player.getHeldItemMainhand();
        // just in case
        if (heldItem == null) return;
        if (heldItem.getCount() == 0) return;

        if (heldItem.getItem() instanceof ItemWispEssence && ((ItemWispEssence) (heldItem.getItem())) == ConfigItems.itemWispEssence) {
            final ItemWispEssence essence = ((ItemWispEssence) (heldItem.getItem()));

            if (essence.getAspects(heldItem) == null) return;
            final Aspect[] aspectList = essence.getAspects(heldItem).getAspects();
            if (aspectList == null || aspectList.length == 0) return;
            final Aspect asp = aspectList[0];

            this.aspectsMax.add(asp, 4 * heldItem.getCount());
            this.aspects.add(asp, 0);
            this.fractionalAspects.add(asp, 0);

            heldItem.setCount(0);
            this.world.notifyBlockUpdate(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())), 3);
            this.markDirty();
        }
    }
}
