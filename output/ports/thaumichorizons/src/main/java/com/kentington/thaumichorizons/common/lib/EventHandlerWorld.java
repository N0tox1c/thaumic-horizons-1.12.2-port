//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.lib;

import net.minecraftforge.event.world.WorldEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerWorld {

    @SubscribeEvent
    public void worldLoad(final WorldEvent.Load event) {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
            PocketPlaneData.loadPocketPlanes(event.getWorld());
        }
    }

    @SubscribeEvent
    public void worldSave(final WorldEvent.Save event) {
        if (!event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
            PocketPlaneData.savePocketPlanes(event.getWorld());
        }
    }
}
