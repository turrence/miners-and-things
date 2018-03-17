import processing.core.PImage;

import java.util.List;

abstract public class AnimatedEntity extends ActiveEntity {

    private int animationPeriod;


    public AnimatedEntity(String id, Point position,
                          List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this,
                createAnimationAction(0), getAnimationPeriod());
    }

    public Animation createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }

    public void nextImage()
    {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }
}
