import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity{
    public Vein( String id, Point position,
                List<PImage> images, int actionPeriod) {
        super(id, position, images,actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler) {
        Random rand = new Random();
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Entity ore = Create.createOre("ore -- " + getId(),
                    openPt.get(), 20000 +
                            rand.nextInt(30000 - 20000),
                    imageStore.getImageList(Parse.ORE_KEY));//?? parse??
            world.addEntity(ore);
            ((Ore)ore).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Create.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }

}
