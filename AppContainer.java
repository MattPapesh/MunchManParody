import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import fundamentals.mechanic.SequentialMechanicGroup;
import mechanics.EnemyMovement;
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
    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private StageChain stage_chain = new StageChain();
    private MunchMan munch_man = new MunchMan();
    private Enemy enemy = new Enemy(1, 1, 6, new Animation("enemy.png"));

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private EntityMovement enemy_movement = new EntityMovement(stage, enemy);

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

        SequentialMechanicGroup seq = new SequentialMechanicGroup();
        seq.addMechanics
        (
            new EnemyMovement(enemy_movement, enemy, 0, 3),
            new EnemyMovement(enemy_movement, enemy, 3, 0),
            new EnemyMovement(enemy_movement, enemy, 0, 5)//,
           // new EnemyMovement(enemy_movement, enemy, -20, 0)//,
            //new EnemyMovement(enemy_movement, enemy, 0, -8),
            //new EnemyMovement(enemy_movement, enemy, -9, 0)
        );

        seq.schedule();
    }
}
