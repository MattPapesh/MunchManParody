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
    private Enemy enemy = new Enemy(1, 1, 6, new Animation("enemy.png"));

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private EntityMovement enemy_movement = new EntityMovement(stage, enemy);
    private EnemyChaseTarget enemy_chase_target = new EnemyChaseTarget(enemy_movement, stage, munch_man, enemy);

    private void configureButtonBindings() {
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-6, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(6, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -6); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, 6); }));
    }

    public AppContainer() {
        configureButtonBindings();
        player_movement.schedule();
        place_stage_chain.schedule();
        enemy_movement.schedule();
        enemy_chase_target.schedule();
        enemy_chase_target.getRoute(10, 5).schedule();
    }
}
