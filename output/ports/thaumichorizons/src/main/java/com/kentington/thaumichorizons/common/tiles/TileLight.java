//
// Decompiled by Procyon v0.5.30
//

package com.kentington.thaumichorizons.common.tiles;

import net.minecraft.util.math.BlockPos;
import java.awt.Color;

import net.minecraft.tileentity.TileEntity;

import com.kentington.thaumichorizons.common.ThaumicHorizons;
import com.kentington.thaumichorizons.common.items.ItemFocusIllumination;

public class TileLight extends TileEntity  implements net.minecraft.util.ITickable {

    int md;
    Color col;

    public TileLight() {
        this.md = -1;
        this.col = null;
    }

    public boolean canUpdate() {
        return true;
    }

    public void update() {
        if (this.world.isRemote) {
            if (this.md == -1) {
                this.md = this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())).getBlock().getMetaFromState(this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())));
                this.col = new Color(ItemFocusIllumination.colors[this.md]);
            }
            ThaumicHorizons.proxy
                    .illuminationFX(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.md, this.col);
        }
    }
}
