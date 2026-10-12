package fox.spiteful.forbidden.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.LogHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import org.apache.logging.log4j.Level;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class BlockLogTainted extends BlockLog
{
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite sides;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite ends;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite gross;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite gross2;

    public BlockLogTainted()
    {
        super();
        setCreativeTab(Forbidden.tab);
        setSoundType(ConfigBlocks.blockTaint.getSoundType());
        try {
            Field mat = ReflectionHelper.findField(Block.class, "blockMaterial", "field_149764_J");
            mat.set(this, Config.taintMaterial);
        } catch (Exception e){
            e.printStackTrace();
        }
        this.setHarvestLevel("axe", 0);
    }

    public int damageDropped(int p_149692_1_)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap register)
    {
        sides = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_log_side"));
        ends = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_log_bottom"));
        gross = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_log_side_gross"));
        gross2 = register.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:taint_log_side_gross2"));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side, int meta)
    {
        int logDirection = meta & 12;
        return logDirection == 0 && (side == 1 || side == 0) ? ends : (logDirection == 4 && (side == 5 || side == 4) ? ends : (logDirection == 8 && (side == 2 || side == 3) ? ends : sides));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
        int logDirection = meta & 12;
        if((logDirection == 0 && (side == 1 || side == 0)) || (logDirection == 4 && (side == 5 || side == 4)) || (logDirection == 8 && (side == 2 || side == 3)))
            return ends;
        else {
            Random r = new Random((long)(side + y + x * z));
            return r.nextInt(100) < 75 ? sides : r.nextBoolean() ? gross : gross2;
        }
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite getSideIcon(int meta)
    {
        return sides;
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite getTopIcon(int meta)
    {
        return ends;
    }

    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }


    @Override public int damageDropped(net.minecraft.block.state.IBlockState state) { return damageDropped(getMetaFromState(state)); }
    @Override public boolean canSustainLeaves(net.minecraft.block.state.IBlockState state,IBlockAccess world,BlockPos pos) { return canSustainLeaves(world,pos.getX(),pos.getY(),pos.getZ()); }
    @Override public net.minecraft.block.state.IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(LOG_AXIS,(meta&12)==4?EnumAxis.X:(meta&12)==8?EnumAxis.Z:(meta&12)==12?EnumAxis.NONE:EnumAxis.Y); }
    @Override public int getMetaFromState(net.minecraft.block.state.IBlockState state) { return state.getValue(LOG_AXIS)==EnumAxis.X?4:state.getValue(LOG_AXIS)==EnumAxis.Z?8:state.getValue(LOG_AXIS)==EnumAxis.NONE?12:0; }
}