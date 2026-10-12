//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.entities;

import net.minecraft.entity.passive.EntityPig;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import com.kentington.thaumichorizons.common.entities.ai.EntityAIEatTaint;

import thaumcraft.common.config.Config;

public class EntityTaintPig extends EntityPig {

    public EntityTaintPig(final World p_i1689_1_) {
        super(p_i1689_1_);
        this.tasks.addTask(9, new EntityAIEatTaint(this));
    }

    public void updateAITasks() {
        super.updateAITasks();
        if (this.getActivePotionEffect(Config.potionFluxTaint) != null) {
            this.removePotionEffect(Config.potionFluxTaint);
        }
        if (this.getActivePotionEffect(Config.potionInfectiousVisExhaust) != null) {
            this.removePotionEffect(Config.potionInfectiousVisExhaust);
        }
        if (this.getActivePotionEffect(Config.potionVisExhaust) != null) {
            this.removePotionEffect(Config.potionVisExhaust);
        }
    }
}
