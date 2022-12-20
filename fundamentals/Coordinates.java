package fundamentals;

/**
 * A data structure that houses and describes the location of a component in the window of the application while on-screen. 
 * 
 * @see
 * Note: Coordinates are relative to the point of origin located at the upper-left cornder of an application's window, where going
 * across the screen, from left to right, is the positive X-direction, and going up to down the screen is positive for the Y-direction.
 * 
 * @see
 * Note: Degrees positively increase for clockwise rotations, and negatively for counter-clockwise rotations. 
 * 
 * @see
 * Note: Degrees are relative to the X-axis.
 */
public class Coordinates 
{
    // Right is positive (X) while left is negative. Down is positve (Y) while up is negative. 
    private int x = 0;
    private int y = 0;
    // Clockwise is positive and counter-clockwise is negative degrees
    private int degrees = 0;

    /**
    * A data structure that houses and describes the location of a component in the window of the application while on-screen. 
    * 
    * @see
    * Note: Coordinates are relative to the point of origin located at the upper-left cornder of an application's window, where going
    * across the screen, from left to right, is the positive X-direction, and going up to down the screen is positive for the Y-direction.
    * 
    * @see
    * Note: Degrees positively increase for clockwise rotations, and negatively for counter-clockwise rotations. 
    */
    public Coordinates(int x, int y, int degrees)
    {
        this.x = x;
        this.y = y;
        this.degrees = degrees;
    }

    /**
     * Used to set new coordinates.
     */
    public void setCoordinates(int x, int y, int degrees)
    {
        this.x = x;
        this.y = y;
        this.degrees = degrees;
    }
    
    /**
     * @return The X coordinate. 
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return The Y coordinate. 
     */
    public int getY()
    {
        return y;
    }

    /**
     * @return The degrees relative to the X-axis. 
     */
    public int getDegrees()
    {
        return degrees;
    }
}