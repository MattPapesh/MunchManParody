import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;
import mechanics.PlayerMovement;
import fundamentals.Constants;
import fundamentals.UI.*;

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

    private PlayerMovement player_movement = new PlayerMovement(stage, munch_man);

    private void configureButtonBindings() {
        controller.whenLeftPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(-3, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(3, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, -3); }));
        controller.whenDownPressed(new InstantMechanic(()->{ player_movement.setTickVelocity(0, 3); }));
    }

    public AppContainer() {
        configureButtonBindings();
        player_movement.schedule();
    }
}
