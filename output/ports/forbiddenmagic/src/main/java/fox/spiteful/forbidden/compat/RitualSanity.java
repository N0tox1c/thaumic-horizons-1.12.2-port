package fox.spiteful.forbidden.compat;

import fox.spiteful.forbidden.compat.BloodNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.potions.PotionWarpWard;

import java.util.ArrayList;
import java.util.List;

@WayofTime.bloodmagic.ritual.RitualRegister("SpiteSanity")
public class RitualSanity extends WayofTime.bloodmagic.ritual.Ritual
{
    public RitualSanity() { super("SpiteSanity", 1, 20000, "ritual.forbidden.sanity"); }
    @Override public WayofTime.bloodmagic.ritual.Ritual getNewCopy() { return new RitualSanity(); }
    public static final int crystallosDrain = 4;

    @Override
    public void performRitual(WayofTime.bloodmagic.ritual.IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner().toString();

        int currentEssence = BloodNetwork.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        int x = ritualStone.getBlockPos().getX();
        int y = ritualStone.getBlockPos().getY();
        int z = ritualStone.getBlockPos().getZ();

        int timeDelay = 600;

        if (world.getWorldTime() % timeDelay != 0)
        {
            return;
        }

        boolean crazy = world.getWorldTime() % (timeDelay * 4) == 0;

        boolean hasCrystallos = canDrainFrostReagent(ritualStone, false);

        int range = 15 * (hasCrystallos ? 3 : 1);
        int vertRange = 15 * (hasCrystallos ? 3 : 1);

        float posX = x + 0.5f;
        float posY = y + 0.5f;
        float posZ = z + 0.5f;
        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).grow(range, vertRange, range));
        int entityCount = 0;

        for (EntityPlayer player : list)
        {
            entityCount ++;
        }

        int cost = this.getRefreshCost();

        if (currentEssence < cost * entityCount)
        {
            BloodNetwork.causeNauseaToPlayer(owner);

        } else
        {
            entityCount = 0;

            for (EntityPlayer player : list)
            {
                PotionEffect effect = player.getActivePotionEffect(PotionWarpWard.instance);
                if (effect == null || effect.getDuration() <= timeDelay)
                {
                    PotionEffect ward = new PotionEffect(PotionWarpWard.instance, timeDelay + 2, 0, true, true);
                    ward.getCurativeItems().clear();
                    player.addPotionEffect(ward);
                    entityCount++;
                    if(crazy && world.rand.nextInt(35) <= 3)
                        Thaumcraft.proxy.getPlayerKnowledge(player).addWarpTemp( 1 + world.rand.nextInt(3));
                }
            }

            if (entityCount > 0)
            {
                if (hasCrystallos)
                {
                    canDrainFrostReagent(ritualStone, true);
                }
                BloodNetwork.syphonFromNetwork(owner, cost * entityCount);
            }
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 15000;
    }

    @Override
    public void gatherComponents(java.util.function.Consumer<WayofTime.bloodmagic.ritual.RitualComponent> consumer)
    {
        addRune(consumer, 4, 1, 0, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, -4, 1, 0, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 0, 1, 4, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 0, 1, -4, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 4, 2, 0, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, -4, 2, 0, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 0, 2, 4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 0, 2, -4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 4, 3, 0, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, -4, 3, 0, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 0, 3, 4, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 0, 3, -4, WayofTime.bloodmagic.ritual.EnumRuneType.DUSK);
        addRune(consumer, 4, 4, 0, WayofTime.bloodmagic.ritual.EnumRuneType.AIR);
        addRune(consumer, -4, 4, 0, WayofTime.bloodmagic.ritual.EnumRuneType.AIR);
        addRune(consumer, 0, 4, 4, WayofTime.bloodmagic.ritual.EnumRuneType.AIR);
        addRune(consumer, 0, 4, -4, WayofTime.bloodmagic.ritual.EnumRuneType.AIR);
        addRune(consumer, 4, 4, 1, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, -4, 4, 1, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 1, 4, 4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 1, 4, -4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 4, 4, -1, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, -4, 4, -1, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, -1, 4, 4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, -1, 4, -4, WayofTime.bloodmagic.ritual.EnumRuneType.EARTH);
        addRune(consumer, 3, 4, 0, WayofTime.bloodmagic.ritual.EnumRuneType.FIRE);
        addRune(consumer, -3, 4, 0, WayofTime.bloodmagic.ritual.EnumRuneType.FIRE);
        addRune(consumer, 0, 4, 3, WayofTime.bloodmagic.ritual.EnumRuneType.FIRE);
        addRune(consumer, 0, 4, -3, WayofTime.bloodmagic.ritual.EnumRuneType.FIRE);


    }
    private boolean canDrainFrostReagent(WayofTime.bloodmagic.ritual.IMasterRitualStone stone, boolean consume) {
        for (net.minecraft.util.EnumFacing side : net.minecraft.util.EnumFacing.values()) {
            net.minecraft.tileentity.TileEntity tile = stone.getWorldObj().getTileEntity(stone.getBlockPos().offset(side));
            if (tile == null) continue;
            net.minecraftforge.items.IItemHandler handler = tile.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
            if (handler == null) continue;
            for (int slot = 0; slot < handler.getSlots(); slot++) {
                net.minecraft.item.ItemStack stack = handler.getStackInSlot(slot);
                if (stack.getItem() == WayofTime.bloodmagic.core.RegistrarBloodMagicItems.COMPONENT && stack.getMetadata() == WayofTime.bloodmagic.item.types.ComponentTypes.REAGENT_FROST.ordinal()) {
                    net.minecraft.item.ItemStack extracted = handler.extractItem(slot, crystallosDrain, true);
                    if (extracted.getCount() == crystallosDrain) {
                        if (consume) handler.extractItem(slot, crystallosDrain, false);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}