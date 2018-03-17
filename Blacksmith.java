import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Blacksmith extends AnimatedEntity {

    private static final String VEIN_KEY = "vein";
    private static Random rand = new Random();

    private PathingStrategy strategy = new AStarPathingStrategy();

    public Blacksmith(String id, Point position, List<PImage> images,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    // if right next to Vein, replace with BurntVein
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> smithTarget = world.findNearest(position(), BurntVein.class);
        long nextPeriod = getActionPeriod();

        if (smithTarget.isPresent())
        {
            Point tgtPos = smithTarget.get().position();

            if (moveTo(world, smithTarget.get(), scheduler))
            {
                System.out.println("replacing vein at: "+ smithTarget.get().position());
                Vein vein = EntityFactory.createVein("Vein:" + smithTarget.get().getId() ,
                        tgtPos, rand.nextInt(7000) + 8000 , imageStore.getImageList(VEIN_KEY));
                world.removeEntity(smithTarget.get());
                scheduler.unscheduleAllEvents(smithTarget.get()); // probably not necessary
                world.addEntity(vein);
                vein.scheduleActions(scheduler, world, imageStore);
                nextPeriod += getActionPeriod();
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                nextPeriod);
    }

    // from MinerNotFull
    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(position(), target.position()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.position());

            if (!position().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private Point nextPosition(WorldModel world, Point destPos){
        List<Point> path = strategy.computePath(position(), destPos,
                nextP -> !world.isOccupied(nextP) && withinBounds(world, nextP) &&// predicate that checks for boundaries
                        !world.getOccupant(nextP).isPresent(),
                (p1, p2) -> adjacent(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0)
            return position();
        return path.get(0);
    }

    private boolean withinBounds(WorldModel world, Point nextP){
        return nextP.x >= 0 && nextP.x < world.numCols() &&
                nextP.y >= 0 && nextP.y < world.numRows();
    }

    private boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
}
