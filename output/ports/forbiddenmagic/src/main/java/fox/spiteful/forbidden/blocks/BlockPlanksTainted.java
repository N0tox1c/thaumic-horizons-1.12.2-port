package fox.spiteful.forbidden.blocks;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.block.Block;
import thaumcraft.common.config.Config;

public class BlockPlanksTainted extends Block{

    public BlockPlanksTainted(){
        super(Config.taintMaterial);
        setCreativeTab(Forbidden.tab);
        setSoundType(net.minecraft.block.SoundType.WOOD);
        setHardness(2.0F);
        setResistance(5.0F);
        // Texture is assigned by the native model registry.
        this.setHarvestLevel("axe", 0);
    }

}
