import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import mechanics.EntityMovement;
import mechanics.PlaceStageChain;
import mechanics.Mech;
import fundamentals.Constants;
import fundamentals.UI.*;
import components.EnemyBase;
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
    private EnemyBase enemy = new EnemyBase();

    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);

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
    }
}
