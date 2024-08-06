package org.hid4java.mechanics;

import org.hid4java.app.audio.AppAudio;
import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.MunchManLives;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.mechanic.MechanicBase;
import org.hid4java.mechanics.behavior.LevelBehavior;

public class KillMunchMan extends MechanicBase
{
    private MunchMan munch_man = null;
    private MunchManLives lives = null;
    private Enemy other_0 = null, other_1 = null, other_2 = null, other_3 = null; 
    private LevelBehavior level_0 = null, level_1 = null, level_2 = null, level_3 = null;
    private boolean init = false;

    public KillMunchMan(MunchMan munch_man, MunchManLives lives, Enemy other_0, 
    Enemy other_1, Enemy other_2, Enemy other_3, LevelBehavior level_0, 
    LevelBehavior level_1, LevelBehavior level_2, LevelBehavior level_3) 
    {
        this.level_0 = level_0;
        this.level_1 = level_1;
        this.level_2 = level_2;
        this.level_3 = level_3;

        this.munch_man = munch_man;
        this.lives = lives; 
        this.other_0 = other_0;
        this.other_1 = other_1;
        this.other_2 = other_2;
        this.other_3 = other_3;
        addRequirements(munch_man, lives, other_0, other_1, other_2, other_3);
    }    

    public void reset() 
    {
        init = false;
    }

    @Override
    public void execute() 
    {
        if(!init) {
            init = true; 
            AppAudio.stopAllAudioFiles();
            AppAudio.playAudioFile("Start.wav");
            long millis = System.currentTimeMillis();
            while(System.currentTimeMillis() - millis <= 3000) {}
            AppAudio.playAudioFileLoopContinuously("Default.wav");
            
        }

        if(munch_man.isKilled()) {    
            AppAudio.stopAllAudioFiles();
            AppAudio.playAudioFile("Death.wav");
            long millis = System.currentTimeMillis();
            while(System.currentTimeMillis() - millis <= 3000) {}
            
            other_0.reset();
            other_1.reset();
            other_2.reset();
            other_3.reset();
            munch_man.reset();

            level_0.reset();
            level_1.reset();
            level_2.reset();
            level_3.reset();

            lives.setNextAnimation();
            init = false; 
            Constants.lives--;
        }
    }
}
