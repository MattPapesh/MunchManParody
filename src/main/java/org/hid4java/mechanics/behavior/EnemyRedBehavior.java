package org.hid4java.mechanics.behavior;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyFlankHuntBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyRedBehavior extends EnemyBehaviorGroup
{
    public EnemyRedBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(0.5, 5000, 5000, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            7,5, 180)),

            new behavior(0.5, 5000, 5000, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            0, 0, 0)),

            new behavior(1.0, 10000, 0, false,
            new EnemyAnchorBehavior(enemy_movement, stage, enemy, munch_man, new anchor_data() {
                @Override public double getTurnAroundProbabilityPercentage() {return 0.3;}
                @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
                @Override public int getRadius() {return 8;}
            }))
        );
    }
}