package org.hid4java;

import org.hid4java.fundamentals.appbase.AppBase;
import org.hid4java.fundamentals.mechanic.InstantMechanic;
import org.hid4java.mechanics.EatPowerPellet;
import org.hid4java.mechanics.KillMunchMan;
import org.hid4java.mechanics.PuppetMunchMan;
import org.hid4java.mechanics.behavior.EnemyBlueBehavior;
import org.hid4java.mechanics.behavior.EnemyPinkBehavior;
import org.hid4java.mechanics.behavior.EnemyRedBehavior;
import org.hid4java.mechanics.behavior.EnemyYellowBehavior;
import org.hid4java.mechanics.behavior.LevelBehavior;
import org.hid4java.mechanics.movement.EntityMovement;
import org.hid4java.mechanics.stage.PlaceStageChain;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.UI.*;
import org.hid4java.app.audio.AppAudio;
import org.hid4java.app.input.AppInput;
import org.hid4java.app.input.NESControllerInput.Button;
import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.MunchManLives;
import org.hid4java.components.PowerPellet;
import org.hid4java.components.Stage;
import org.hid4java.components.StageChain;

public class AppContainer extends AppBase
{
    private final double PLAYER_DEF_SPEED = 0.25;
    private final double ENEMY_DEF_SPEED = 0.25;

    private AppInput app_input = null; 
    private NESController nes_controller = getNESController();
    private Controller controller = getController(Constants.CONTROLLER_IDS.LEFT_KEY, Constants.CONTROLLER_IDS.RIGHT_KEY, 
    Constants.CONTROLLER_IDS.UP_KEY, Constants.CONTROLLER_IDS.DOWN_KEY);

    private Stage stage = new Stage();
    private StageChain stage_chain = new StageChain();
    private MunchManLives lives = new MunchManLives();

    private PowerPellet A = new PowerPellet(2, 3, 0.005);
    private PowerPellet B = new PowerPellet(44, 3, 0.005);
    private PowerPellet C = new PowerPellet(2, 22, 0.005);
    private PowerPellet D = new PowerPellet(44, 22, 0.005);
    
    private MunchMan munch_man = new MunchMan(Constants.STAGE_CHARACTERISTICS.MUNCHMAN_SPAWN.getX(), 
    Constants.STAGE_CHARACTERISTICS.MUNCHMAN_SPAWN.getY(), Constants.STAGE_CHARACTERISTICS.MUNCHMAN_SPAWN.getDegrees());
    private MunchMan left_puppet_munch_man = new MunchMan(-1, -1, 0);
    private MunchMan right_puppet_munch_man = new MunchMan(-1, -1, 0);
    
    private Enemy enemy_red = new Enemy(Constants.STAGE_CHARACTERISTICS.RED_SPAWN_STAGE_COORD.getX(), Constants.STAGE_CHARACTERISTICS.RED_SPAWN_STAGE_COORD.getY(), 
    Constants.STAGE_CHARACTERISTICS.RED_SPAWN_STAGE_COORD.getDegrees(), ENEMY_DEF_SPEED, Constants.ENEMY_HUE[0], Constants.ENEMY_HUE[4], Constants.WEAKENED_ENEMY_SATURATION);

    private Enemy enemy_yellow = new Enemy(Constants.STAGE_CHARACTERISTICS.YELLOW_SPAWN_STAGE_COORD.getX(), Constants.STAGE_CHARACTERISTICS.YELLOW_SPAWN_STAGE_COORD.getY(), 
    Constants.STAGE_CHARACTERISTICS.YELLOW_SPAWN_STAGE_COORD.getDegrees(), ENEMY_DEF_SPEED, Constants.ENEMY_HUE[3], Constants.ENEMY_HUE[4], Constants.WEAKENED_ENEMY_SATURATION);

    private Enemy enemy_blue = new Enemy(Constants.STAGE_CHARACTERISTICS.BLUE_SPAWN_STAGE_COORD.getX(), Constants.STAGE_CHARACTERISTICS.BLUE_SPAWN_STAGE_COORD.getY(), 
    Constants.STAGE_CHARACTERISTICS.BLUE_SPAWN_STAGE_COORD.getDegrees(), ENEMY_DEF_SPEED, Constants.ENEMY_HUE[2], Constants.ENEMY_HUE[4], Constants.WEAKENED_ENEMY_SATURATION);

    private Enemy enemy_pink = new Enemy(Constants.STAGE_CHARACTERISTICS.PINK_SPAWN_STAGE_COORD.getX(), Constants.STAGE_CHARACTERISTICS.PINK_SPAWN_STAGE_COORD.getY(), 
    Constants.STAGE_CHARACTERISTICS.PINK_SPAWN_STAGE_COORD.getDegrees(), ENEMY_DEF_SPEED, Constants.ENEMY_HUE[1], Constants.ENEMY_HUE[4], Constants.WEAKENED_ENEMY_SATURATION);

