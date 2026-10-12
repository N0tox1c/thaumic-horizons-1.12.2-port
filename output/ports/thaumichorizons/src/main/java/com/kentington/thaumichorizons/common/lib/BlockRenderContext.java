package com.kentington.thaumichorizons.common.lib;

import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

/** Render-only properties: never serialized into chunks or network metadata. */
public final class BlockRenderContext {
    public static final IUnlistedProperty<IBlockAccess> WORLD=new Property<>("world",IBlockAccess.class);
    public static final IUnlistedProperty<BlockPos> POSITION=new Property<>("position",BlockPos.class);
    private static final class Property<T> implements IUnlistedProperty<T> {
        private final String name; private final Class<T> type;
        Property(String name,Class<T> type) { this.name=name; this.type=type; }
        public String getName() { return name; }
        public boolean isValid(T value) { return value!=null; }
        public Class<T> getType() { return type; }
        public String valueToString(T value) { return String.valueOf(value); }
    }
    private BlockRenderContext() {}
}
