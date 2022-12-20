package fundamentals.animation;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import fundamentals.Constants;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;

/**
 *  Used to return Image, ImageIcon, and BufferedImage instances along with the dimensions of an image. 
 * 
 * @see
 * Note: Can only utilize PNG and GIF file types. 
 */
public class Animation 
{
    private String file_name = "";

    /**
     * Used to return Image, ImageIcon, and BufferedImage instances along with the dimensions of an image. 
     * 
     * @param file_name
     * - The name of the image file. The String must include the file type.        
     *  EX 1: "myImage.png" EX 2: "myAnimation.gif"
     * 
     * @see
     * Note: Can utilize PNG and GIF file types. 
     */
    public Animation(String file_name)
    {
        this.file_name = file_name;
    }

    /**
     * @return A Image instance created from the image file specified in the constructor. 
     */
    public Image getAnimation()
    {
       return new ImageIcon(getClass().getResource("/" + Constants.FILE_ROOT_DIRECTORIES.IMAGE_ROOT_DIRECTORY + file_name)).getImage();
    }

    /**
     * @return A ImageIcon instance created from the image file specified in the constructor. 
     */
    private ImageIcon getImageIcon()
    {    
        return new ImageIcon(Constants.FILE_ROOT_DIRECTORIES.IMAGE_ROOT_DIRECTORY + file_name);
    }

    /**
     * @return A BufferedImage instance created from the image file specified in the constructor. 
     */
    private BufferedImage getBufferedImage()
    {
        try
        {
            return ImageIO.read(new File(Constants.FILE_ROOT_DIRECTORIES.IMAGE_ROOT_DIRECTORY + file_name));
        }
        catch(IOException e) 
        {
            System.err.println("Animation.java: IOExeception caught!");
            return null;
        }
    }

    /**
     * @return The width of the image specified in the constructor. 
     * 
     * @see
     * Note: Unit of measurement: Pixels.
     */
    public int getImageWidth()
    {
        if(file_name.contains(".png"))
        {
            return getBufferedImage().getWidth();  
        }
        else if(file_name.contains(".gif"))
        {
            return getImageIcon().getIconWidth();
        }

        return 0;
    }

    /**
     * @return The height of the image specified in the constructor. 
     * 
     * @see
     * Note: Unit of measurement: Pixels.
     */
    public int getImageHeight()
    {
        if(file_name.contains(".png"))
        {
            return getBufferedImage().getHeight();  
        }
        else if(file_name.contains(".gif"))
        {
            return getImageIcon().getIconHeight();
        }

        return 0;
    }

    /**
     * @return The image file's name; file type included. EX: "myImage.png"
     */
    public String getName()
    {
        return file_name;
    }
}