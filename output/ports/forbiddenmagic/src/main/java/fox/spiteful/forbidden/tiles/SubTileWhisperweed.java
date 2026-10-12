package fox.spiteful.forbidden.tiles;

import fox.spiteful.forbidden.compat.DarkSignature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.List;

public class SubTileWhisperweed extends SubTileFunctional {
    private static final int cost = 6000;
    private final static int range = 3;
    public static LexiconEntry lexicon;

    @Override
    public void onUpdate(){
        super.onUpdate();

        if(redstoneSignal > 0)
            return;

        if(!supertile.getWorld().isRemote && mana >= cost && this.ticksExisted % 300 == 0) {
            List<EntityPlayer> players = supertile.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(supertile.getPos().getX() - range, supertile.getPos().getY() - range, supertile.getPos().getZ() - range, supertile.getPos().getX() + range + 1, supertile.getPos().getY() + range + 1, supertile.getPos().getZ() + range + 1));
            if(players.size() > 0) {
                EntityPlayer player = players.get(supertile.getWorld().rand.nextInt(players.size()));
                int amt = 1 + player.world.rand.nextInt(3);
                if(player.world.rand.nextInt(10) < 2){
                    amt += 1 + player.world.rand.nextInt(3);
                    Thaumcraft.addWarpToPlayer(player, 1 + player.world.rand.nextInt(5), true);
                }

                for(int a = 0; a < amt; ++a) {
                    Aspect aspect;
                    if(player.world.rand.nextInt(25) > 2)
                        aspect = (Aspect)Aspect.getPrimalAspects().get(player.world.rand.nextInt(6));
                    else
                        aspect = (Aspect)Aspect.getCompoundAspects().get(player.world.rand.nextInt(Aspect.getCompoundAspects().size()));
                    Thaumcraft.proxy.getPlayerKnowledge(player).addAspectPool( aspect, (short)1);
                    PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getTag(), (short)1, (short)(Thaumcraft.proxy.getPlayerKnowledge(player).getAspectPoolFor( aspect))), (EntityPlayerMP)player);
                    ResearchManager.updateCache(player);
                }
                mana -= cost;
            }

        }
    }

    @Override
    public int getColor(){
        return 0x745380;
    }

    @Override
    public int getMaxMana() {
        return cost;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    public LexiconEntry getEntry(){
        return lexicon;
    }

    public TextureAtlasSprite getIcon(){
        return ((fox.spiteful.forbidden.compat.DarkSignature) BotaniaAPI.getSignatureForName("whisperweed")).getIconForStack(null);
    }
    @Override public net.minecraft.client.renderer.block.model.ModelResourceLocation getBlockModel() { return new net.minecraft.client.renderer.block.model.ModelResourceLocation("forbiddenmagic:whisperweed", "normal"); }
    @Override public net.minecraft.client.renderer.block.model.ModelResourceLocation getItemModel() { return new net.minecraft.client.renderer.block.model.ModelResourceLocation("forbiddenmagic:whisperweed", "inventory"); }
}
