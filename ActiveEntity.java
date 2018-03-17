import processing.core.PImage;

import java.util.List;

abstract public class ActiveEntity extends Entity {

    private int actionPeriod;

    public ActiveEntity(String id, Point position,
                        List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;

    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                getActionPeriod());
    }
    public int getActionPeriod(){return actionPeriod;}

    protected Activity createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }

    abstract public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
