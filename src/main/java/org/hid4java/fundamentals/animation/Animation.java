package org.hid4java.fundamentals.animation;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.hid4java.fundamentals.Constants;

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
    private final String file_name;
    private BufferedImage image = null;
    private Image other_image = null;
    private BufferedImage parent_image = null;

    private int img_x0 = 0; 
    private int img_y0 = 0;
    private int img_x1 = 0;
    private int img_y1 = 0;

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
        image = getBufferedImage();
    }

    public Animation(int img_x0, int img_y0, int img_x1, int img_y1, Animation parent_animation)
    {
        this.file_name = "";
        this.img_x0 = img_x0;
        this.img_y0 = img_y0;
        this.img_x1 = img_x1;
        this.img_y1 = img_y1;
        parent_image = parent_animation.getBufferedImage();
        image = parent_image.getSubimage(img_x0, img_y0, Math.abs(img_x1 - img_x0), Math.abs(img_y1 - img_y0));
    }

    public Animation(BufferedImage image) 
    {
        this.file_name = "";
        this.image = image; 
    }

    public Animation(Image image) 
    {
        this.file_name = "";
        this.other_image = image; 
    }

    public Image getAnimation()
    {
        return (image != null) ? image : other_image; 
    }

    public Image getHuedAnimation(double hue_offset) 
    {
        BufferedImage image = getBufferedImage();
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);
                Color color = new Color(rgba, true);

                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                hsb[0] += (float)hue_offset;

                int final_rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                int final_alpha = (rgba >> 24) & 0xff;
                int final_rgba = (final_alpha << 24) | (final_rgb & 0x00ffffff);

                image.setRGB(x, y, final_rgba);
            }
        }

        return image;
    }

    /**
     * @return A ImageIcon instance created from the image file specified in the constructor. 
     * 
     * @see Note: This only applies for Animations created by calling Animation(String file_name); 
     * Animations created by providing a parent Animation via the alternative constructor will be considered Animations
     * of sub images. Sub image Animations are based on their parent Animation and this method cannot apply. 
     */
    private ImageIcon getImageIcon()
    {    
        return new ImageIcon(Constants.FILE_ROOT_DIRECTORIES.IMAGE_ROOT_DIRECTORY + file_name);
    }

    /**
     * @return A BufferedImage instance created from the image file specified in the constructor. 
     * 
     * @see Note: This only applies for Animations created by calling Animation(String file_name); 
     * Animations created by providing a parent Animation via the alternative constructor will be considered Animations
     * of sub images. Sub image Animations are based on their parent Animation and this method cannot apply.
     */
    protected BufferedImage getBufferedImage()
    {
        try
        {
            return ImageIO.read(new File(Constants.FILE_ROOT_DIRECTORIES.IMAGE_ROOT_DIRECTORY + file_name));
        }
        catch(IOException e) 
        {
            //System.err.println("Animation.java: IOExeception caught!");
            return null;
        }
    }

    /** 
     * Sets the RGB color values for a given section of the Animation/image. (img_x0, img_y0) is the initial coord, (img_x1, img_y1)
     * is the final coord, where the section of the Animation that is changed is whatever is captured by the rectangular bounds made
     * by the initial/final coords; intial coord comes first at the top left coord, and the final coord comes second as the bottom left
     * coord of the bounding rectangle. 
     * 
     * @note Note: Setting RGB values will override the initial Animtation/image's color(s), or RGB values for the given section of 
     * the Animation; these changes can be removed by resetting the Animation by calling the reset() method. 
     */
    public void setRGB(int img_x0, int img_y0, int img_x1, int img_y1, int red_val, int green_val, int blue_val, int alpha)
    {
        int color = new Color(red_val, green_val, blue_val, alpha).getRGB();
        for(int y = img_y0; y < img_y1; y++)
        {
            for(int x = img_x0; x < img_x1; x++)
            {
                image.setRGB(x, y, color);
            }
        }
    }

    public void setAlpha(int img_x0, int img_y0, int img_x1, int img_y1, int alpha)
    {
        BufferedImage image = this.image;

        for(int y = img_y0; y < img_y1; y++)
        {
            for(int x = img_x0; x < img_x1; x++)
            {
                try
                {
                    Color color = new Color(image.getRGB(x, y)); 
                    this.image.setRGB(x, y, (new Color(color.getRed(), color.getBlue(), color.getGreen(), alpha)).getRGB());
                }
                catch(ArrayIndexOutOfBoundsException e) {}
            }
        }
    }

    /**
     * Resets the Animation/image to its original state; if changes were made to the Animation by calling methods such as, 
     * setRGB(...), these changes will be removed from the Animation. 
     */
    public void reset()
    {
        if(file_name != "")
        {
            image = getBufferedImage();
        }
        else
        {
            image = parent_image.getSubimage(img_x0, img_y0, Math.abs(img_x1 - img_x0), Math.abs(img_y1 - img_y0));
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
        else if(image != null) 
        {
            return image.getWidth();
        }
        else if(other_image != null) 
        {
            return other_image.getWidth(null);
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
        else if(image != null) 
        {
            return image.getHeight();
        }
        else if(other_image != null) 
        {
            return other_image.getHeight(null);
        }
        
        return 0;
    }

    /**
     * @return The image file's name; file type included. EX: "myImage.png"
     * 
     * @see Note: This only applies for Animations created by calling Animation(String file_name); 
     * Animations created by providing a parent Animation via the alternative constructor will be considered Animations
     * of sub images. Sub image Animations are based on their parent Animation and this method cannot apply.
     */
    public String getName()
    {
        return file_name;
    }
}