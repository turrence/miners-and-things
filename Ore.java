import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends ActiveEntity {

    public Ore(String id, Point position,
               List<PImage> images, int actionPeriod) {
        super(id, position,images,actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Random rand = new Random();
        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Create.createOreBlob(getId() + " -- blob",
                pos, getActionPeriod() / 4,
                50 +
                        rand.nextInt(150 - 50),
                imageStore.getImageList("blob"));

        world.addEntity(blob);
        ((OreBlob)blob).scheduleActions(scheduler, world, imageStore);
    }

}
