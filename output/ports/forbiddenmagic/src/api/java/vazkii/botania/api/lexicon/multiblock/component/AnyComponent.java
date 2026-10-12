/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [01/11/2015, 19:05:57 (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A multiblock component that matches any non air blocks that have a collision box.
 */
public class AnyComponent extends MultiblockComponent {

	public AnyComponent(BlockPos relPos, Block block, int meta) {
		super(relPos, block, meta);
	}

	@Override
	public boolean matches(World world, int x, int y, int z) {
		Block block = fox.spiteful.forbidden.compat.LegacyWorldAccess.getBlock(world, x, y, z);
		return !world.isAirBlock(new net.minecraft.util.math.BlockPos(x, y, z)) && world.getBlockState(new net.minecraft.util.math.BlockPos(x, y, z)).getCollisionBoundingBox(world, new net.minecraft.util.math.BlockPos(x, y, z)) != net.minecraft.block.Block.NULL_AABB;
	}

}
