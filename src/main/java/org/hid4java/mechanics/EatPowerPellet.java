package org.hid4java.mechanics;

import org.hid4java.app.audio.AppAudio;
import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.PowerPellet;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class EatPowerPellet extends MechanicBase
{
    private MunchMan munch_man = null; 
    private PowerPellet pellet = null; 
    private Enemy other_0 = null, other_1 = null, other_2 = null, other_3 = null;
    private long eaten_millis = 0;
    private int power_delay_millis = 0;
    private boolean is_reset = true; 

    public EatPowerPellet(MunchMan munch_man, PowerPellet pellet, 
    Enemy other_0, Enemy other_1, Enemy other_2, Enemy other_3, int power_delay_millis) 
    {
        addRequirements(munch_man, pellet);
        this.munch_man = munch_man;
        this.pellet = pellet;
        this.other_0 = other_0; 
        this.other_1 = other_1;
        this.other_2 = other_2;
        this.other_3 = other_3;
        this.power_delay_millis = power_delay_millis;
    }

    public void reset() 
    {
        is_reset = true;
        pellet.reset();
    }

    @Override
    public void execute() 
    {
        pellet.run();
        if(!pellet.getEaten() && GameMath.isCoordsEqual(munch_man.getStageCoords(), pellet.getStageCoords())) {
            Constants.score += Constants.POWER_PELLET_PTS;
            eaten_millis = System.currentTimeMillis();

            AppAudio.stopAudioFile("Default.wav");
            AppAudio.playAudioFile("Weakened.wav");
            other_0.enableWeakenedState(true);
            other_1.enableWeakenedState(true);
            other_2.enableWeakenedState(true);
            other_3.enableWeakenedState(true);
            pellet.eat();
        }
        else if(pellet.getEaten() && is_reset && System.currentTimeMillis() - eaten_millis >= power_delay_millis) {
            is_reset = false;
            AppAudio.playAudioFileLoopContinuously("Default.wav");
            AppAudio.stopAudioFile("Weakened.wav");
            other_0.enableWeakenedState(false);
            other_1.enableWeakenedState(false);
            other_2.enableWeakenedState(false);
            other_3.enableWeakenedState(false);
        }
    }
}
