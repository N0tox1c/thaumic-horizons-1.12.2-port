package fox.spiteful.forbidden.compat;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.BindableHelper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.UUID;

/** Translates original FM ownership and LP operations to Blood Magic 2 networks. */
public final class BloodNetwork {
    private BloodNetwork() {}
    private static SoulNetwork network(String owner) {
        try { return NetworkHelper.getSoulNetwork(UUID.fromString(owner)); }
        catch (IllegalArgumentException notUUID) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server == null) throw new IllegalStateException("LP networks require a running server");
            GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(owner);
            if (profile == null || profile.getId() == null) throw new IllegalArgumentException("Unknown LP owner: " + owner);
            return NetworkHelper.getSoulNetwork(profile.getId());
        }
    }
    public static void checkAndSetItemOwner(ItemStack stack, EntityPlayer player) {
        if (player.world.isRemote) return;
        if (Binding.fromStack(stack) == null) {
            // Never silently transfer a legacy item belonging to someone else.
            String oldOwner = stack.hasTagCompound() ? stack.getTagCompound().getString("ownerName") : "";
            if (oldOwner.isEmpty() || oldOwner.equals(player.getName())) BindableHelper.applyBinding(stack, player);
            else {
                SoulNetwork oldNetwork = network(oldOwner);
                BindableHelper.applyBinding(stack, new Binding(oldNetwork.getPlayerId(), oldOwner));
            }
        }
        Binding binding = Binding.fromStack(stack);
        if (binding != null) stack.getTagCompound().setString("ownerName", binding.getOwnerName());
    }
    public static String getUsername(EntityPlayer player) { return player.getName(); }
    public static int getCurrentEssence(String owner) { return network(owner).getCurrentEssence(); }
    public static void setCurrentEssence(String owner, int value) { network(owner).setCurrentEssence(value); }
    public static int syphonFromNetwork(ItemStack stack, int amount) {
        return NetworkHelper.syphonFromContainer(stack, SoulTicket.item(stack, amount)) ? amount : 0;
    }
    public static int syphonFromNetwork(String owner, int amount) { return network(owner).syphon(new SoulTicket(amount)); }
    public static void causeNauseaToPlayer(String owner) { network(owner).causeNauseaToPlayer(); }
    public static void setMaxOrbToMax(String owner, int tier) { NetworkHelper.setMaxOrb(network(owner), tier); }
    public static void addCurrentEssenceToMaximum(String owner, int amount, int maximum) { network(owner).add(amount, maximum); }
    public static boolean syphonAndDamageFromNetwork(ItemStack stack, EntityPlayer player, int amount) {
        Binding binding = Binding.fromStack(stack);
        return binding != null && NetworkHelper.getSoulNetwork(binding).syphonAndDamage(player, amount);
    }
}
