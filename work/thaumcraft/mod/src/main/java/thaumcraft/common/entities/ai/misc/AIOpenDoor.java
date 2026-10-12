package thaumcraft.common.entities.ai.misc;

import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIOpenDoor extends net.minecraft.entity.ai.EntityAIOpenDoor {
    public AIOpenDoor(EntityGolemBase golem) {
        this(golem, true);
    }

    public AIOpenDoor(EntityGolemBase golem, boolean closeDoor) {
        super(golem, closeDoor);
        ((net.minecraft.pathfinding.PathNavigateGround) golem.getNavigator()).setBreakDoors(true);
    }
}
