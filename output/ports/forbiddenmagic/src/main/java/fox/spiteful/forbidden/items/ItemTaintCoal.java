package fox.spiteful.forbidden.items;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.item.Item;

/** Original Forbidden Magic taint charcoal, with 1.12 JSON model instead of TextureAtlasSprite. */
public class ItemTaintCoal extends Item {
    public ItemTaintCoal() {
        setCreativeTab(Forbidden.tab);
    }
}