    private PlaceStageChain place_stage_chain = new PlaceStageChain(munch_man, stage, stage_chain);
    private PuppetMunchMan puppet_munch_man = new PuppetMunchMan(munch_man, left_puppet_munch_man, right_puppet_munch_man);

    private EntityMovement player_movement = new EntityMovement(stage, munch_man);
    private EntityMovement enemy_red_movement = new EntityMovement(stage, enemy_red);
    private EntityMovement enemy_yellow_movement = new EntityMovement(stage, enemy_yellow);
    private EntityMovement enemy_blue_movement = new EntityMovement(stage, enemy_blue);
    private EntityMovement enemy_pink_movement = new EntityMovement(stage, enemy_pink);

    private EnemyRedBehavior enemy_red_behavior = new EnemyRedBehavior(enemy_red_movement, stage, enemy_red, munch_man, A, B, C, D);
    private EnemyYellowBehavior enemy_yellow_behavior = new EnemyYellowBehavior(enemy_yellow_movement, stage, enemy_yellow, munch_man, A, B, C, D);
    private EnemyBlueBehavior enemy_blue_behavior = new EnemyBlueBehavior(enemy_blue_movement, stage, enemy_blue, enemy_red, munch_man, A, B, C, D);
    private EnemyPinkBehavior enemy_pink_behavior = new EnemyPinkBehavior(enemy_pink_movement, stage, enemy_pink, enemy_red, enemy_yellow, enemy_blue, munch_man, A, B, C, D);

    private LevelBehavior red_level = new LevelBehavior(enemy_red_movement, stage, enemy_red, munch_man, Constants.STAGE_CHARACTERISTICS.RED_SPAWN_STAGE_COORD, 
    Constants.STAGE_CHARACTERISTICS.RED_START_STAGE_COORD, Constants.STAGE_CHARACTERISTICS.RED_HOME_STAGE_COORD, enemy_red_behavior);
    private LevelBehavior yellow_level = new LevelBehavior(enemy_yellow_movement, stage, enemy_yellow, munch_man, Constants.STAGE_CHARACTERISTICS.YELLOW_SPAWN_STAGE_COORD, 
    Constants.STAGE_CHARACTERISTICS.YELLOW_START_STAGE_COORD, Constants.STAGE_CHARACTERISTICS.YELLOW_HOME_STAGE_COORD, enemy_yellow_behavior);
    private LevelBehavior blue_level = new LevelBehavior(enemy_blue_movement, stage, enemy_blue, munch_man, Constants.STAGE_CHARACTERISTICS.BLUE_SPAWN_STAGE_COORD, 
    Constants.STAGE_CHARACTERISTICS.BLUE_START_STAGE_COORD, Constants.STAGE_CHARACTERISTICS.BLUE_HOME_STAGE_COORD, enemy_blue_behavior);
    private LevelBehavior pink_level = new LevelBehavior(enemy_pink_movement, stage, enemy_pink, munch_man, Constants.STAGE_CHARACTERISTICS.PINK_SPAWN_STAGE_COORD, 
    Constants.STAGE_CHARACTERISTICS.PINK_START_STAGE_COORD, Constants.STAGE_CHARACTERISTICS.PINK_HOME_STAGE_COORD, enemy_pink_behavior);

    private EatPowerPellet eat_pellot_A = new EatPowerPellet(munch_man, A, enemy_red, enemy_yellow, enemy_blue, enemy_pink, Constants.POWER_PELLOT_DURATION_MILLIS);
    private EatPowerPellet eat_pellot_B = new EatPowerPellet(munch_man, B, enemy_red, enemy_yellow, enemy_blue, enemy_pink, Constants.POWER_PELLOT_DURATION_MILLIS);
    private EatPowerPellet eat_pellot_C = new EatPowerPellet(munch_man, C, enemy_red, enemy_yellow, enemy_blue, enemy_pink, Constants.POWER_PELLOT_DURATION_MILLIS);
    private EatPowerPellet eat_pellot_D = new EatPowerPellet(munch_man, D, enemy_red, enemy_yellow, enemy_blue, enemy_pink, Constants.POWER_PELLOT_DURATION_MILLIS);

    private KillMunchMan kill_munch_man = new KillMunchMan(munch_man, lives, enemy_yellow, enemy_red, enemy_pink, enemy_blue, yellow_level, red_level, pink_level, blue_level);

