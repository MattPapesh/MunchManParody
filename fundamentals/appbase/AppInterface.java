package fundamentals.appbase;

/**
 * AppInterface is the interface for AppBase. AppInterface houses initializer methods that are called once, 
 * periodic methods that are continuously called, and a method for determining the application's current status.
 * Finally, the initializer and periodic method pairs are called based on the applications current status, and all 
 * methods must be overridden in AppBase's sublcass, App.java. 
 */
public interface AppInterface 
{
    /**
     * Meant to be overriden with the intent to be used to determine the application's current status. Moreover,
     * it's recommended that the initiate status methods and enable status methods are used in this method's overridden definition, given
     * that this method is continuously called, regardless of the application's current status. 
     */
    public void determineAppStatus();

    /**
     * Immediately called once after the "menu status" has become the application's current status. 
     */
    public void appInit();

    /**
     * Continuously called after the "menu status" has become the application's current status. 
     */
    public void appPeriodic();
}
