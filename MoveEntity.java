import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MoveEntity extends AnimationEntity{
    //PathingStrategy strategy = new SingleStepPathingStrategy();
    PathingStrategy strategy = new AStarPathingStrategy();

    public MoveEntity(String id, Point position,
                      List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id,position,images,actionPeriod,animationPeriod);
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        Point nextPos = nextPosition(world, target.getPosition());

        if (!getPosition().equals(nextPos))
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


    public Point nextPosition(WorldModel world, Point destPos) {
/*        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(),
                    getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = getPosition();
            }
        }*/

        List<Point> points = strategy.computePath(getPosition(),destPos,p-> !p.equals(destPos)
                        && world.withinBounds(p)
                        && !world.isOccupied(p),
                (p1,p2) -> p1.adjacent(p2),PathingStrategy.CARDINAL_NEIGHBORS);
        if (points.size() == 0)
        {
            return getPosition();
        }
        return points.get(0);

    }



}
