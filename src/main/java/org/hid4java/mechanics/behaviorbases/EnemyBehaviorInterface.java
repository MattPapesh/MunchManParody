package org.hid4java.mechanics.behaviorbases;

public interface EnemyBehaviorInterface 
{
    public void initializeBehavior();
    public void executeBehavior();
    public void endBehavior(boolean interrupted);
    public boolean isBehaviorFinished();    
}
