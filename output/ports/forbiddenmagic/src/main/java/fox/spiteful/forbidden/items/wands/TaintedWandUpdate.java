package fox.spiteful.forbidden.items.wands;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.ItemWandCasting;

import fox.spiteful.forbidden.Config;

public class TaintedWandUpdate implements IWandRodOnUpdate {

    Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

    public void onUpdate(ItemStack itemstack, EntityPlayer player) {
        if(player.ticksExisted % 100 == 0 && net.minecraft.world.biome.Biome.getIdForBiome(player.world.getBiome(player.getPosition())) == thaumcraft.common.config.Config.biomeTaintID){
            for(int x = 0;x < primals.length;x++){
                if(((ItemWandCasting)itemstack.getItem()).getVis(itemstack, primals[x]) < ((ItemWandCasting)itemstack.getItem()).getMaxVis(itemstack) / 10) {
                    ((ItemWandCasting)itemstack.getItem()).addVis(itemstack, primals[x], 1, true);
                }
            }
        }
        
    }
}
