import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import mechanics.PuppetMunchMan;
import mechanics.behavior.EnemyBlueBehavior;
import mechanics.behavior.EnemyPinkBehavior;
import mechanics.behavior.EnemyRedBehavior;
import mechanics.behavior.EnemyYellowBehavior;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyCowardiceFlankHuntBehavior;
import mechanics.movement.EntityMovement;
import mechanics.stage.PlaceStageChain;
//import mechanics.stage.PlaceStageChain;
import fundamentals.Constants;
import fundamentals.UI.*;
import fundamentals.animation.Animation;

import components.Enemy;
import components.MunchMan;
import components.Stage;
//import components.StageChain;
import components.StageChain;

public class AppContainer extends AppBase
{
    private final int PLAYER_DEF_SPEED = 1;
    private final int ENEMY_DEF_SPEED = 1;

    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private StageChain stage_chain = new StageChain();

    private MunchMan munch_man = new MunchMan(23, 15, 0);
    private MunchMan left_puppet_munch_man = new MunchMan(-1, -1, 0);
    private MunchMan right_puppet_munch_man = new MunchMan(-1, -1, 0);

    private Enemy enemy_red = new Enemy(1, 1, ENEMY_DEF_SPEED, new Animation("red_enemy.png"), new Animation("vulnerable_enemy.png"));
    private Enemy enemy_yellow = new Enemy(1, 30, ENEMY_DEF_SPEED, new Animation("orange_enemy.png"), new Animation("vulnerable_enemy.png"));
    private Enemy enemy_blue = new Enemy(8, 1, ENEMY_DEF_SPEED, new Animation("blue_enemy.png"), new Animation("vulnerable_enemy.png"));
    private Enemy enemy_pink = new Enemy(41, 30, ENEMY_DEF_SPEED, new Animation("pink_enemy.png"), new Animation("vulnerable_enemy.png"));

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private PuppetMunchMan puppet_munch_man = new PuppetMunchMan(munch_man, left_puppet_munch_man, right_puppet_munch_man);

    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private EntityMovement enemy_red_movement = new EntityMovement(stage, enemy_red);
    private EntityMovement enemy_yellow_movement = new EntityMovement(stage, enemy_yellow);
    private EntityMovement enemy_blue_movement = new EntityMovement(stage, enemy_blue);
    private EntityMovement enemy_pink_movement = new EntityMovement(stage, enemy_pink);

    private EnemyRedBehavior enemy_red_behavior = new EnemyRedBehavior(enemy_red_movement, stage, enemy_red, munch_man);
    private EnemyYellowBehavior enemy_yellow_behavior = new EnemyYellowBehavior(enemy_yellow_movement, stage, enemy_yellow, munch_man);
    private EnemyBlueBehavior enemy_blue_behavior = new EnemyBlueBehavior(enemy_blue_movement, stage, enemy_blue, enemy_red, munch_man);
    private EnemyPinkBehavior enemy_pink_behavior = new EnemyPinkBehavior(enemy_pink_movement, stage, enemy_pink, enemy_red, enemy_yellow, enemy_blue, munch_man);

    private void configureButtonBindings() 
    {    
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-PLAYER_DEF_SPEED, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(PLAYER_DEF_SPEED, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -PLAYER_DEF_SPEED); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, PLAYER_DEF_SPEED); }));
    }

    public AppContainer() 
    {
        configureButtonBindings();
        // Basic Mechanics:
        player_movement.schedule();
        puppet_munch_man.schedule();
        place_stage_chain.schedule();
        // Basic Enemy Mechanics:
        enemy_red_movement.enableDirectionalAnimations(false);
        enemy_yellow_movement.enableDirectionalAnimations(false);
        enemy_blue_movement.enableDirectionalAnimations(false);
        enemy_pink_movement.enableDirectionalAnimations(false);
        // Enemy Behaviors:
        //enemy_red_behavior.schedule();
        //enemy_yellow_behavior.schedule();
        //enemy_blue_behavior.schedule();
        enemy_pink_behavior.schedule();
    }

    public void periodic() 
    {
        
    }
}