    private void configureButtonBindings() 
    {    
        // NES Controller Button Bindings:
        nes_controller.whenPressed(Button.LEFT, new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(-PLAYER_DEF_SPEED, 0); }));
        nes_controller.whenPressed(Button.RIGHT, new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(PLAYER_DEF_SPEED, 0); }));
        nes_controller.whenPressed(Button.UP, new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(0, -PLAYER_DEF_SPEED); }));
        nes_controller.whenPressed(Button.DOWN, new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(0, PLAYER_DEF_SPEED); }));
        // Keyboard Controller Button Bindings:
        controller.whenLeftPressed(new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(-PLAYER_DEF_SPEED, 0); }));
        controller.whenRightPressed(new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(PLAYER_DEF_SPEED, 0); }));
        controller.whenUpPressed(new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(0, -PLAYER_DEF_SPEED); }));
        controller.whenDownPressed(new InstantMechanic(()->{ if(Constants.game_start && !munch_man.isKilled()) player_movement.setTickVelocity(0, PLAYER_DEF_SPEED); }));
    }

    public AppContainer(AppInput app_input) 
    {
        this.app_input = app_input;
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

        enemy_red_movement.enableSpawnPointAccess(true);
        enemy_yellow_movement.enableSpawnPointAccess(true);
        enemy_blue_movement.enableSpawnPointAccess(true);
        enemy_pink_movement.enableSpawnPointAccess(true);

        enemy_red_movement.schedule();
        enemy_yellow_movement.schedule();
        enemy_blue_movement.schedule();
        enemy_pink_movement.schedule();
   
        munch_man.appendEntityMovement(player_movement);
        enemy_red.appendEntityMovement(enemy_red_movement);
        enemy_yellow.appendEntityMovement(enemy_yellow_movement);
        enemy_blue.appendEntityMovement(enemy_blue_movement);
        enemy_pink.appendEntityMovement(enemy_pink_movement);

        red_level.schedule();
        yellow_level.schedule();
        blue_level.schedule();
        pink_level.schedule();
 
        eat_pellot_A.schedule();
        eat_pellot_B.schedule();
        eat_pellot_C.schedule();
        eat_pellot_D.schedule();

        kill_munch_man.schedule();
        AppAudio.playAudioFileLoopContinuously("Menu.wav");
    }

    public void periodic() 
    {
        boolean up = app_input.isKeyPressed(Constants.CONTROLLER_IDS.UP_KEY);
        boolean down = app_input.isKeyPressed(Constants.CONTROLLER_IDS.DOWN_KEY);
        boolean left = app_input.isKeyPressed(Constants.CONTROLLER_IDS.LEFT_KEY);
        boolean right = app_input.isKeyPressed(Constants.CONTROLLER_IDS.RIGHT_KEY);

        if(!Constants.game_start && up) {
            Constants.select_start = true;
            AppAudio.playAudioFile("Select.wav");
            try {Thread.sleep(500);} catch(InterruptedException e) {}
        }
        else if(!Constants.game_start && down) {
            Constants.select_start = false; 
            AppAudio.playAudioFile("Select.wav");
            try {Thread.sleep(500);} catch(InterruptedException e) {}
        }

        if(!Constants.game_start && Constants.select_start && (left || right)) {
            Constants.game_start = true; 
            AppAudio.playAudioFile("Confirm.wav");
            try {Thread.sleep(500);} catch(InterruptedException e) {}
        }
        else if(!Constants.select_start && (left || right)) {
            System.exit(0);  
        }

        if(!Constants.game_start) {
            return;
        }

        if(stage_chain.isAllChainPlaced() || Constants.lives <= 0) {
            if(Constants.lives > 0) { 
                AppAudio.stopAllAudioFiles();
                AppAudio.playAudioFile("Win.wav");
                long millis = System.currentTimeMillis();
                while(System.currentTimeMillis() - millis <= 3000) {}

                Constants.level++;
                stage.setNextAnimation();
                kill_munch_man.reset();
                A.reset();
                B.reset();
                C.reset();
                D.reset();
            }
            else {
                AppAudio.stopAllAudioFiles();
                AppAudio.playAudioFile("GameOver.wav");
                long millis = System.currentTimeMillis();
                while(System.currentTimeMillis() - millis <= 4000) {}
                AppAudio.playAudioFileLoopContinuously("Menu.wav");

                Constants.high_score = (Constants.score > Constants.high_score) ? Constants.score : Constants.high_score;
                Constants.score = 0;
                Constants.level = 1;
                Constants.lives = 3;
                Constants.game_start = false;
                stage.setAnimation(stage.getAnimation(0).getName());
                kill_munch_man.reset();
                A.fullReset();
                B.fullReset();
                C.fullReset();
                D.fullReset();
            }

            stage_chain.reset();
            red_level.nextLevel(Constants.level);
            yellow_level.nextLevel(Constants.level);
            blue_level.nextLevel(Constants.level);
            pink_level.nextLevel(Constants.level);
            munch_man.reset();
        }
    }
}
