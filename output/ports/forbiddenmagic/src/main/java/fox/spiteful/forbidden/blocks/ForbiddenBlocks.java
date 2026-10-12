package fox.spiteful.forbidden.blocks;

import fox.spiteful.forbidden.tiles.TileEntityWrathCage;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForbiddenBlocks {
    private static final List<Block> PENDING_BLOCKS = new ArrayList<>();
    private static final List<Item> PENDING_ITEM_BLOCKS = new ArrayList<>();

    private static Block enqueueBlock(Block block, String legacyId) {
        block.setRegistryName("forbiddenmagic", legacyId.toLowerCase(Locale.ROOT));
        PENDING_BLOCKS.add(block);
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        PENDING_ITEM_BLOCKS.add(itemBlock);
        return block;
    }

    private static Block enqueueStone(Block block, String legacyId) {
        block.setRegistryName("forbiddenmagic", legacyId.toLowerCase(Locale.ROOT));
        PENDING_BLOCKS.add(block);
        ItemBlock itemBlock = new ItemBlockStoneTainted(block);
        itemBlock.setRegistryName(block.getRegistryName());
        PENDING_ITEM_BLOCKS.add(itemBlock);
        return block;
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : PENDING_BLOCKS) event.getRegistry().register(block);
    }

    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        for (Item item : PENDING_ITEM_BLOCKS) event.getRegistry().register(item);
    }

    public static Block arcaneCake;
    public static Block blackFlower;
    public static Block wrathCage;
    public static Block starBlock;
    public static Block roseBush;
    public static Block taintLog;
    public static Block taintPlanks;
    public static Block taintLeaves;
    public static Block taintSapling;
    public static Block taintStone;

    public static void addBlocks() {
        arcaneCake = enqueueBlock(new BlockArcaneCake().setTranslationKey("ArcaneCake"), "ArcaneCake");

        wrathCage = enqueueBlock(new BlockWrathCage().setTranslationKey("WrathCage"), "WrathCage");
        GameRegistry.registerTileEntity(TileEntityWrathCage.class, new ResourceLocation("forbiddenmagic", "wrathcage"));

        blackFlower = enqueueBlock(new BlockBlackFlower().setTranslationKey("InkFlower"), "InkFlower");

        starBlock = enqueueBlock(new BlockResource().setTranslationKey("StarBlock"), "StarBlock");
        OreDictionary.registerOre("blockNetherStar", new ItemStack(starBlock, 1, 0));

        roseBush = enqueueBlock(new BlockRoseBush(), "UmbralBush");

        taintLog = enqueueBlock(new BlockLogTainted().setTranslationKey("TaintLog"), "TaintLog");
        taintPlanks = enqueueBlock(new BlockPlanksTainted().setTranslationKey("TaintPlank"), "TaintPlank");
        taintLeaves = enqueueBlock(new BlockLeavesTainted().setTranslationKey("TaintLeaves"), "TaintLeaves");
        taintSapling = enqueueBlock(new BlockSaplingTainted().setTranslationKey("TaintSapling"), "TaintSapling");

        taintStone = enqueueStone(new BlockStoneTainted(), "TaintBlock");
    }
}