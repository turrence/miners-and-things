import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends AnimatedEntity {

    private int resourceLimit;
    private int resourceCount;
    private PathingStrategy strategy = new AStarPathingStrategy();


    public MinerNotFull(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;

    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(position(), Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore), getActionPeriod());
        }
    }

    private boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            MinerFull miner = EntityFactory.createMinerFull(getId(), resourceLimit,
                    position(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(position(), target.position()))
        {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

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

    private Point nextPosition(WorldModel world, Point destPos)
    {
/*        int horiz = Integer.signum(destPos.x - position().x);
        Point newPos = new Point(position().x + horiz,
                position().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - position().y);
            newPos = new Point(position().x,
                    position().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = position();
            }
        }
        return newPos;*/

        List<Point> path = strategy.computePath(position(), destPos,
                nextP -> !world.isOccupied(nextP) && withinBounds(world, nextP) &&
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

    public int getResourceLimit(){return resourceLimit;}
    public int getResourceCount(){return resourceCount;}

}
