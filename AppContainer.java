import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import fundamentals.mechanic.MechanicScheduler;
import mechanics.PuppetMunchMan;
import mechanics.behavior.EnemyRedBehavior;
import mechanics.behavior.lowerlevel.EnemyFlankHuntBehavior;
import mechanics.behavior.lowerlevel.EnemyHuntBehavior;
import mechanics.behavior.lowerlevel.EnemyRetreatingWanderBehavior;
import mechanics.movement.EntityMovement;
//import mechanics.stage.PlaceStageChain;
import fundamentals.Constants;
import fundamentals.UI.*;
import fundamentals.animation.Animation;

import components.Enemy;
import components.MunchMan;
import components.Stage;
//import components.StageChain;

public class AppContainer extends AppBase
{
    private final int PLAYER_DEF_SPEED = 1;
    private final int ENEMY_DEF_SPEED = 1;

    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    //private StageChain stage_chain = new StageChain();
    private MunchMan munch_man = new MunchMan(23, 15, 0);
    private MunchMan left_puppet_munch_man = new MunchMan(-1, -1, 0);
    private MunchMan right_puppet_munch_man = new MunchMan(-1, -1, 0);
    private Enemy enemy_red = new Enemy(2, 1, ENEMY_DEF_SPEED, new Animation("enemy.png"), new Animation("enemy_blue.png"));
    //private Enemy enemy_yellow = new Enemy(1, 30, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    //private Enemy enemy_blue = new Enemy(42, 1, ENEMY_DEF_SPEED, new Animation("enemy.png"));
    //private Enemy enemy_pink = new Enemy(40, 30, ENEMY_DEF_SPEED, new Animation("enemy.png"));

    //private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private PuppetMunchMan puppet_munch_man = new PuppetMunchMan(munch_man, left_puppet_munch_man, right_puppet_munch_man);
    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private EntityMovement enemy_red_movement = new EntityMovement(stage, enemy_red);

    private EnemyRedBehavior enemy_red_behavior = new EnemyRedBehavior(enemy_red_movement, stage, enemy_red, munch_man);

    private void configureButtonBindings() {
        
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-PLAYER_DEF_SPEED, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(PLAYER_DEF_SPEED, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -PLAYER_DEF_SPEED); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, PLAYER_DEF_SPEED); }));
    }

    public AppContainer() {
        configureButtonBindings();
        player_movement.schedule();
        puppet_munch_man.schedule();
        enemy_red_movement.enableDirectionalAnimations(false);
        enemy_red_behavior.schedule();
        //place_stage_chain.schedule();
    }

    public void periodic() 
    {
        
    }
}
