import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import fundamentals.mechanic.SequentialMechanicGroup;
import mechanics.EnemyGoNearTarget;
import mechanics.EnemyHuntBehavior;
import mechanics.EntityMovement;
import mechanics.PlaceStageChain;
import fundamentals.Constants;
import fundamentals.UI.*;
import fundamentals.animation.Animation;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import components.StageChain;

public class AppContainer extends AppBase
{
    private final int PLAYER_DEF_SPEED = 2;
    private final int ENEMY_DEF_SPEED = 2;

    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private StageChain stage_chain = new StageChain();
    private MunchMan munch_man = new MunchMan();
    private Enemy enemy_A = new Enemy(1, 1, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    private Enemy enemy_B = new Enemy(1, 30, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    private Enemy enemy_C = new Enemy(42, 1, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    private Enemy enemy_D = new Enemy(40, 30, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private EntityMovement player_movement = new EntityMovement(stage, munch_man);

    private EnemyHuntBehavior enemy_A_hunt_behavior = new EnemyHuntBehavior(stage, enemy_A, munch_man);

    private void configureButtonBindings() {
        
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-PLAYER_DEF_SPEED, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(PLAYER_DEF_SPEED, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -PLAYER_DEF_SPEED); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, PLAYER_DEF_SPEED); }));
    }

    public AppContainer() {
        configureButtonBindings();
        player_movement.schedule();
        //place_stage_chain.schedule();

        enemy_A_hunt_behavior.schedule();
    }

    public void periodic() 
    {
        
    }
}
