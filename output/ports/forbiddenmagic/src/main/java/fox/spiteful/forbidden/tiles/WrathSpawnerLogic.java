package fox.spiteful.forbidden.tiles;

import java.util.Locale;
import fox.spiteful.forbidden.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;

/** Original Wrath Cage spawner adapted to Minecraft 1.12.2. */
public class WrathSpawnerLogic {
    final TileEntityWrathCage mobSpawnerEntity;
    public boolean mobSet;
    public int spawnDelay = 20;
    private String mobID = "Pig"; // legacy NBT; DO NOT convert this field on save.
    private Aspect aspect = Aspect.GREED;
    private boolean slothful;
    private int fuel;
    public double field_98287_c;
    public double field_98284_d;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 300;
    private int spawnCount = 3;
    private Entity renderEntity;
    private int maxNearbyEntities = 6;
    private int spawnRange = 4;

    WrathSpawnerLogic(TileEntityWrathCage cage) { mobSpawnerEntity = cage; }
    public String getEntityNameToSpawn() { return mobID; }
    public Aspect getAspect() { return aspect; }
    public boolean isMobSet() { return mobSet; }
    public void mobIsSet(boolean value) { mobSet = value; mobSpawnerEntity.sync(); }

    public void setMobID(String id) {
        if (id == null || id.isEmpty()) return;
        mobID = id;
        renderEntity = null;
        aspect = Config.spawnerMobs.containsKey(id) ? Config.spawnerMobs.get(id) : Aspect.GREED;
        mobSpawnerEntity.sync();
    }

    public World getSpawnerWorld() { return mobSpawnerEntity.getWorld(); }
    public int getSpawnerX() { return mobSpawnerEntity.getPos().getX(); }
    public int getSpawnerY() { return mobSpawnerEntity.getPos().getY(); }
    public int getSpawnerZ() { return mobSpawnerEntity.getPos().getZ(); }

    public void updateSpawnerBlock(int event) {
        World world = getSpawnerWorld();
        if (world == null) return;
        BlockPos pos = mobSpawnerEntity.getPos();
        IBlockState state = world.getBlockState(pos);
        world.addBlockEvent(pos, state.getBlock(), event, 0);
        mobSpawnerEntity.sync();
    }

