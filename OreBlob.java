import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends AnimatedEntity {

    private static final String QUAKE_KEY = "quake";
    private PathingStrategy strategy = new AStarPathingStrategy();

    public OreBlob(String id, Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(position(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position();

            if (moveTo(world, blobTarget.get(), scheduler))
            {
                Quake quake = EntityFactory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
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
    {/*
        int horiz = Integer.signum(destPos.x - position().x);
        Point newPos = new Point(position().x + horiz,
                position().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
        {
            int vert = Integer.signum(destPos.y - position().y);
            newPos = new Point(position().x, position().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
            {
                newPos = position();
            }
        }

        return newPos;*/

        List<Point> path = strategy.computePath(position(), destPos,
                nextP -> (!world.isOccupied(nextP) && // predicate that checks for boundaries
                        withinBounds(world, nextP)) && !isOreAtPoint(world, nextP),
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

    private boolean isOreAtPoint(WorldModel world, Point nextP){
        Optional<Entity> occupant = world.getOccupant(nextP);
        return occupant.isPresent() && !(occupant.get().getClass() == Ore.class);

    }

    private boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }




}
