//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.client;

import com.kentington.thaumichorizons.client.fx.THSparkle;
import net.minecraft.util.math.BlockPos;


import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import com.kentington.thaumichorizons.client.audio.VortexSound;
import com.kentington.thaumichorizons.client.fx.FXSonic;
import com.kentington.thaumichorizons.client.gui.GuiBloodInfuser;
import com.kentington.thaumichorizons.client.gui.GuiCase;
import com.kentington.thaumichorizons.client.gui.GuiFingers;
import com.kentington.thaumichorizons.client.gui.GuiInjector;
import com.kentington.thaumichorizons.client.gui.GuiInspiratron;
import com.kentington.thaumichorizons.client.gui.GuiSoulExtractor;
import com.kentington.thaumichorizons.client.gui.GuiSoulforge;
import com.kentington.thaumichorizons.client.gui.GuiVat;
import com.kentington.thaumichorizons.client.gui.GuiVisDynamo;
import com.kentington.thaumichorizons.client.renderer.block.BlockBloodInfuserRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockEssentiaDynamoRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockInspiratronRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockJarTHRenderer;
import com.kentington.thaumichorizons.client.renderer.block.BlockNodeMonitorRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockRecombinatorRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockSlotRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockSoulBeaconRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockSoulSieveRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockSoulforgeRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockSpikeRenderer;
import com.kentington.thaumichorizons.client.renderer.block.BlockSyntheticNodeRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockTransductionAmplifierRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVatInteriorRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVatMatrixRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVatRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVatSolidRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVisDynamoRender;
import com.kentington.thaumichorizons.client.renderer.block.BlockVortexStabilizerRender;
import com.kentington.thaumichorizons.client.renderer.entity.BlastPhialRender;
import com.kentington.thaumichorizons.client.renderer.entity.RenderAlchemitePrimed;
import com.kentington.thaumichorizons.client.renderer.entity.RenderBoatGreatwood;
import com.kentington.thaumichorizons.client.renderer.entity.RenderBoatThaumium;
import com.kentington.thaumichorizons.client.renderer.entity.RenderChocolateCow;
import com.kentington.thaumichorizons.client.renderer.entity.RenderEndersteed;
import com.kentington.thaumichorizons.client.renderer.entity.RenderFamiliar;
import com.kentington.thaumichorizons.client.renderer.entity.RenderGoldChicken;
import com.kentington.thaumichorizons.client.renderer.entity.RenderGolemTH;
import com.kentington.thaumichorizons.client.renderer.entity.RenderGravekeeper;
import com.kentington.thaumichorizons.client.renderer.entity.RenderGuardianPanther;
import com.kentington.thaumichorizons.client.renderer.entity.RenderLightningBoltFinite;
import com.kentington.thaumichorizons.client.renderer.entity.RenderLunarWolf;
import com.kentington.thaumichorizons.client.renderer.entity.RenderMeatSlime;
import com.kentington.thaumichorizons.client.renderer.entity.RenderMedSlime;
import com.kentington.thaumichorizons.client.renderer.entity.RenderMercurialSlime;
import com.kentington.thaumichorizons.client.renderer.entity.RenderNetherHound;
import com.kentington.thaumichorizons.client.renderer.entity.RenderNightmare;
import com.kentington.thaumichorizons.client.renderer.entity.RenderOreBoar;
import com.kentington.thaumichorizons.client.renderer.entity.RenderScholarChicken;
import com.kentington.thaumichorizons.client.renderer.entity.RenderSeawolf;
import com.kentington.thaumichorizons.client.renderer.entity.RenderSheeder;
import com.kentington.thaumichorizons.client.renderer.entity.RenderSoul;
import com.kentington.thaumichorizons.client.renderer.entity.RenderSyringe;
import com.kentington.thaumichorizons.client.renderer.entity.RenderTaintfeeder;
import com.kentington.thaumichorizons.client.renderer.entity.RenderVoltSlime;
import com.kentington.thaumichorizons.client.renderer.item.ItemCorpseEffigyRender;
import com.kentington.thaumichorizons.client.renderer.item.ItemInjectorRender;
import com.kentington.thaumichorizons.client.renderer.item.ItemSyringeRender;
import com.kentington.thaumichorizons.client.renderer.model.ModelFamiliar;
import com.kentington.thaumichorizons.client.renderer.model.ModelGolemTH;
import com.kentington.thaumichorizons.client.renderer.tile.ItemJarTHRenderer;
import com.kentington.thaumichorizons.client.renderer.tile.TileBloodInfuserRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileCloudRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileEssentiaDynamoRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileEtherealShardRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileInspiratronRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileJarTHRenderer;
import com.kentington.thaumichorizons.client.renderer.tile.TileNodeMonitorRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileRecombinatorRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileSlotRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileSoulBeaconRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileSoulSieveRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileSoulforgeRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileSpikeRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileTransductionAmplifierRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileVatMatrixRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileVatSlaveRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileVisDynamoRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileVortexRender;
import com.kentington.thaumichorizons.client.renderer.tile.TileVortexStabilizerRender;
import com.kentington.thaumichorizons.common.CommonProxy;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.entities.EntityAlchemitePrimed;
import com.kentington.thaumichorizons.common.entities.EntityBlastPhial;
import com.kentington.thaumichorizons.common.entities.EntityBoatGreatwood;
import com.kentington.thaumichorizons.common.entities.EntityBoatThaumium;
import com.kentington.thaumichorizons.common.entities.EntityChocolateCow;
import com.kentington.thaumichorizons.common.entities.EntityEndersteed;
import com.kentington.thaumichorizons.common.entities.EntityFamiliar;
import com.kentington.thaumichorizons.common.entities.EntityGoldChicken;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityGravekeeper;
import com.kentington.thaumichorizons.common.entities.EntityGuardianPanther;
import com.kentington.thaumichorizons.common.entities.EntityLightningBoltFinite;
import com.kentington.thaumichorizons.common.entities.EntityLunarWolf;
import com.kentington.thaumichorizons.common.entities.EntityMeatSlime;
import com.kentington.thaumichorizons.common.entities.EntityMedSlime;
import com.kentington.thaumichorizons.common.entities.EntityMercurialSlime;
import com.kentington.thaumichorizons.common.entities.EntityNetherHound;
import com.kentington.thaumichorizons.common.entities.EntityNightmare;
import com.kentington.thaumichorizons.common.entities.EntityOrePig;
import com.kentington.thaumichorizons.common.entities.EntityScholarChicken;
import com.kentington.thaumichorizons.common.entities.EntitySeawolf;
import com.kentington.thaumichorizons.common.entities.EntitySheeder;
import com.kentington.thaumichorizons.common.entities.EntitySoul;
import com.kentington.thaumichorizons.common.entities.EntitySyringe;
import com.kentington.thaumichorizons.common.entities.EntityTaintPig;
import com.kentington.thaumichorizons.common.entities.EntityVoltSlime;
import com.kentington.thaumichorizons.common.items.WandManagerTH;
import com.kentington.thaumichorizons.common.lib.THKeyHandler;
import com.kentington.thaumichorizons.common.tiles.TileBloodInfuser;
import com.kentington.thaumichorizons.common.tiles.TileCloud;
import com.kentington.thaumichorizons.common.tiles.TileEssentiaDynamo;
import com.kentington.thaumichorizons.common.tiles.TileInspiratron;
import com.kentington.thaumichorizons.common.tiles.TileNodeMonitor;
import com.kentington.thaumichorizons.common.tiles.TileRecombinator;
import com.kentington.thaumichorizons.common.tiles.TileSlot;
import com.kentington.thaumichorizons.common.tiles.TileSoulBeacon;
import com.kentington.thaumichorizons.common.tiles.TileSoulExtractor;
import com.kentington.thaumichorizons.common.tiles.TileSoulJar;
import com.kentington.thaumichorizons.common.tiles.TileSoulforge;
import com.kentington.thaumichorizons.common.tiles.TileSpike;
import com.kentington.thaumichorizons.common.tiles.TileSyntheticNode;
import com.kentington.thaumichorizons.common.tiles.TileTransductionAmplifier;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatMatrix;
import com.kentington.thaumichorizons.common.tiles.TileVatSlave;
import com.kentington.thaumichorizons.common.tiles.TileVisDynamo;
import com.kentington.thaumichorizons.common.tiles.TileVortex;
import com.kentington.thaumichorizons.common.tiles.TileVortexStabilizer;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXBurst;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.client.renderers.item.ItemWandRenderer;
import thaumcraft.common.Thaumcraft;

