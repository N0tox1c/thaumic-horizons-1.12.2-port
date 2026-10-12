package fox.spiteful.forbidden.items;

import fox.spiteful.forbidden.Forbidden;
import net.minecraft.item.ItemFood;

/** Original: restores two hunger units; saturation 0.1, cannot feed wolves. */
public class ItemGluttonyShard extends ItemFood {
    public ItemGluttonyShard() {
        super(2, 0.1F, false);
        setCreativeTab(Forbidden.tab);
    }
}
