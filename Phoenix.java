import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Phoenix extends AnimatedEntity {

    private PathingStrategy strategy = new AStarPathingStrategy();

    public Phoenix(String id, Point position,
                   List<PImage> images,
                   int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    // if right next to Vein, replace with BurntVein
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> phoenixTarget = world.findNearest(position(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (phoenixTarget.isPresent())
        {
            Point tgtPos = phoenixTarget.get().position();

            if (moveTo(world, phoenixTarget.get(), scheduler))
            {
                System.out.println("replacing vein at: "+ phoenixTarget.get().position());
                BurntVein burnt_vein = EntityFactory.createBurntVein("burnt vein",
                        tgtPos, imageStore.getImageList("burntvein"));
                world.removeEntity(phoenixTarget.get());
                scheduler.unscheduleAllEvents(phoenixTarget.get());
                world.addEntity(burnt_vein);
                nextPeriod += getActionPeriod();
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                nextPeriod);
    }

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
                nextP -> (!world.isOccupied(nextP) || world.getOccupancyCell(nextP).getClass() == Fire.class)
                        && withinBounds(world, nextP) &&// predicate that checks for boundaries
                        (!world.getOccupant(nextP).isPresent() || world.getOccupancyCell(nextP).getClass() == Fire.class),
                (p1, p2) -> adjacent(p1,p2),
                PathingStrategy.ALL_NEIGHBORS);
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
        return (Math.abs(p1.x - p2.x) == 1 || Math.abs(p1.x - p2.x) == 0) &&
                (Math.abs(p1.y - p2.y) == 1 || Math.abs(p1.y - p2.y) == 0);
    }

}