public class ClientProxy extends CommonProxy {

    public IWandTriggerManager wandManager;

    public ClientProxy() {
        this.wandManager = new WandManagerTH();
    }

    @Override
    public void registerHandlers() {
        ThaumicHorizons.instance.renderEventHandler = new com.kentington.thaumichorizons.client.lib.RenderEventHandler();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LegacyTextureRegistration());
        NativeLegacyModels legacyModels = new NativeLegacyModels();
        net.minecraftforge.client.model.ModelLoaderRegistry.registerLoader(legacyModels);
        MinecraftForge.EVENT_BUS.register(legacyModels);
        MinecraftForge.EVENT_BUS.register(ThaumicHorizons.instance.renderEventHandler);
        final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).registerReloadListener(ignored -> FXSonic.model = null);
        }
    }

    @Override
    public void registerKeyBindings() {
        FMLCommonHandler.instance().bus().register(new THKeyHandler());
    }

    @Override
    public void scheduleClientPacket(Runnable task) {
        net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
            if (net.minecraft.client.Minecraft.getMinecraft().world != null
                    && net.minecraft.client.Minecraft.getMinecraft().player != null) task.run();
        });
    }

    @Override
    public void registerRenderers() {
        // spotless:off
        ClientRegistry.bindTileEntitySpecialRenderer(TileNodeMonitor.class, new TileNodeMonitorRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSyntheticNode.class, new TileEtherealShardRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileVisDynamo.class, new TileVisDynamoRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaDynamo.class, new TileEssentiaDynamoRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSoulExtractor.class, new TileSoulSieveRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileInspiratron.class, new TileInspiratronRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSoulforge.class, new TileSoulforgeRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileVatSlave.class, new TileVatSlaveRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileVatMatrix.class, new TileVatMatrixRender(0));
        ClientRegistry.bindTileEntitySpecialRenderer(TileBloodInfuser.class, new TileBloodInfuserRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSoulBeacon.class, new TileSoulBeaconRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTransductionAmplifier.class, new TileTransductionAmplifierRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRecombinator.class, new TileRecombinatorRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileVortexStabilizer.class, new TileVortexStabilizerRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileVortex.class, new TileVortexRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSpike.class, new TileSpikeRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCloud.class, new TileCloudRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSlot.class, new TileSlotRender());
        RenderingRegistry.registerEntityRenderingHandler(EntityAlchemitePrimed.class, new RenderAlchemitePrimed());
        RenderingRegistry.registerEntityRenderingHandler(EntitySyringe.class, RenderSyringe::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBlastPhial.class, BlastPhialRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChocolateCow.class, RenderChocolateCow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityOrePig.class, RenderOreBoar::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGuardianPanther.class, RenderGuardianPanther::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFamiliar.class, RenderFamiliar::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGravekeeper.class, RenderGravekeeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldChicken.class, RenderGoldChicken::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityScholarChicken.class, RenderScholarChicken::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTaintPig.class, RenderTaintfeeder::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNetherHound.class, RenderNetherHound::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySeawolf.class, RenderSeawolf::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLunarWolf.class, RenderLunarWolf::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGolemTH.class, RenderGolemTH::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityEndersteed.class, RenderEndersteed::new);
        RenderingRegistry.registerEntityRenderingHandler(com.kentington.thaumichorizons.common.entities.EntityHorseUndead.class, net.minecraft.client.renderer.entity.RenderAbstractHorse::new);
        RenderingRegistry.registerEntityRenderingHandler(com.kentington.thaumichorizons.common.entities.EntityHorseUndeadS.class, net.minecraft.client.renderer.entity.RenderAbstractHorse::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNightmare.class, RenderNightmare::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoatGreatwood.class, RenderBoatGreatwood::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoatThaumium.class, RenderBoatThaumium::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMeatSlime.class, RenderMeatSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMercurialSlime.class, RenderMercurialSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityVoltSlime.class, RenderVoltSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMedSlime.class, RenderMedSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySheeder.class, RenderSheeder::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySoul.class, RenderSoul::new);
        RenderingRegistry.registerEntityRenderingHandler(com.kentington.thaumichorizons.common.entities.EntityWizardCow.class, com.kentington.thaumichorizons.client.renderer.entity.RenderWizardCow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLightningBoltFinite.class, RenderLightningBoltFinite::new);
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemSyringeBloodSample, new ItemSyringeRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemSyringeHuman, new ItemSyringeRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemSyringeEmpty, new ItemSyringeRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemSyringeInjection, new ItemSyringeRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemCorpseEffigy, new ItemCorpseEffigyRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemInjector, new ItemInjectorRender());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.itemWandCastingDisposable, new ItemWandRenderer());
        // spotless:on
    }

    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z) {
        if (world instanceof WorldClient) {
            switch (ID) {
    case 1: {
                    return new GuiVisDynamo(player, (TileVisDynamo) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 2: {
                    return new GuiSoulExtractor(player.inventory, (TileSoulExtractor) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 3: {
                    return new GuiInspiratron(player.inventory, (TileInspiratron) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 4: {
                    return new GuiSoulforge(player, (TileSoulforge) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 5: {
                    return new GuiBloodInfuser(player, (TileBloodInfuser) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 6: {
                    return new GuiInjector(player);
                }
    case 7: {
                    return new GuiVat(player, (TileVat) world.getTileEntity(new BlockPos(x, y, z)));
                }
    case 8: {
                    return new GuiCase(player.inventory, world, x, y, z);
                }
    case 9: {
                    return new GuiFingers(player.inventory);
                }
}
        }
        return null;
    }

    @Override
    public void registerDisplayInformation() {
        ThaumicHorizons.blockJarRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockJar, new BlockJarTHRenderer());
        com.kentington.thaumichorizons.client.renderer.item.NativeItemRenderRegistration.registerItemRenderer(ThaumicHorizons.blockSoulJarItem, new ItemJarTHRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSoulJar.class, new TileJarTHRenderer());
        ThaumicHorizons.blockSyntheticNodeRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSynthNode, new BlockSyntheticNodeRender());
        ThaumicHorizons.blockNodeMonRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockNodeMonitor, new BlockNodeMonitorRender());
        ThaumicHorizons.blockVisDynamoRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockVisDynamo, new BlockVisDynamoRender());
        ThaumicHorizons.blockEssentiaDynamoRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockEssentiaDynamo, new BlockEssentiaDynamoRender());
        ThaumicHorizons.blockSoulSieveRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSoulSieve, new BlockSoulSieveRender());
        ThaumicHorizons.blockInspiratronRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockInspiratron, new BlockInspiratronRender());
        ThaumicHorizons.blockSoulforgeRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSoulforge, new BlockSoulforgeRender());
        ThaumicHorizons.blockVatRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockVat, new BlockVatRender());
        ThaumicHorizons.blockVatSolidRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockVatSolid, new BlockVatSolidRender());
        ThaumicHorizons.blockVatInteriorRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockVatInterior, new BlockVatInteriorRender());
        ThaumicHorizons.blockVatMatrixRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockModifiedMatrix, new BlockVatMatrixRender());
        ThaumicHorizons.blockBloodInfuserRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockBloodInfuser, new BlockBloodInfuserRender());
        ThaumicHorizons.blockSoulBeaconRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSoulBeacon, new BlockSoulBeaconRender());
        ThaumicHorizons.blockTransducerRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockTransducer, new BlockTransductionAmplifierRender());
        ThaumicHorizons.blockRecombinatorRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockRecombinator, new BlockRecombinatorRender());
        ThaumicHorizons.blockVortexStabilizerRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockVortexStabilizer, new BlockVortexStabilizerRender());
        ThaumicHorizons.blockSpikeRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSpike, new BlockSpikeRenderer());
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSpikeWood, new BlockSpikeRenderer());
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSpikeTooth, new BlockSpikeRenderer());
        ThaumicHorizons.blockSlotRI = com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.getNextAvailableRenderId();
        com.kentington.thaumichorizons.client.renderer.block.NativeBlockRenderRegistration.registerBlockHandler(ThaumicHorizons.blockSlot, new BlockSlotRender());
    }

    @Override
    public void disintegrateFX(final double blockX, final double blockY, final double blockZ, final EntityPlayer p,
            final int howMany, final boolean enlarged) {
        if (enlarged) {
            for (int x = -1; x < 2; ++x) {
                for (int y = -1; y < 2; ++y) {
                    for (int z = -1; z < 2; ++z) {
                        for (int i = 0; i < howMany; ++i) {
                            final THSparkle fx = new THSparkle(
                                    p.world,
                                    blockX + 0.5,
                                    blockY + 0.5,
                                    blockZ + 0.5,
                                    1.0f,
                                    0,
                                    6);
                            fx.setMotion((p.world.rand.nextDouble() - 0.5) / 4.0, (p.world.rand.nextDouble() - 0.5) / 4.0, (p.world.rand.nextDouble() - 0.5) / 4.0);
                            fx.disableCollision();
                            ParticleEngine.addEffect(p.world, fx);
                        }
                    }
                }
            }
        } else {
            for (int j = 0; j < howMany; ++j) {
                final THSparkle fx = new THSparkle(p.world, blockX + 0.5, blockY + 0.5, blockZ + 0.5, 1.0f, 0, 6);
                fx.setMotion((p.world.rand.nextDouble() - 0.5) / 4.0, (p.world.rand.nextDouble() - 0.5) / 4.0, (p.world.rand.nextDouble() - 0.5) / 4.0);
                fx.disableCollision();
                ParticleEngine.addEffect(p.world, fx);
            }
        }
    }

    @Override
    public void smeltFX(final double blockX, final double blockY, final double blockZ, final World w, final int howMany,
            final boolean enlarged) {
        if (enlarged) {
            for (int x = -1; x < 2; ++x) {
                for (int y = -1; y < 2; ++y) {
                    for (int z = -1; z < 2; ++z) {
                        for (int i = 0; i < howMany; ++i) {
                            w.spawnParticle(net.minecraft.util.EnumParticleTypes.FLAME, blockX + 0.5 + x, blockY + 0.5 + y, blockZ + 0.5 + z, (w.rand.nextDouble() - 0.5) * 0.25, (w.rand.nextDouble() - 0.5) * 0.25, (w.rand.nextDouble() - 0.5) * 0.25);
                        }
                    }
                }
            }
        } else {
            for (int j = 0; j < howMany; ++j) {
                w.spawnParticle(net.minecraft.util.EnumParticleTypes.FLAME, blockX + 0.5, blockY + 0.5, blockZ + 0.5, (w.rand.nextDouble() - 0.5) * 0.25, (w.rand.nextDouble() - 0.5) * 0.25, (w.rand.nextDouble() - 0.5) * 0.25);
            }
        }
    }

    @Override
    public void soulParticles(final int blockX, final int blockY, final int blockZ, final World world) {
        for (int i = 0; i < 10; ++i) {
            world.spawnParticle(net.minecraft.util.EnumParticleTypes.SPELL, blockX + 0.5 + (world.rand.nextDouble() - 0.5) * 0.8, blockY + 0.8, blockZ + 0.5 + (world.rand.nextDouble() - 0.5) * 0.8, 0.0, world.rand.nextDouble() * 0.25, 0.0);
        }
    }

    @Override
    public void containmentFX(final double blockX, final double blockY, final double blockZ, final EntityPlayer p,
            final Entity ent, final int times) {
        final double xSize = ent.getEntityBoundingBox().maxX - ent.getEntityBoundingBox().minX;
        final double ySize = ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY;
        final double zSize = ent.getEntityBoundingBox().maxZ - ent.getEntityBoundingBox().minZ;
        final double radius = (xSize > ySize) ? (Math.max(xSize, zSize)) : (Math.max(ySize, zSize));
        final double xCenter = (ent.getEntityBoundingBox().maxX + ent.getEntityBoundingBox().minX) / 2.0;
        final double yCenter = (ent.getEntityBoundingBox().maxY + ent.getEntityBoundingBox().minY) / 2.0;
        final double zCenter = (ent.getEntityBoundingBox().maxZ + ent.getEntityBoundingBox().minZ) / 2.0;
        for (int i = 0; i < times; ++i) {
            double theta = p.world.rand.nextDouble() * 3.141592653589793 * 2.0;
            double phi = p.world.rand.nextDouble() * 3.141592653589793 * 2.0;
            final double z1 = zCenter + radius * Math.cos(phi);
            final double y1 = yCenter + radius * Math.sin(phi) * Math.sin(theta);
            final double x1 = xCenter + radius * Math.sin(phi) * Math.cos(theta);
            theta = p.world.rand.nextDouble() * 3.141592653589793 * 2.0;
            phi = p.world.rand.nextDouble() * 3.141592653589793 * 2.0;
            final double z2 = zCenter + radius * Math.cos(phi);
            final double y2 = yCenter + radius * Math.sin(phi) * Math.sin(theta);
            final double x2 = xCenter + radius * Math.sin(phi) * Math.cos(theta);
            Thaumcraft.proxy.arcLightning(
                    p.world,
                    x1,
                    y1,
                    z1,
                    x2,
                    y2,
                    z2,
                    p.world.rand.nextFloat() * 0.1f,
                    p.world.rand.nextFloat() * 0.2f,
                    p.world.rand.nextFloat() * 0.8f,
                    p.world.rand.nextFloat());
        }
    }

    @Override
    public void disintegrateExplodeFX(final World worldObj, final double posX, final double posY, final double posZ) {
        final FXBurst ef = new FXBurst(worldObj, posX, posY, posZ, 1.0f, 1);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(ef);
    }

    @Override
    public void illuminationFX(final World world, final int xCoord, final int yCoord, final int zCoord, final int md,
            final Color col) {
        if (world.rand.nextInt(9 - Thaumcraft.proxy.particleCount(2)) == 0) {
            final FXWisp ef = new FXWisp(
                    world,
                    xCoord + 0.55f,
                    yCoord + 0.5f,
                    zCoord + 0.55f,
                    0.5f,
                    col.getRed() / 255.0f + 0.01f,
                    col.getGreen() / 255.0f,
                    col.getBlue() / 255.0f);
            ef.setGravity(0.0f);
            ef.shrink = false;
            if (md == 0) {
                ef.blendmode = 0;
            }
            ParticleEngine.instance.addEffect(world, ef);
        }
    }

    @Override
    public void blockSplosionFX(final int x, final int y, final int z, final Block block, final int md) {
        Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(new BlockPos(x, y, z), block.getStateFromMeta(md));
    }

    @Override
    public void alchemiteFX(final World worldObj, final double x, final double y, final double z) {
        final FXBurst ef = new FXBurst(worldObj, x, y, z, 10.0f, 1);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(ef);
    }

    @Override
    public boolean readyToRender() {
        return FMLClientHandler.instance().getClient().getRenderViewEntity() != null;
    }

    @Override
    public void addEffect(final Object effect) {
        if (!(effect instanceof Particle)) throw new IllegalArgumentException("Expected a particle effect");
        ParticleEngine.addEffect(FMLClientHandler.instance().getClient().world, (Particle) effect);
    }

    @Override
    public void lightningBolt(final World worldObj, final double x, final double y, final double z,
            final int boltLength) {
        Thaumcraft.proxy.arcLightning(
                worldObj,
                x + worldObj.rand.nextFloat() - 0.5,
                y + boltLength + 0.5,
                z + worldObj.rand.nextFloat() - 0.5,
                x + worldObj.rand.nextFloat() - 0.5,
                y + 1.0,
                z + worldObj.rand.nextFloat() - 0.5,
                0.8f,
                1.0f,
                1.0f,
                0.1f);
    }

    private final Map<TileCoord, VortexSound> activeVortexSounds = new HashMap<>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            activeVortexSounds.clear();
            ThaumicHorizons.instance.renderEventHandler.thingsThatSparkle.clear();
        }
    }

    @Override
    public void playVortexSound(TileVortex tile) {
        if (tile == null || tile.isInvalid() || tile.getWorld() == null) return;

        TileCoord pos = new TileCoord(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
        if (activeVortexSounds.containsKey(pos)) return;

        VortexSound sound = new VortexSound(tile);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        activeVortexSounds.put(pos, sound);
    }

    @Override
    public void stopVortexSound(TileVortex tile) {
        if (tile == null) return;

        TileCoord pos = new TileCoord(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
        VortexSound sound = activeVortexSounds.remove(pos);
        if (sound != null) {
            sound.markDone();
        }
    }

    // Inner class to replace BlockPos for 1.7.10
    private static class TileCoord {

        final int x, y, z;

        TileCoord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TileCoord)) return false;
            TileCoord other = (TileCoord) o;
            return this.x == other.x && this.y == other.y && this.z == other.z;
        }

        @Override
        public int hashCode() {
            return x * 31 * 31 + y * 31 + z;
        }
    }

}
