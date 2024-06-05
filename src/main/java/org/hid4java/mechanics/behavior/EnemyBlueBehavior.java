package org.hid4java.mechanics.behavior;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors.EnemySideKickBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyBlueBehavior extends EnemyBehaviorGroup
{
    public EnemyBlueBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, Enemy hero, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, 5000, 0, false, 
            new EnemySideKickBehavior(enemy_movement, stage, enemy, hero, munch_man))
        );
    }
}