package fox.spiteful.forbidden.items.tools;

import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Enchantments;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.enchantments.DarkEnchantments;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import thaumcraft.api.IRepairable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.lib.utils.BlockUtils;

public class ItemMorphShovel extends ItemSpade implements IRepairable, IWarpingGear {
    public TextureAtlasSprite[] icon;
    int side;

    public ItemMorphShovel(ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Forbidden.tab);
        this.setHarvestLevel("shovel", 4);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap ir) {
        icon = new TextureAtlasSprite[2];
        this.icon[0] = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:chameleonshovel"));
        this.icon[1] = ir.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:eyeshovel"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIconFromDamageForRenderPass(int par1, int renderPass) {
        return renderPass != 1 ? icon[0] : icon[1];
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.EPIC;
    }

    public boolean getIsRepairable(ItemStack stack, ItemStack stack2) {
        return stack2.isItemEqual(new ItemStack(ForbiddenItems.deadlyShards, 1, 1)) ? true : super.getIsRepairable(stack, stack2);
    }

    @Override
    public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, net.minecraft.util.EnumHand hand) {
        return new net.minecraft.util.ActionResult<>(net.minecraft.util.EnumActionResult.SUCCESS, legacyRightClick(player.getHeldItem(hand), world, player));
    }
    public ItemStack legacyRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if (player.isSneaking() && itemstack.hasTagCompound() && getMaxDamage(itemstack) - itemstack.getItemDamage() > 5) {
            NBTTagCompound tags = itemstack.getTagCompound();
            byte phase = tags.getByte("phase");
            if(tags.hasKey("ench")){
                NBTTagList enchants = itemstack.getEnchantmentTagList();
                tags.setTag("enchants" + phase, enchants);
            }
            else
                tags.removeTag("enchants" + phase);

            if (tags.hasKey("display")) {
                String name = tags.getCompoundTag("display").getString("Name");
                if (name != null && !name.equals(""))
                    tags.getCompoundTag("display").setString("Name" + phase, name);
                else
                    tags.getCompoundTag("display").removeTag("Name" + phase);
            }
            if (++phase > 2)
                phase = 0;
            tags.setByte("phase", phase);
            if(tags.hasKey("enchants" + phase)) {
                NBTTagList enchants = (NBTTagList) (tags.getTag("enchants" + phase));
                tags.setTag("ench", enchants);
            }
            else
                tags.removeTag("ench");

            if (tags.hasKey("display")) {
                String name = tags.getCompoundTag("display").getString("Name" + phase);
                if (name != null && !name.equals(""))
                    tags.getCompoundTag("display").setString("Name", name);
                else
                    tags.getCompoundTag("display").removeTag("Name");
            }

            itemstack.setTagCompound(tags);
            itemstack.damageItem(5, player);
            player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
            fox.spiteful.forbidden.compat.NativeCalls.playSound(world, player.posX, player.posY, player.posZ, "thaumcraft:wandfail", 0.2F, 0.2F + world.rand.nextFloat() * 0.2F);
        }
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int renderpass) {
        if (renderpass != 1)
            return 16777215;
        else {
            if (!itemstack.hasTagCompound())
                return 0x980000;
            byte phase = itemstack.getTagCompound().getByte("phase");
            if (phase == 1)
                return 0x0010CC;
            else if (phase == 2)
                return 0xE5DA00;
            else
                return 0x980000;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int fuckObfuscation, boolean fuckObfuscation2) {
        super.onUpdate(stack, world, entity, fuckObfuscation, fuckObfuscation2);
        if(EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.voidtouched, stack) > 0 && stack.isItemDamaged() && entity != null && entity.ticksExisted % 10 == 0 && entity instanceof EntityLivingBase) {
            stack.damageItem(-1, (EntityLivingBase)entity);
        }

    }

    public int getWarp(ItemStack itemstack, EntityPlayer player) {
        if(EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.voidtouched, itemstack) > 0)
            return 1;
        else
            return 0;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, net.minecraft.util.math.BlockPos pos, EntityPlayer player) {
        int x=pos.getX(), y=pos.getY(), z=pos.getZ();
        RayTraceResult movingobjectposition = player.rayTrace(5.0D, 1.0F);
        if(movingobjectposition != null && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.side = movingobjectposition.sideHit.getIndex();
        }

        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, net.minecraft.block.state.IBlockState state, net.minecraft.util.math.BlockPos pos, EntityLivingBase player) {
        Block block=state.getBlock(); int x=pos.getX(), y=pos.getY(), z=pos.getZ();
        if(EnchantmentHelper.getEnchantmentLevel(DarkEnchantments.impact, stack) <= 0)
            return super.onBlockDestroyed(stack, world, state, pos, player);
        if(!player.world.isRemote) {
            int meta = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
            if(block.isToolEffective("shovel", block.getStateFromMeta(meta))) {
                for(int aa = -1; aa <= 1; ++aa) {
                    for(int bb = -1; bb <= 1; ++bb) {
                        int xx = 0;
                        int yy = 0;
                        int zz = 0;
                        if(this.side <= 1) {
                            xx = aa;
                            zz = bb;
                        } else if(this.side <= 3) {
                            xx = aa;
                            yy = bb;
                        } else {
                            zz = aa;
                            yy = bb;
                        }

                        if(!(player instanceof EntityPlayer) || world.isBlockModifiable((EntityPlayer)player, new net.minecraft.util.math.BlockPos(x + xx, y + yy, z + zz))) {
                            Block bl = world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)).getBlock();
                            meta = world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x + xx, y + yy, z + zz)));
                            if(bl.getBlockHardness(world.getBlockState(new net.minecraft.util.math.BlockPos(x + xx, y + yy, z + zz)), world, new net.minecraft.util.math.BlockPos(x + xx, y + yy, z + zz)) >= 0.0F && bl.isToolEffective("shovel", bl.getStateFromMeta(meta))) {
                                stack.damageItem(1, player);
                                BlockUtils.harvestBlock(world, new BlockPos(x + xx, y + yy, z + zz), (EntityPlayer)player);
                            }
                        }
                    }
                }
            }
            else
                return super.onBlockDestroyed(stack, world, state, pos, player);
        }

        return super.onBlockDestroyed(stack, world, state, pos, player);
    }

}
