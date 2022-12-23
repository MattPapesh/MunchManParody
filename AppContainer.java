import fundamentals.appbase.AppBase;
import mechanics.PlayerMovement;
import fundamentals.Constants;
import fundamentals.UI.*;

import components.MunchMan;
import components.Stage;

public class AppContainer extends AppBase
{
    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private MunchMan munch_man = new MunchMan();

    private void configureButtonBindings() {
        controller.whenLeftPressed(new PlayerMovement(stage, munch_man, -1, 0));
        controller.whenRightPressed(new PlayerMovement(stage, munch_man, 1, 0));
        controller.whenUpPressed(new PlayerMovement(stage, munch_man, 0, -1));
        controller.whenDownPressed(new PlayerMovement(stage, munch_man, 0, 1));
    }

    public AppContainer() {
        configureButtonBindings();
    }
}
