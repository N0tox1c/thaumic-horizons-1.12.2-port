package fox.spiteful.forbidden.blocks;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockResource extends Block {
    public BlockResource() {

        super(Material.IRON);
        this.setCreativeTab(Forbidden.tab);
        setHardness(5.0F);
        setSoundType(net.minecraft.block.SoundType.METAL);
        // Texture is assigned by the native model registry.
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.BlockPos beacon) {

        return true;
    }

}
