package mechanics;

public interface EnemyBehaviorInterface 
{
    public void initializeBehavior();
    public void executeBehavior();
    public void endBehavior(boolean interrupted);
    public boolean isBehaviorFinished();    
}
