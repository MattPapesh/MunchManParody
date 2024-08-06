package org.hid4java.app;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.component.ComponentBase;
import org.hid4java.fundamentals.component.ComponentScheduler;

/**
 * AppGraphics is responsible for painting all Components onto the application's window with the use of the Graphics class, and
 * the Graphics instance passed into the overridden method, paintComponent(Graphics graphics), 
 * from AppGraphics's superclass, JPanel. Moreover, Graphics is used to paint each Component from the ComponentScheduler's
 * list of registered Component instances onto the screen. 
 * 
 * @see Given that registered Components remain registered unless they are manually unregistered, all registered Components will
 * remain continuously being painted on-screen, allowing any changes they may undergo at any moment be reflected on-screen.  
 */
public class AppGraphics extends JPanel 
{
    /**
     * AppGraphics is responsible for painting all Components onto the application's window with the use of the Graphics class, and
     * the Graphics instance passed into the overridden method, paintComponent(Graphics graphics), 
     * from AppGraphics's superclass, JPanel. Moreover, Graphics is used to paint each Component from the ComponentScheduler's
     * list of registered Component instances onto the screen. 
     * 
     * @see Given that registered Components remain registered unless they are manually unregistered, all registered Components will
     * remain continuously being painted on-screen, allowing any changes they may undergo at any moment be reflected on-screen.  
     */
    public AppGraphics() {}

    @Override
    protected void paintComponent(Graphics graphics)
    {
        Graphics2D graphics_2d = (Graphics2D)graphics;
        graphics_2d.translate(Constants.WINDOW_CHARACTERISTICS.GRAPHICS_X_TRANSLATION, 
        Constants.WINDOW_CHARACTERISTICS.GRAPHICS_Y_TRANSLATION);
        for(int i = 0; i < ComponentScheduler.getComponents().size(); i++)
        {
            try
            {
                ComponentBase current_component = ComponentScheduler.getComponents().get(i);
            
                if(current_component.getActivity())
                {
                    int x = current_component.getCoordinates().getX() - (current_component.getWidth() / 2);
                    int y = current_component.getCoordinates().getY() - (current_component.getHeight() / 2);    

                    AffineTransform original_transformation = graphics_2d.getTransform();
                    double radians = Math.toRadians(current_component.getCoordinates().getDegrees());
                    int delta_x = current_component.getCoordinates().getX() + 0*current_component.getWidth();
                    int delta_y = current_component.getCoordinates().getY() + 0*current_component.getHeight();

                    graphics_2d.setClip(0, 0, Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
                    
                    graphics_2d.scale(Constants.WINDOW_CHARACTERISTICS.GRAPHICS_X_SCALER_COEFF,
                    Constants.WINDOW_CHARACTERISTICS.GRAPHICS_Y_SCALER_COEFF);
                    
                    graphics_2d.translate(delta_x, delta_y);
                    graphics_2d.rotate(radians);
                    graphics_2d.translate(-delta_x, -delta_y);
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive((float)current_component.getOpacicty()));
                    graphics_2d.drawImage(current_component.getAnimation().getAnimation(), x, y, null);
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive(1));
                    graphics_2d.setTransform(original_transformation);
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {}
            catch(IndexOutOfBoundsException e) {}
            catch (NullPointerException e) {}
        }

        float font_size = Constants.WINDOW_CHARACTERISTICS.FONT_SIZE;
        double width = Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH; 
        double height = Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT;

        // Custom font type:
        try { 
            File font_file = new File(Constants.WINDOW_CHARACTERISTICS.FONT);
            Font font = Font.createFont(Font.TRUETYPE_FONT, font_file).deriveFont(font_size);
            graphics_2d.setFont(font);
        } 
        catch(IOException | FontFormatException e) {System.out.println(e);} 

        String score = "SCORE: " + Constants.score + "    HIGH SCORE: " + Constants.high_score;
        String lives = "LIVES: ";
        String level = "LVL: " + Constants.level;

        graphics_2d.drawString(score, (int)(width - (font_size * score.length())) / 2, (int)(height * 0.05));
        graphics_2d.drawString(lives, (int)(width * 0.8) - (font_size * (lives.length() / 2)), (int)(height * 0.05));
        graphics_2d.drawString(level, (int)(width * 0.1) - (font_size * (level.length() / 2)), (int)(height * 0.05));
        repaint();
    }
}