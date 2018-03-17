import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OreBlob extends MoveEntity{

    private final String QUAKE_KEY = "quake";
    //PathingStrategy strategy = new SingleStepPathingStrategy();
    PathingStrategy strategy = new AStarPathingStrategy();

    public OreBlob( String id, Point position,
                   List<PImage> images,
                   int actionPeriod, int animationPeriod) {
       super(id,position,images,actionPeriod,animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> blobTarget = world.findNearest(getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo(world, blobTarget.get(), scheduler)) {
                Entity quake = Create.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                ((Quake)quake).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Create.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        /*int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore))) {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore))) {
                newPos = getPosition();
            }
        }*/
        List<Point> points = strategy.computePath(getPosition(),destPos,p-> !p.equals(destPos)
                        && world.withinBounds(p)
                        && ( !world.isOccupied(p)
                        || world.getOccupant(p).equals(Ore.class) )
                        ,(p1,p2) -> p1.adjacent(p2)
                        ,PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0)
        {
            return getPosition();
        }
        return points.get(0);    }


}
