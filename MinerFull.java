import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MoveEntity{

    private int resourceLimit;
    PathingStrategy strategy = new SingleStepPathingStrategy();


    public MinerFull(String id, Point position,
                     List<PImage> images, int resourceLimit,
                     int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);;
        this.resourceLimit = resourceLimit;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    Create.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity miner = Create.createMinerNotFull(getId(),resourceLimit,
                getPosition(), getActionPeriod(), getAnimationPeriod(),
                getImageList());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(miner);
        ((MinerNotFull)miner).scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            super.moveTo(world, target, scheduler);
            return false;
        }
    }

}
