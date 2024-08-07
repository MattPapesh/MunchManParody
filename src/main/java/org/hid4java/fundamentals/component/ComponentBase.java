package org.hid4java.fundamentals.component;

import java.util.LinkedList;

import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;

/**
 * The superclass to every app component. ComponentBase allows component classes to inherit methods for setting its image's
 * Coordinates, Animation, opacity, and activity. It is required of every component to extend ComponentBase as a superclass while 
 * calling the addRequirements(int x, int y, int degrees, Animation... animations) function for apropriate functionality.
 * 
 * @see
 * Note: When ComponentBase is used as a superclass and it is extended to a sublcass, that component subclass will immediately create 
 * its image on screen upon instantiation.   
 */
public class ComponentBase 
{
    private final double COMPONENT_ID = Math.random();
    private LinkedList<Animation> animations = new LinkedList<Animation>();
    private Animation current_animation = new Animation("");
    private int current_animation_index = 0;
    private Coordinates coordinates = new Coordinates(0, 0, 0);
    private double opacity_pct = 1.0;
    private boolean active = false;

    /**
     * Once ComponentBase has been extended and become a superclass to a sublcass, the subclass must call 
     * this method in order for the subclass to apropriately function as a app component. Moreover, the initial on-screen
     * coordinates of the component must be passed in; along with the component's rotation relative to the X-axis. Lastly, 
     * all Animation instances must be passed in. Animations will be PNGs or GIFs, and multiple can be passed into the parameters; 
     * these animations will be considered as all of the visual representations that the component can have at any time when
     * running the application.  
     * 
     * @see
     * Note: Animation is a vague term referring to any PNG or GIF image. 
     */
    public void addRequirements(int x, int y, int degrees, Animation... animations)
    {
        current_animation = animations[0];
        coordinates.setCoordinates(x, y, degrees);
        importAnimations(animations);
        toggleActivity(true);
        ComponentScheduler.registerComponent(this);
    }

    public void setNextAnimation()
    {
        current_animation_index = (current_animation_index + 1 < animations.size()) ? current_animation_index + 1 : 0;
        current_animation = animations.get(current_animation_index);
    }

    /**
     * The current image can be set by passing in the name of the image file; file type included. (EX: "myImage.png" )
     * 
     * @see
     * Note: If the method is never called, the image belonging to the first Animation instance passed into the constructor will be used.
     */
    public void setAnimation(String animation_name)
    {
        for(int i = 0; i < animations.size(); i++)
        {
            if(animations.get(i).getName() == animation_name)
            {
                current_animation = animations.get(i);
                current_animation_index = i;
            }
        }
    }

    public void importAnimations(Animation... animations) 
    {
        this.animations.clear();
        for(int i = 0; i < animations.length; i++)
        {
            this.animations.addLast(animations[i]);
        }

        current_animation_index = 0;
        current_animation = animations[0];
    }

    /**
     * Used to set the component's opacity when on-screen. Opacity is how "solid" or visible the image is. The oppisite 
     * of transparency.  
     * 
     * @param pct - How opaque the component's image should be on screen as a percentage that is represented as a decimal.
     * 
     * @see
     * Note: The perentage representation as a decimal must be a value within the interval: [0, 1].
     */
    public void setOpacity(double pct)
    {
        opacity_pct = pct;
    }

    /**
     * @return How opaque the component is on-screen as a percentage that is represented as a decimal.
     * 
     * @see
     * Note: The value returned will be within the interval: [0, 1].
     */
    public double getOpacicty()
    {
        return opacity_pct;
    }

    /**
     * Toggles the component's activity to either be enabled or disabled.
     * 
     * @param active
     * - The new toggled active state for the controller.
     */
    public void toggleActivity(boolean active)
    {
        if(!this.active && active)
        {
            this.active = true;
            //ComponentScheduler.registerComponent(this);
        }
        else if(this.active & !active)
        {
            this.active = false; 
            //ComponentScheduler.removeComponent(this);
        }
    }

    /**
     *  Sets the component's coordinates.
     */
    public void setCoordinates(int x, int y, int degrees)
    {
        coordinates.setCoordinates(x, y, degrees);
    }

    /**
     * @return The component's coordinates. 
     */
    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    /**
     * @return The component's personal ID; a specific and unique value that is assigned upon instantiation.
     */
    public double getComponentID()
    {
        return COMPONENT_ID;
    }

    /**
     * @return The current Animation instance;
     */
    public Animation getAnimation()
    {
        return current_animation;
    }

    /**
     * @return An Image instance of the animation/image specified by the index parameter.
     * 
     * @param index The animation/image index of the list of animations provided when addRequirements(...) is called. 
     */
    public Animation getAnimation(int index)
    {
        if(index < animations.size())
        {
            return animations.get(index);
        }
        
        return null;
    }

    /**
     * @return An the width of the animation/image that the component is currently using. 
     * 
     * @see
     * Note: Unit of measurement: Pixels.
     */
    public int getWidth()
    {
        return current_animation.getImageWidth();
    }

    /**
     * @return An the height of the animation/image that the component is currently using. 
     * 
     * @see
     * Note: Unit of measurement: Pixels.
     */
    public int getHeight()
    {
        return current_animation.getImageHeight();
    }

    /**
     * @return The activity status of the component. 
     */
    public boolean getActivity()
    {
        return active;
    }

    public Animation getSubAnimation(int img_x0, int img_y0, int img_x1, int img_y1, int index)
    {   
        if(index < animations.size())
        {
            return new Animation(img_x0, img_y0, img_x1, img_y1, animations.get(index));
        }
        
        return null;
    }
}
