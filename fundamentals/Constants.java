package fundamentals;

import java.awt.Toolkit;

/**
 * All global constants defined in specific sub-classes that were statically declared and defined. 
 */
public class Constants 
{       
    public static final class CONTROLLER_IDS
    {
        public static final int LEFT_KEY = 65;
        public static final int RIGHT_KEY = 68;
        public static final int UP_KEY = 87;
        public static final int DOWN_KEY = 83;
    };

    /*
     * All root directories for any assets used. 
     */
    public static final class FILE_ROOT_DIRECTORIES
    {
        public static final String IMAGE_ROOT_DIRECTORY = "assets/images/";
        public static final String AUDIO_ROOT_DIRECTORY = "assets/audio/";
    }

    /*
     * Window characteristics for the window that the application displays on-screen.
     */
    public static final class WINDOW_CHARACTERISTICS
    {
        public static final String[] APP_ICON_IMAGES = {"icon.png", "icon.png"};
        public static final String APP_TITLE = "Munch Man";
        public static final int WINDOW_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        public static final int WINDOW_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        public static final int REFRESH_RATE_MILLIS = 0;
        public static final double GRAPHICS_X_SCALER_COEFF = 1.22;
        public static final double GRAPHICS_Y_SCALER_COEFF = 0.85;
        public static final int GRAPHICS_X_TRANSLATION = 0;
        public static final int GRAPHICS_Y_TRANSLATION = 0;
    }

    public static final class STAGE_CHARACTERISTICS
    {
        public static final int STAGE_COORD_SCALER = 27;
        public static final Coordinates COORD_DISPLACEMENT = new Coordinates(-50, 0, 0);
        public static final class DEGREE_DIRECTIONS 
        {
            // Measured in degrees: 
            public static final int LEFT = 180;
            public static final int RIGHT = 0;
            public static final int UP = 90;
            public static final int DOWN = 270;
        }

        public static final Coordinates[] TOP_LEFT_STAGE_TUNNEL_REGION = 
        {new Coordinates(0, 9, 0), new Coordinates(5, 9, 0)};
        public static final Coordinates[] TOP_RIGHT_STAGE_TUNNEL_REGION = 
        {new Coordinates(41, 9, 0), new Coordinates(46, 9, 0)};
        public static final Coordinates[] BOTTOM_LEFT_STAGE_TUNNEL_REGION = 
        {new Coordinates(0, 30, 0), new Coordinates(5, 27, 0)};
        public static final Coordinates[] BOTTOM_RIGHT_STAGE_TUNNEL_REGION = 
        {new Coordinates(41, 27, 0), new Coordinates(46, 30, 0)};

        public static final Coordinates[][] STAGE_TUNNEL_REGIONS = 
        {
            TOP_LEFT_STAGE_TUNNEL_REGION, TOP_RIGHT_STAGE_TUNNEL_REGION,
            BOTTOM_LEFT_STAGE_TUNNEL_REGION, BOTTOM_RIGHT_STAGE_TUNNEL_REGION
        };

        public static final int[][] STAGE_DATA = 
        {   // Tunnel regions: [9][0]->[9][5], [9][41]->[9][46], [30][0]->[27][5], [27][41]->[30][46]
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
            {0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,1,1,1,0,0,0,0,0,0,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    }
}
