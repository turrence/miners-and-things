import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActiveEntity
{
    private int imageIndex;
    private int animationPeriod;
    public AnimationEntity(String id, Point position,
                           List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id ,position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }
    public void nextImage() {
        imageIndex = (imageIndex + 1) % getImageList().size();
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Create.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this, Create.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    public PImage getCurrentImage() {
        return getImageList().get(imageIndex);
    }
    public int getAnimationPeriod() {
        return animationPeriod;
    }
}
