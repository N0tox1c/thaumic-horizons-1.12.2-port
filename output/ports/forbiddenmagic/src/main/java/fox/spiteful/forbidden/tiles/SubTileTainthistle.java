/**
 * This class based on a class created by <Pokefenn>.
 * It was distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 */

package fox.spiteful.forbidden.tiles;
import net.minecraft.util.math.BlockPos;


import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;

import java.util.Random;

public class SubTileTainthistle extends SubTileGenerating {

    private static final String TAG_BURN_TIME = "burnTime";
    private static final int range = 4;
    public static LexiconEntry lexicon;
    int burnTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        boolean didSomething = false;
        if(this.ticksExisted % 80 == 0) {
            if (!supertile.getWorld().isRemote) {
                for (int ex = supertile.getPos().getX() - range; ex <= supertile.getPos().getX() + range; ex++) {
                    for (int wy = supertile.getPos().getY() - range; wy <= supertile.getPos().getY() + range; wy++) {
                        for (int zee = supertile.getPos().getZ() - range; zee <= supertile.getPos().getZ() + range; zee++) {
                            if (isFlux(ex, wy, zee)) {
                                int depth = fox.spiteful.forbidden.compat.LegacyWorldAccess.getBlockMetadata(supertile.getWorld(), ex, wy, zee);
                                supertile.getWorld().setBlockToAir(new BlockPos(ex, wy, zee));
                                didSomething = true;
                                burnTime = Math.min(burnTime + depth * (20), 4000);
                            }
                        }
                    }
                }

                if (didSomething) {
                    playSound();
                    sync();
                }
            }
        }
        if(burnTime > 0){
            if(supertile.getWorld().rand.nextInt(8) == 0)
                doBurnParticles();
            burnTime--;
        }
    }

    @Override
    public boolean isPassiveFlower() {
        return false;
    }

    public void doBurnParticles() {
        fox.spiteful.forbidden.compat.NativeCalls.reservoirBubble(supertile.getWorld(), supertile.getPos().getX(), supertile.getPos().getY(), supertile.getPos().getZ(), 0x4D00FF);
    }

    public boolean isFlux(int x, int y, int z){
        Block target = supertile.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
        return target == ConfigBlocks.blockFluxGas || target == ConfigBlocks.blockFluxGoo;
    }

    public void playSound() {
        fox.spiteful.forbidden.compat.NativeCalls.playSound(supertile.getWorld(), supertile.getPos().getX(), supertile.getPos().getY(), supertile.getPos().getZ(), "random.drink", 0.05F, 0.5F + (float) Math.random() * 0.5F);
    }

    @Override
    public int getMaxMana() {
        return 150;
    }

    @Override
    public int getColor() {
        return 0x4D00FF;
    }

    @Override
    public LexiconEntry getEntry() {
        return lexicon;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);
        cmp.setInteger(TAG_BURN_TIME, burnTime);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);
        burnTime = cmp.getInteger(TAG_BURN_TIME);
    }

    @Override
    public boolean canGeneratePassively() {
        return burnTime > 0;
    }

    @Override
    public int getDelayBetweenPassiveGeneration() {
        return 2;
    }

    @Override
    public int getValueForPassiveGeneration() {
        return 3;
    }

    public TextureAtlasSprite getIcon(){
        return ((fox.spiteful.forbidden.compat.DarkSignature) BotaniaAPI.getSignatureForName("tainthistle")).getIconForStack(null);
    }

    @Override public net.minecraft.client.renderer.block.model.ModelResourceLocation getBlockModel() { return new net.minecraft.client.renderer.block.model.ModelResourceLocation("forbiddenmagic:tainthistle", "normal"); }
    @Override public net.minecraft.client.renderer.block.model.ModelResourceLocation getItemModel() { return new net.minecraft.client.renderer.block.model.ModelResourceLocation("forbiddenmagic:tainthistle", "inventory"); }
}
