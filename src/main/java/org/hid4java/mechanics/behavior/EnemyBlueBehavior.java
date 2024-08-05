package org.hid4java.mechanics.behavior;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.PowerPellet;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Constants;
import org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors.EnemySideKickBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors.WeakenedEnemyBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyBlueBehavior extends EnemyBehaviorGroup
{
    public EnemyBlueBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, Enemy hero, MunchMan munch_man, 
    PowerPellet A, PowerPellet B, PowerPellet C, PowerPellet D)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (   new behavior(0, 10000, 0, true,
            new WeakenedEnemyBehavior(enemy_movement, stage, enemy, munch_man, A, B, C, D, Constants.POWER_PELLOT_DURATION_MILLIS)),

            new behavior(1.0, 5000, 0, false, 
            new EnemySideKickBehavior(enemy_movement, stage, enemy, hero, munch_man))
        );
    }
}