    public void updateSpawner() {
        World world = getSpawnerWorld();
        if (!mobSet || world == null || !Config.wrathCage) return;
        if (world.isRemote) {
            if (fuel > 0 || Config.wrathCost <= 0) {
                if (spawnDelay > 0) --spawnDelay;
                field_98284_d = field_98287_c;
                field_98287_c = (field_98287_c + 1000.0 / (spawnDelay + 200.0)) % 360.0;
            }
            return;
        }
        if (!(world instanceof WorldServer)) return;
        if (Config.wrathCost > 0 && fuel <= 0) {
            int cost = Config.wrathCost;
            if (mobSpawnerEntity.special >= cost) {
                mobSpawnerEntity.special -= cost;
                slothful = false;
            } else if (mobSpawnerEntity.wrath >= cost) {
                mobSpawnerEntity.wrath -= cost;
                slothful = false;
            } else if (mobSpawnerEntity.sloth >= cost) {
                mobSpawnerEntity.sloth -= cost;
                slothful = true;
            } else return;
            fuel = Math.max(0, Config.wrathEff);
            mobSpawnerEntity.sync();
        }
        if (spawnDelay == -1) resetDelay();
        if (Config.wrathCost > 0 && fuel <= 0) return;
        if (spawnDelay > 0) {
            --spawnDelay;
            if (spawnDelay % 20 == 0) mobSpawnerEntity.markDirty();
            return;
        }
        ResourceLocation id = previewRegistryName(mobID);
        if (id == null) { resetDelay(); return; }
        boolean spawned = false;
        for (int i = 0; i < spawnCount && (fuel > 0 || Config.wrathCost <= 0); ++i) {
            Entity candidate = EntityList.createEntityByIDFromName(id, world);
            if (!(candidate instanceof EntityLiving)) break;
            EntityLiving entity = (EntityLiving) candidate;
            AxisAlignedBB nearby = new AxisAlignedBB(mobSpawnerEntity.getPos()).grow(spawnRange * 2.0, 4.0, spawnRange * 2.0);
            if (world.getEntitiesWithinAABB(entity.getClass(), nearby).size() >= maxNearbyEntities) {
                resetDelay();
                return;
            }
            double x = getSpawnerX() + 0.5 + (world.rand.nextDouble() - world.rand.nextDouble()) * spawnRange;
            double y = getSpawnerY() + world.rand.nextInt(3) - 1;
            double z = getSpawnerZ() + 0.5 + (world.rand.nextDouble() - world.rand.nextDouble()) * spawnRange;
            entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0f, 0.0f);
            if (!entityCanSpawn(entity)) continue;
            entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData) null);
            if (world.spawnEntity(entity)) {
                world.playEvent(2004, mobSpawnerEntity.getPos(), 0);
                entity.spawnExplosionParticle();
                spawned = true;
                if (Config.wrathCost > 0) --fuel;
            }
        }
        // Delay applies to a completed attempt, including blocked or invalid locations.
        resetDelay();
        if (spawned) mobSpawnerEntity.sync();
    }

    public boolean entityCanSpawn(EntityLiving entity) {
        if (entity == null || entity.world == null) return false;
        AxisAlignedBB bounds = entity.getEntityBoundingBox();
        return entity.world.checkNoEntityCollision(bounds)
                && entity.world.getCollisionBoxes(entity, bounds).isEmpty()
                && !entity.world.containsAnyLiquid(bounds);
    }

    private void resetDelay() {
        World world = getSpawnerWorld();
        if (world == null) return;
        spawnDelay = maxSpawnDelay <= minSpawnDelay ? minSpawnDelay
                : minSpawnDelay + world.rand.nextInt(maxSpawnDelay - minSpawnDelay);
        if (slothful) spawnDelay += 200;
        updateSpawnerBlock(1);
    }

    public void readFromNBT(NBTTagCompound tag) {
        String loadedId = tag.getString("EntityId");
        if (!loadedId.isEmpty()) mobID = loadedId;
        aspect = Config.spawnerMobs.containsKey(mobID) ? Config.spawnerMobs.get(mobID) : Aspect.GREED;
        mobSet = tag.getBoolean("MobSet");
        slothful = tag.getBoolean("Slothful");
        fuel = Math.max(0, tag.getShort("Fuel"));
        spawnDelay = tag.getShort("Delay");
        if (tag.hasKey("MaxNearbyEntities")) maxNearbyEntities = Math.max(1, tag.getShort("MaxNearbyEntities"));
        if (tag.hasKey("SpawnRange")) spawnRange = Math.max(0, Math.min(16, tag.getShort("SpawnRange")));
        renderEntity = null;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("EntityId", mobID);
        tag.setBoolean("MobSet", mobSet);
        tag.setBoolean("Slothful", slothful);
        tag.setShort("Fuel", (short) fuel);
        tag.setShort("Delay", (short) spawnDelay);
        tag.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
        tag.setShort("SpawnRange", (short) spawnRange);
    }

    public boolean setDelayToMin(int id) {
        World world = getSpawnerWorld();
        if (id == 1 && world != null && world.isRemote) {
            spawnDelay = minSpawnDelay;
            return true;
        }
        return false;
    }

    /** Legacy 1.7.10 mob IDs stay in saved NBT. Normalize only for entity creation. */
    public static ResourceLocation previewRegistryName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String path;
        switch (name) {
            case "PigZombie": path = "minecraft:zombie_pigman"; break;
            case "MushroomCow": path = "minecraft:mooshroom"; break;
            case "Ozelot": path = "minecraft:ocelot"; break;
            case "LavaSlime": path = "minecraft:magma_cube"; break;
            case "CaveSpider": path = "minecraft:cave_spider"; break;
            case "EntityHorse": path = "minecraft:horse"; break;
            default:
                if (name.startsWith("Thaumcraft.")) path = "thaumcraft:" + name.substring(10).toLowerCase(Locale.ROOT);
                else if (name.indexOf(':') >= 0) path = name.toLowerCase(Locale.ROOT);
                else path = "minecraft:" + name.toLowerCase(Locale.ROOT);
        }
        try { return new ResourceLocation(path); }
        catch (IllegalArgumentException badId) { return null; }
    }

    @SideOnly(Side.CLIENT)
    public Entity getEntityForRender() {
        if (renderEntity != null) return renderEntity;
        World world = getSpawnerWorld();
        ResourceLocation id = previewRegistryName(mobID);
        if (world == null || id == null) return null;
        renderEntity = EntityList.createEntityByIDFromName(id, world); // Never spawn the preview.
        return renderEntity;
    }
}
