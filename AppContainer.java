import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import fundamentals.mechanic.SequentialMechanicGroup;
import mechanics.EnemyChaseTarget;
import mechanics.EnemyMovement;
import mechanics.EnemyPredeterminedRoute;
import mechanics.EntityMovement;
import mechanics.PlaceStageChain;
import fundamentals.Constants;
import fundamentals.Coordinates;
import fundamentals.UI.*;
import fundamentals.animation.Animation;

import java.util.LinkedList;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import components.StageChain;

public class AppContainer extends AppBase
{
    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private StageChain stage_chain = new StageChain();
    private MunchMan munch_man = new MunchMan();
    private Enemy enemy_A = new Enemy(1, 1, 9, new Animation("enemy.png"));
    private Enemy enemy_B = new Enemy(1, 30, 9, new Animation("enemy.png"));
    private Enemy enemy_C = new Enemy(42, 1, 9, new Animation("enemy.png"));
    private Enemy enemy_D = new Enemy(40, 30, 9, new Animation("enemy.png"));
    

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private EntityMovement player_movement = new EntityMovement(stage, munch_man);

    private EntityMovement enemy_A_movement = new EntityMovement(stage, enemy_A);
    private EntityMovement enemy_B_movement = new EntityMovement(stage, enemy_B);
    private EntityMovement enemy_C_movement = new EntityMovement(stage, enemy_C);
    private EntityMovement enemy_D_movement = new EntityMovement(stage, enemy_D);

    private EnemyChaseTarget enemy_A_chase_target = new EnemyChaseTarget(enemy_A_movement, stage, munch_man, enemy_A);
    private EnemyChaseTarget enemy_B_chase_target = new EnemyChaseTarget(enemy_B_movement, stage, munch_man, enemy_B);
    private EnemyChaseTarget enemy_C_chase_target = new EnemyChaseTarget(enemy_C_movement, stage, munch_man, enemy_C);
    private EnemyChaseTarget enemy_D_chase_target = new EnemyChaseTarget(enemy_D_movement, stage, munch_man, enemy_D);


    private void configureButtonBindings() {
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-8, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(8, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -8); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, 8); }));
    }

    public AppContainer() {
        configureButtonBindings();
        player_movement.schedule();
        place_stage_chain.schedule();

        enemy_A_movement.schedule();
        enemy_B_movement.schedule();
        enemy_C_movement.schedule();
        enemy_D_movement.schedule();

        enemy_A_chase_target.schedule();
        enemy_B_chase_target.schedule();
        enemy_C_chase_target.schedule();
        enemy_D_chase_target.schedule();
    }
}
