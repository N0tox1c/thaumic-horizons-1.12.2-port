package am2.api.spell;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpellIconManager {
	/**
	 * Returns the TextureAtlasSprite registered to the specified spell name.  Returns the missing TextureAtlasSprite if one doesn't exist.
	 */
	public TextureAtlasSprite getIcon(String spellName);
	
	/**
	 * Registers an TextureAtlasSprite to be associated with a spell component
	 * @param spellName  The unlocalized name of the spell used when it was registered
	 * @param TextureAtlasSprite The TextureAtlasSprite of the spell
	 */
	public void registerIcon(String spellName, TextureAtlasSprite TextureAtlasSprite);
}
