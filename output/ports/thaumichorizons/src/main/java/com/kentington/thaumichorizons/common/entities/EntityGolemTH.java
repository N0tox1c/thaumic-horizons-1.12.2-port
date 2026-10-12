//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.util.math.BlockPos;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.client.lib.GolemTHTexture;
import com.kentington.thaumichorizons.common.entities.ai.EntityAIFollowPlayer;
import com.kentington.thaumichorizons.common.lib.networking.PacketFXBlocksplosion;
import com.kentington.thaumichorizons.common.lib.networking.PacketHandler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAvoidCreeperSwell;
import thaumcraft.common.entities.ai.combat.AIGolemAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AIHurtByTarget;
import thaumcraft.common.entities.ai.combat.AINearestAttackableTarget;
import thaumcraft.common.entities.ai.misc.AIOpenDoor;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;

public class EntityGolemTH extends EntityGolemBase {

private static final net.minecraft.network.datasync.DataParameter<Byte> TH_TYPE =
            net.minecraft.network.datasync.EntityDataManager.createKey(EntityGolemTH.class,
                    net.minecraft.network.datasync.DataSerializers.BYTE);

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TH_TYPE, (byte) EnumGolemTHType.SAND.ordinal());
    }

    public ResourceLocation texture;
    public Block blocky;
    public int md;
    public int ticksAlive;
    public int voidCount;
    public boolean berserk;
    public boolean kaboom;
    public EnumGolemTHType type;

    public EntityGolemTH(final World par1World) {
        super(par1World);
        this.texture = null;
        this.ticksAlive = 0;
        this.voidCount = 0;
        this.type = EnumGolemTHType.SAND;
    }

    public void loadGolem(final double x, final double y, final double z, final Block block, final int md,
            final int ticksAlive, final boolean adv, final boolean berserk, final boolean kaboom) {
        this.setPosition(x, y, z);
        this.md = md;
        this.blocky = block;
        this.ticksAlive = ticksAlive;
        this.advanced = adv;
        this.berserk = berserk;
        this.kaboom = kaboom;
        this.loadGolemTexturesAndStats();
        this.setupGolem();
        this.upgrades = new byte[this.type.upgrades + (this.advanced ? 1 : 0)];
        Arrays.fill(this.upgrades, (byte) -1);
    }

    public void loadGolemTexturesAndStats() {
        if (this.blocky == null) {
            this.type = EnumGolemTHType.VOID;
            return;
        }
        final Material m = this.blocky.getStateFromMeta(this.md).getMaterial();
        if (m == Material.GRASS) {
            this.type = EnumGolemTHType.GRASS;
        } else if (m == Material.GROUND) {
            this.type = EnumGolemTHType.DIRT;
        } else if (m == Material.WOOD || m == Material.GOURD) {
            this.type = EnumGolemTHType.WOOD;
        } else if (m == Material.LEAVES || m == Material.PLANTS || m == Material.VINE) {
            this.type = EnumGolemTHType.PLANT;
        } else if (m == Material.ROCK) {
            this.type = EnumGolemTHType.ROCK;
        } else if (m == Material.IRON || m == Material.ANVIL) {
            this.type = EnumGolemTHType.METAL;
        } else if (m == Material.SPONGE || m == Material.CLOTH || m == Material.CARPET) {
            this.type = EnumGolemTHType.CLOTH;
        } else if (m == Material.SAND) {
            this.type = EnumGolemTHType.SAND;
        } else if (m == Material.REDSTONE_LIGHT || m == Material.CIRCUITS) {
            this.type = EnumGolemTHType.REDSTONE;
        } else if (m == Material.TNT) {
            this.type = EnumGolemTHType.TNT;
        } else if (m == Material.ICE || m == Material.PACKED_ICE || m == Material.SNOW || m == Material.CRAFTED_SNOW) {
            this.type = EnumGolemTHType.ICE;
        } else if (m == Material.CACTUS) {
            this.type = EnumGolemTHType.CACTUS;
        } else if (m == Material.CAKE) {
            this.type = EnumGolemTHType.CAKE;
        } else if (m == Material.WEB) {
            this.type = EnumGolemTHType.WEB;
        } else {
            this.type = EnumGolemTHType.ROCK;
        }
        if (this.world.isRemote) {
            this.loadTexture();
        }
    }

    public boolean isValidTarget(final Entity target) {
        if (!this.berserk) {
            return super.isValidTarget(target) || target instanceof EntityCreeper;
        }
        return target.isEntityAlive()
                && (!(target instanceof EntityPlayer) || !target.getName().equals(this.getOwnerName()))
                && !target.getName().equals(this.getName());
    }

    public boolean setupGolem() {
        super.setupGolem();
        if (this.getCore() == -1) {
            this.tasks.addTask(0, new AIAvoidCreeperSwell(this));
            this.targetTasks.addTask(1, new AIHurtByTarget(this, false));
            this.targetTasks.addTask(2, new AINearestAttackableTarget(this, 0, true));
            this.tasks.addTask(3, new AIGolemAttackOnCollide(this));
            this.tasks.addTask(5, new AIOpenDoor(this, true));
            if (!this.kaboom) {
                this.tasks.addTask(6, new EntityAIFollowPlayer(this, this.getAIMoveSpeed() * 3.0f, 2.0f, 12.0f));
            } else {
                this.tasks.addTask(6, new EntityAIFollowPlayer(this, this.getAIMoveSpeed() * 3.0f, 8.0f, 12.0f));
            }
            this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
            this.tasks.addTask(8, new EntityAILookIdle(this));
            this.paused = false;
            this.inactive = false;
            this.bootup = 0.0f;
        }
        if (!this.world.isRemote) {
            this.dataManager.set(TH_TYPE, (byte) this.type.ordinal());
        }
        this.setPathPriority(net.minecraft.pathfinding.PathNodeType.WATER,
                this.getGolemTHType() != EnumGolemTHType.ROCK && this.getGolemTHType() != EnumGolemTHType.METAL
                        && this.getGolemTHType() != EnumGolemTHType.REDSTONE ? -1.0f : 0.0f);
        final String decor = this.getGolemDecoration();
        final int bonus = decor == null ? 0 : (decor.contains("H") ? 5 : 0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getGolemTHType().health + bonus);
        return true;
    }

    public boolean attackEntityFrom(final DamageSource ds, float par2) {
        this.paused = false;
        if (ds == DamageSource.CACTUS) {
            return false;
        }
        if (this.blocky == ConfigBlocks.blockCosmeticSolid && this.md == 4 && ds == DamageSource.MAGIC) {
            par2 *= 0.5f;
        }
        if (ds.getImmediateSource() != null && this.getUpgradeAmount(5) > 0
                && ds.getImmediateSource().getEntityId() != this.getEntityId()) {
            ds.getImmediateSource().attackEntityFrom(
                    DamageSource.causeThornsDamage(this),
                    (float) (this.getUpgradeAmount(5) * 2 + this.rand.nextInt(2 * this.getUpgradeAmount(5))));
            ds.getImmediateSource().playSound(com.kentington.thaumichorizons.common.lib.THSoundBridge.lookup("damage.thorns"), 0.5f, 1.0f);
        } else if (ds.getImmediateSource() != null && this.blocky == Blocks.CACTUS
                && ds.getImmediateSource().getEntityId() != this.getEntityId()) {
                    ds.getImmediateSource().attackEntityFrom(
                            DamageSource.causeThornsDamage(this),
                            (float) (this.getUpgradeAmount(5) * 2 + this.rand.nextInt(2)));
                    ds.getImmediateSource().playSound(com.kentington.thaumichorizons.common.lib.THSoundBridge.lookup("damage.thorns"), 0.5f, 1.0f);
                }
        return super.attackEntityFrom(ds, par2);
    }

    public int getCarryLimit() {
        int base = this.type.carry;
        if (this.world.isRemote) {
            base = this.getGolemTHType().carry;
        }
        base += Math.min(16, Math.max(4, base)) * this.getUpgradeAmount(1);
        return base;
    }

    public float getAIMoveSpeed() {
        if (this.paused || this.inactive) {
            return 0.0f;
        }
        float speed = this.type.speed * (this.decoration.contains("B") ? 1.1f : 1.0f);
        if (this.decoration.contains("P")) {
            speed *= 0.88f;
        }
        speed *= 1.0f + this.getUpgradeAmount(0) * 0.15f;
        if (this.advanced) {
            speed *= 1.1f;
        }
        if (this.isInWater()
                && (this.getGolemTHType() == EnumGolemTHType.ROCK || this.getGolemTHType() == EnumGolemTHType.METAL
                        || this.getGolemTHType() == EnumGolemTHType.REDSTONE)) {
            speed *= 2.0f;
        }
        return speed;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.getCore() == -1 && this.ticksAlive > 0) {
                --this.ticksAlive;
                if (this.world.getPlayerEntityByName(this.getOwnerName()) != null) {
                    this.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) this.world.getPlayerEntityByName(this.getOwnerName()).posX, (int) this.world.getPlayerEntityByName(this.getOwnerName()).posY, (int) this.world.getPlayerEntityByName(this.getOwnerName()).posZ), 16);
                }
            } else if (this.getCore() == -1 && this.ticksAlive != -420) {
                this.die();
            } else if (this.getCore() == -1) {
                if (this.ticksExisted % 10 == 0 && this.world.rand.nextInt(500) == 0) {
                    final EntityPlayer player = this.world.getPlayerEntityByName(this.getOwnerName());
                    switch (this.voidCount) {
    case 0: {
                            if (player != null) {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.DARK_PURPLE
                                                        + I18n
                                                                .translateToLocal("thaumichorizons.golemWarning1")));
                                break;
                            }
                        } break;
    case 1: {
                            if (player != null) {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.DARK_PURPLE
                                                        + I18n
                                                                .translateToLocal("thaumichorizons.golemWarning2")));
                                break;
                            }
                        } break;
    case 2: {
                            if (player != null) {
                                player.sendMessage(
                                        new TextComponentString(
                                                TextFormatting.ITALIC + ""
                                                        + TextFormatting.DARK_PURPLE
                                                        + I18n
                                                                .translateToLocal("thaumichorizons.golemWarning3")));
                                break;
                            }
                        } break;
    case 3: {
                            this.die();
                            Thaumcraft.proxy
                                    .burst(this.world, this.posX, this.posY + this.height / 2.0f, this.posZ, 2.0f);
                            final EntityEldritchGuardian scaryThing = new EntityEldritchGuardian(this.world);
                            scaryThing.setPosition(this.posX, this.posY, this.posZ);
                            this.world.spawnEntity(scaryThing);
                            scaryThing.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 32);
                        } break;
}
                    ++this.voidCount;
                }
                if (this.world.getPlayerEntityByName(this.getOwnerName()) != null) {
                    this.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) this.world.getPlayerEntityByName(this.getOwnerName()).posX, (int) this.world.getPlayerEntityByName(this.getOwnerName()).posY, (int) this.world.getPlayerEntityByName(this.getOwnerName()).posZ), 16);
                }
            }
            if (this.regenTimer <= 0) {
                this.regenTimer = this.type.regenDelay;
                if (this.decoration.contains("F")) {
                    this.regenTimer *= (int) 0.66f;
                }
                if (!this.world.isRemote && this.getHealth() < this.getMaxHealth()) {
                    this.world.setEntityState(this, (byte) 5);
                    this.heal(1.0f);
                }
            }
        }
    }

    public boolean isWithinHomeDistanceFromPosition(final int par1, final int par2, final int par3) {
        return this.getCore() == -1 || super.isWithinHomeDistanceFromPosition(par1, par2, par3);
    }

    public void setFire(final int par1) {
        if (!this.type.fireResist) {
            super.setFire(par1);
        }
    }

    public void writeEntityToNBT(final NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("GolemTypeTH", (byte) this.type.ordinal());
        nbt.setByte("GolemType", (byte) EnumGolemType.FLESH.ordinal());
        if (this.blocky != null) {
            final String s = "block";
            final Block blocky = this.blocky;
            nbt.setInteger(s, Block.getIdFromBlock(this.blocky));
            nbt.setString("blockName", this.blocky.getRegistryName().toString());
        } else {
            nbt.setInteger("block", 0);
        }
        nbt.setByteArray("upgrades", this.upgrades);
        nbt.setInteger("metadata", this.md);
        nbt.setInteger("ticksAlive", this.ticksAlive);
        nbt.setInteger("voidCount", this.voidCount);
        nbt.setBoolean("berserk", this.berserk);
        nbt.setBoolean("explosive", this.kaboom);
    }

    public void readEntityFromNBT(final NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.type = EnumGolemTHType.getType(nbt.getByte("GolemTypeTH"));
        this.upgrades = new byte[this.type.upgrades + (this.advanced ? 1 : 0)];
        final int ul = this.upgrades.length;
        this.upgrades = nbt.getByteArray("upgrades");
        if (ul != this.upgrades.length) {
            final byte[] tt = new byte[ul];
            for (int a = 0; a < ul; ++a) {
                tt[a] = -1;
            }
            for (int a = 0; a < this.upgrades.length; ++a) {
                if (a < ul) {
                    tt[a] = this.upgrades[a];
                }
            }
            this.upgrades = tt;
        }
        StringBuilder st = new StringBuilder();
        for (final byte c : this.upgrades) {
            st.append(Integer.toHexString(c));
        }
        for (int slot = 0; slot < this.upgrades.length; slot++) this.setUpgrade(slot, this.upgrades[slot]);
        this.dataManager.set(TH_TYPE, (byte) this.type.ordinal());
        this.blocky = nbt.hasKey("blockName") ? Block.getBlockFromName(nbt.getString("blockName")) : Block.getBlockById(nbt.getInteger("block"));
        this.md = nbt.getInteger("metadata");
        this.ticksAlive = nbt.getInteger("ticksAlive");
        this.voidCount = nbt.getInteger("voidCount");
        this.berserk = nbt.getBoolean("berserk");
        this.kaboom = nbt.getBoolean("explosive");
        if (this.world.isRemote) {
            this.loadTexture();
        }
    }

    protected void damageEntity(final DamageSource ds, final float par2) {
        if (ds.isFireDamage() && this.type.fireResist) {
            return;
        }
        super.damageEntity(ds, par2);
    }

    public int getTotalArmorValue() {
        int var1 = super.getTotalArmorValue() + this.type.armor;
        if (this.decoration.contains("V")) {
            ++var1;
        }
        if (this.decoration.contains("P")) {
            var1 += 4;
        }
        if (var1 > 20) {
            var1 = 20;
        }
        return var1;
    }

    public EnumGolemTHType getGolemTHType() {
        return EnumGolemTHType.getType(this.dataManager.get(TH_TYPE));
    }

    @SideOnly(Side.CLIENT)
    public void loadTexture() {
        if (this.blocky == null || this.blocky == Blocks.AIR) {
            return;
        }
        final TextureAtlasSprite bottom = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(0));
        final TextureAtlasSprite top = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(1));
        final TextureAtlasSprite east = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(2));
        final TextureAtlasSprite west = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(3));
        final TextureAtlasSprite north = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(4));
        final TextureAtlasSprite south = com.kentington.thaumichorizons.client.lib.BlockSprites.face(this.blocky.getStateFromMeta(this.md), net.minecraft.util.EnumFacing.byIndex(5));
        final GolemTHTexture newTex = new GolemTHTexture(
                new TextureAtlasSprite[] { bottom, top, east, west, north, south },
                (this.blocky == Blocks.GRASS) ? 1 : ((this.blocky == Blocks.CAKE) ? 2 : 0));
        this.texture = new ResourceLocation("thaumichorizons", "TEMPGOLEMTEX" + this.getEntityId());
        Minecraft.getMinecraft().getTextureManager().loadTexture(this.texture, newTex);
    }

    public boolean processInteract(final EntityPlayer player, final net.minecraft.util.EnumHand hand) {
        if (player.isSneaking()) {
            return false;
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (this.world.isRemote) return true;
        if (this.getCore() == -1 && !itemstack.isEmpty()
                && itemstack.getItem() == ConfigItems.itemGolemCore
                && itemstack.getItemDamage() != 100) {
            this.setHomePosAndDistance(new net.minecraft.util.math.BlockPos((int) Math.round(this.posX - 0.5), (int) Math.round(this.posY), (int) Math.round(this.posZ - 0.5)), 32);
            this.setCore((byte) itemstack.getItemDamage());
            this.setupGolem();
            this.setupGolemInventory();
            itemstack.shrink(1);
            if (itemstack.getCount() <= 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(this.world, this, "thaumcraft:upgrade", 0.5f, 1.0f);
            player.swingArm(hand);
            this.world.setEntityState(this, (byte) 7);
            return true;
        }
        if (this.getCore() == -1) {
            return false;
        }
        if (!itemstack.isEmpty() && itemstack.getItem() == ConfigItems.itemGolemUpgrade) {
            for (int a = 0; a < this.upgrades.length; ++a) {
                if (this.getUpgrade(a) == -1 && this.getUpgradeAmount(itemstack.getItemDamage()) < 2) {
                    this.setUpgrade(a, (byte) itemstack.getItemDamage());
                    this.setupGolem();
                    this.setupGolemInventory();
                    itemstack.shrink(1);
                    if (itemstack.getCount() <= 0) {
                        player.setHeldItem(hand, ItemStack.EMPTY);
                    }
                    com.kentington.thaumichorizons.common.lib.THSoundBridge.playAtEntity(this.world, this, "thaumcraft:upgrade", 0.5f, 1.0f);
                    player.swingArm(hand);
                    return true;
                }
            }
            return false;
        }
        return super.processInteract(player, hand);
    }

    public void die() {
        this.setDead();
        this.spawnExplosionParticle();
        com.kentington.thaumichorizons.common.lib.THSoundBridge.play(this.world, this.posX, this.posY, this.posZ, "thaumcraft:craftfail", 1.0f, 1.0f);
        if (this.blocky != null && this.blocky != Blocks.AIR && this.blocky.getStateFromMeta(this.md).getMaterial() == Material.TNT) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4.0f, true);
            return;
        }
        if (this.kaboom) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3.0f, true);
            return;
        }
        if (this.world.isAirBlock(new BlockPos((int) Math.round(this.posX - 0.5), (int) Math.round(this.posY), (int) Math.round(this.posZ - 0.5)))) {
            if (this.blocky != null && this.blocky != Blocks.AIR) {
                this.world.setBlockState(new BlockPos((int) Math.round(this.posX - 0.5), (int) Math.round(this.posY), (int) Math.round(this.posZ - 0.5)), this.blocky.getStateFromMeta(this.md), 3);
                final SimpleNetworkWrapper instance = PacketHandler.INSTANCE;
                final Block blocky = this.blocky;
                instance.sendToAllAround(
                        new PacketFXBlocksplosion(
                                Block.getIdFromBlock(this.blocky),
                                this.md,
                                (int) Math.round(this.posX - 0.5),
                                (int) Math.round(this.posY),
                                (int) Math.round(this.posZ - 0.5)),
                        new NetworkRegistry.TargetPoint(
                                this.world.provider.getDimension(),
                                (int) Math.round(this.posX - 0.5),
                                (int) Math.round(this.posY),
                                (int) Math.round(this.posZ - 0.5),
                                32.0));
            }
            return;
        }
        for (int x = -1; x < 2; ++x) {
            for (int z = -1; z < 2; ++z) {
                if (this.world.isAirBlock(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY), (int) Math.round(this.posZ - 0.5) + z))) {
                    if (this.blocky != null && this.blocky != Blocks.AIR) {
                        this.world.setBlockState(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY), (int) Math.round(this.posZ - 0.5) + z), this.blocky.getStateFromMeta(this.md), 3);
                        final SimpleNetworkWrapper instance2 = PacketHandler.INSTANCE;
                        final Block blocky2 = this.blocky;
                        instance2.sendToAllAround(
                                new PacketFXBlocksplosion(
                                        Block.getIdFromBlock(this.blocky),
                                        this.md,
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY),
                                        (int) Math.round(this.posZ - 0.5) + z),
                                new NetworkRegistry.TargetPoint(
                                        this.world.provider.getDimension(),
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY),
                                        (int) Math.round(this.posZ - 0.5) + z,
                                        32.0));
                    }
                    return;
                }
            }
        }
        for (int x = -1; x < 2; ++x) {
            for (int z = -1; z < 2; ++z) {
                if (this.world.isAirBlock(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY) - 1, (int) Math.round(this.posZ - 0.5) + z))) {
                    if (this.blocky != null && this.blocky != Blocks.AIR) {
                        this.world.setBlockState(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY) - 1, (int) Math.round(this.posZ - 0.5) + z), this.blocky.getStateFromMeta(this.md), 3);
                        final SimpleNetworkWrapper instance3 = PacketHandler.INSTANCE;
                        final Block blocky3 = this.blocky;
                        instance3.sendToAllAround(
                                new PacketFXBlocksplosion(
                                        Block.getIdFromBlock(this.blocky),
                                        this.md,
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY) - 1,
                                        (int) Math.round(this.posZ - 0.5) + z),
                                new NetworkRegistry.TargetPoint(
                                        this.world.provider.getDimension(),
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY) - 1,
                                        (int) Math.round(this.posZ - 0.5) + z,
                                        32.0));
                    }
                    return;
                }
            }
        }
        for (int x = -1; x < 2; ++x) {
            for (int z = -1; z < 2; ++z) {
                if (this.world.isAirBlock(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY) + 1, (int) Math.round(this.posZ - 0.5) + z))) {
                    if (this.blocky != null && this.blocky != Blocks.AIR) {
                        this.world.setBlockState(new BlockPos((int) Math.round(this.posX - 0.5) + x, (int) Math.round(this.posY) + 1, (int) Math.round(this.posZ - 0.5) + z), this.blocky.getStateFromMeta(this.md), 3);
                        final SimpleNetworkWrapper instance4 = PacketHandler.INSTANCE;
                        final Block blocky4 = this.blocky;
                        instance4.sendToAllAround(
                                new PacketFXBlocksplosion(
                                        Block.getIdFromBlock(this.blocky),
                                        this.md,
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY) + 1,
                                        (int) Math.round(this.posZ - 0.5) + z),
                                new NetworkRegistry.TargetPoint(
                                        this.world.provider.getDimension(),
                                        (int) Math.round(this.posX - 0.5) + x,
                                        (int) Math.round(this.posY) + 1,
                                        (int) Math.round(this.posZ - 0.5) + z,
                                        32.0));
                    }
                    return;
                }
            }
        }
    }

    public void writeSpawnData(final ByteBuf data) {
        super.writeSpawnData(data);
        if (this.blocky != null) {
            final Block blocky = this.blocky;
            data.writeInt(Block.getIdFromBlock(this.blocky));
        } else {
            data.writeInt(0);
        }
        data.writeByte(this.md);
        data.writeByte(this.upgrades.length);
        for (final byte b : this.upgrades) {
            data.writeByte(b);
        }
    }

    public void readSpawnData(final ByteBuf data) {
        super.readSpawnData(data);
        this.blocky = Block.getBlockById(data.readInt());
        this.md = data.readByte();
        this.upgrades = new byte[data.readByte()];
        for (int a = 0; a < this.upgrades.length; ++a) {
            this.upgrades[a] = data.readByte();
        }
    }

    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        final ItemStack stack = new ItemStack(this.blocky, 1, this.md);
        if (stack.getItem() != null) {
            return stack.getDisplayName() + " Golem";
        }
        if (this.blocky == null || this.blocky == Blocks.AIR) {
            return "Voidling Golem";
        }
        return this.blocky.getLocalizedName() + " Golem";
    }

    public EnumGolemType getGolemType() {
        return EnumGolemType.WOOD;
    }

    public int getGolemStrength() {
        return this.type.strength + this.getUpgradeAmount(1);
    }

    public boolean attackEntityAsMob(final Entity par1Entity) {
        if (this.blocky == Blocks.WEB && par1Entity instanceof EntityLivingBase) {
            ((EntityLivingBase) par1Entity).addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SLOWNESS, 20, 0));
        }
        return super.attackEntityAsMob(par1Entity);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(final byte par1) {
        if (par1 == 4) {
            this.action = 6;
        } else if (par1 == 5) {
            this.healing = 5;
            final String decor = this.getGolemDecoration();
            final int bonus = decor == null ? 0 : (decor.contains("H") ? 5 : 0);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.type.health + bonus);
        } else if (par1 == 6) {
            this.leftArm = 5;
        } else if (par1 == 8) {
            this.rightArm = 5;
        } else if (par1 == 7) {
            this.bootup = 33.0f;
        } else {
            super.handleStatusUpdate(par1);
        }
    }

    public void onStruckByLightning(final EntityLightningBolt bolt) {
        if (bolt instanceof EntityLightningBoltFinite) {
            return;
        }
        super.onStruckByLightning(bolt);
    }
}
