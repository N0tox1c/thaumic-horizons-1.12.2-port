package fox.spiteful.forbidden.compat;

        import net.minecraft.client.renderer.texture.TextureMap;
        import net.minecraft.entity.player.EntityPlayer;
        import net.minecraft.item.ItemStack;
        import net.minecraft.util.text.TextFormatting;
        import net.minecraft.client.renderer.texture.TextureAtlasSprite;
        import net.minecraft.util.text.translation.I18n;
        import vazkii.botania.api.BotaniaAPI;
        import vazkii.botania.api.subtile.SubTileEntity;
        import vazkii.botania.api.subtile.SubTileFunctional;
        import vazkii.botania.api.subtile.SubTileGenerating;
        import vazkii.botania.api.subtile.signature.BasicSignature;
        import vazkii.botania.api.subtile.signature.SubTileSignature;

        import java.util.List;

public class DarkSignature extends BasicSignature {
    TextureAtlasSprite icon;

    public DarkSignature(String name){
        super(name);
    }

    public void registerIcons(TextureMap reg){
        icon = reg.registerSprite(new net.minecraft.util.ResourceLocation("forbidden:" + getName()));
    }

    public TextureAtlasSprite getIconForStack(ItemStack item){
        return icon;
    }

    @Override
    public String getUnlocalizedNameForStack(ItemStack item){
        return "darkflower." + getName();
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack item){
        return "tile.darkflower." + getName() + ".lore";
    }

}