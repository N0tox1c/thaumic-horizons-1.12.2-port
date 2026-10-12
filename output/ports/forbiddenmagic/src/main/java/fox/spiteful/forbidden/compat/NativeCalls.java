package fox.spiteful.forbidden.compat;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
public final class NativeCalls {
    private NativeCalls() {}
    public static ItemStack findItemStack(String mod, String name, int count) {
        Item item=Item.REGISTRY.getObject(new ResourceLocation(mod.toLowerCase(java.util.Locale.ROOT), name.toLowerCase(java.util.Locale.ROOT)));
        return item == null ? null : new ItemStack(item,count);
    }
    public static void playSound(World world,double x,double y,double z,String oldName,float volume,float pitch) {
        String name=oldName;
        if ("random.fizz".equals(name)) name="minecraft:block.fire.extinguish";
        if ("random.drink".equals(name)) name="minecraft:entity.generic.drink";
        SoundEvent sound=SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
        if(sound==null) throw new IllegalArgumentException("Unregistered sound: "+name);
        world.playSound(null,x,y,z,sound,SoundCategory.PLAYERS,volume,pitch);
    }
    public static void reservoirBubble(World world,int x,int y,int z,int color) {
        world.spawnParticle(net.minecraft.util.EnumParticleTypes.SPELL_MOB,x+0.5,y+0.6,z+0.5,((color>>16)&255)/255.0,((color>>8)&255)/255.0,(color&255)/255.0);
    }
}
