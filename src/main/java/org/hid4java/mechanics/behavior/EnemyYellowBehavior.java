package org.hid4java.mechanics.behavior;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors.EnemyCowardiceFlankHuntBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyYellowBehavior extends EnemyBehaviorGroup
{
    public EnemyYellowBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, -1, -1, false, 
            new EnemyCowardiceFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            7, 3, 
            0, 0.8, 4, 
            25, 10))
        );
    }
}