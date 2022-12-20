import fundamentals.appbase.AppBase;

public class App extends AppBase
{
    private AppContainer app_container = new AppContainer(); 

    // Use the status initiation methods here to transition between the status methods below
    @Override // This method is always periodically called, regardless of the app's current status
    public void determineAppStatus() 
    {   
        if(app_container.getAppStatus())
        {
            super.initiateAppStatus();
            return;
        }
    }

    @Override 
    public void appInit() 
    {
        
    }

    @Override 
    public void appPeriodic() 
    { 
        
    }
}
