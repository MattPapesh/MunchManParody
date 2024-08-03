package org.hid4java.fundamentals;

import java.awt.Toolkit;

import org.hid4java.fundamentals.animation.Animation;

/**
 * All global constants defined in specific sub-classes that were statically declared and defined. 
 */
public class Constants 
{       
    public static final class CONTROLLER_IDS
    {
        public static final int LEFT_KEY = 83;//65; S
        public static final int RIGHT_KEY = 68; //D
        public static final int UP_KEY = 69;//87; E
        public static final int DOWN_KEY = 88;//83; X
    };

    /*
     * All root directories for any assets used. 
     */
    public static final class FILE_ROOT_DIRECTORIES
    {
        public static final String IMAGE_ROOT_DIRECTORY = "src/main/java/org/hid4java/assets/images/";
        public static final String AUDIO_ROOT_DIRECTORY = "src/main/java/org/hid4java/assets/audio/";
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
        public static final long REFRESH_RATE_NANOS = 1000;
        public static final double GRAPHICS_X_SCALER_COEFF = (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1302.0) * 1.03;
        public static final double GRAPHICS_Y_SCALER_COEFF = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 975.0) * 0.975;
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

        public static final Coordinates[] SPAWN_REGION = 
        {new Coordinates(18, 15, 0), new Coordinates(28, 15, 0)};

        public static final Coordinates RED_SPAWN_STAGE_COORD = new Coordinates(14, 12, 0);
        public static final Coordinates PINK_SPAWN_STAGE_COORD = new Coordinates(32, 12, 180);
        public static final Coordinates YELLOW_SPAWN_STAGE_COORD = new Coordinates(14, 18, 0);
        public static final Coordinates BLUE_SPAWN_STAGE_COORD = new Coordinates(32, 18, 180); 

        public static final Coordinates RED_START_STAGE_COORD = new Coordinates(17, 12, 90);
        public static final Coordinates PINK_START_STAGE_COORD = new Coordinates(29, 12, 90);
        public static final Coordinates YELLOW_START_STAGE_COORD = new Coordinates(17, 18, 270);
        public static final Coordinates BLUE_START_STAGE_COORD = new Coordinates(29, 18, 270);

        public static final Coordinates RED_HOME_STAGE_COORD = new Coordinates(2, 1, 0);
        public static final Coordinates PINK_HOME_STAGE_COORD = new Coordinates(44, 1, 0);
        public static final Coordinates YELLOW_HOME_STAGE_COORD = new Coordinates(5, 27, 0);
        public static final Coordinates BLUE_HOME_STAGE_COORD = new Coordinates(41, 27, 0);

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
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,0,0,1,0,0,0,0,0,1,0,0,0,0,0},//14,12 -- 32,12
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0,0,1,0,0,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,0,0,1,0,0,0,0,0,1,0,0,0,0,0},//14,18 -- 32,18
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

    public static final float[] ENEMY_HUE = 
    {
        // Red      Pink         Blue         Orange
        (float)0.93, (float)0.67, (float)0.43, (float)0.0
    };

    public static final Animation[] BLAZE = 
    {
        
        new Animation("blaze/blaze_0.png"), new Animation("blaze/blaze_1.png"), new Animation("blaze/blaze_2.png"),
        new Animation("blaze/blaze_3.png"), new Animation("blaze/blaze_4.png"), new Animation("blaze/blaze_5.png"),
        new Animation("blaze/blaze_6.png"), new Animation("blaze/blaze_7.png"), new Animation("blaze/blaze_8.png"),
        new Animation("blaze/blaze_9.png"), new Animation("blaze/blaze_10.png"), new Animation("blaze/blaze_11.png"),
        new Animation("blaze/blaze_12.png"), new Animation("blaze/blaze_13.png"), new Animation("blaze/blaze_14.png")
        
    };

    public static final Animation[] SUIT = 
    {
        new Animation("suit/suit_0.png"), new Animation("suit/suit_1.png"), new Animation("suit/suit_2.png"), 
        new Animation("suit/suit_3.png"), new Animation("suit/suit_4.png"), new Animation("suit/suit_5.png")
    };

    public static final Animation[] SPIKE = 
    {
        new Animation("spike/spike_0.png"), new Animation("spike/spike_0.png"), new Animation("spike/spike_1.png"), 
        new Animation("spike/spike_1.png"), new Animation("spike/spike_2.png"), new Animation("spike/spike_2.png"), 
        new Animation("spike/spike_3.png"), new Animation("spike/spike_3.png"), new Animation("spike/spike_4.png"), 
        new Animation("spike/spike_4.png"), new Animation("spike/spike_5.png"), new Animation("spike/spike_5.png"),
        new Animation("spike/spike_6.png"), new Animation("spike/spike_6.png"), new Animation("spike/spike_7.png"), 
        new Animation("spike/spike_7.png"), new Animation("spike/spike_8.png"), new Animation("spike/spike_8.png"), 
        new Animation("spike/spike_9.png"), new Animation("spike/spike_9.png"), new Animation("spike/spike_10.png"), 
        new Animation("spike/spike_10.png"), new Animation("spike/spike_11.png"), new Animation("spike/spike_11.png")
    };

    public static final Animation[] MUNCHER = 
    {
        new Animation("muncher/muncher_0.png"), new Animation("muncher/muncher_1.png"), new Animation("muncher/muncher_2.png"), 
        new Animation("muncher/muncher_3.png"), new Animation("muncher/muncher_4.png"), new Animation("muncher/muncher_5.png"), 
        new Animation("muncher/muncher_6.png"), new Animation("muncher/muncher_7.png"), new Animation("muncher/muncher_8.png"), 
    };

    public static final Animation[][] ENEMY = 
    {
        BLAZE, SUIT, SPIKE, MUNCHER
    };
}
