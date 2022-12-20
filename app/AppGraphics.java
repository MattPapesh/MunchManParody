package app;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

import fundamentals.Constants;
import fundamentals.component.ComponentBase;
import fundamentals.component.ComponentScheduler;

/**
 * AppGraphics is responsible for painting all Components onto the application's window with the use of the Graphics class, and
 * the Graphics instance passed into the overridden method, paintComponent(Graphics graphics), 
 * from AppGraphics's superclass, JPanel. Moreover, Graphics is used to paint each Component from the ComponentScheduler's
 * list of registered Component instances onto the screen. 
 * 
 * @see Given that registered Components remain registered unless they are manually unregistered, all registered Components will
 * remain continuously being painted on-screen, allowing any changes they may undergo at any momement be reflected on-screen.  
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
     * remain continuously being painted on-screen, allowing any changes they may undergo at any momement be reflected on-screen.  
     */
    public AppGraphics() {}

    @Override
    protected void paintComponent(Graphics graphics)
    {
        Graphics2D graphics_2d = (Graphics2D)graphics;

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
                
                    graphics_2d.setClip(0, 0, Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
                    graphics_2d.translate(current_component.getCoordinates().getX(), current_component.getCoordinates().getY());
                    graphics_2d.rotate(radians);
                    graphics_2d.translate(-current_component.getCoordinates().getX(), -current_component.getCoordinates().getY());
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive((float)current_component.getOpacicty()));
                    graphics_2d.drawImage(current_component.getAnimation(), x, y, null);
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive(1));
                    graphics_2d.setTransform(original_transformation);
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {}
        }

        repaint();
    }
}