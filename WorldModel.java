import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

final class WorldModel {

    private static final int ORE_REACH = 1;

    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";

    private static final String MINER_KEY = "miner";

    private static final String OBSTACLE_KEY = "obstacle";

    private static final String ORE_KEY = "ore";

    private static final String SMITH_KEY = "blacksmith";

    private static final String VEIN_KEY = "vein";

    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (withinBounds(newPt) &&
                        !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void load(Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    private boolean processLine(String line, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return Parser.parseBackground(this, properties, imageStore);
                case MINER_KEY:
                    return Parser.parseMiner(this, properties, imageStore);
                case OBSTACLE_KEY:
                    return Parser.parseObstacle(this, properties, imageStore);
                case ORE_KEY:
                    return Parser.parseOre(this, properties, imageStore);
                case SMITH_KEY:
                    return Parser.parseSmith(this, properties, imageStore);
                case VEIN_KEY:
                    return Parser.parseVein(this, properties, imageStore);
            }
        }

        return false;
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.position())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }


    private boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows &&
                pos.x >= 0 && pos.x < numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) &&
                getOccupancyCell(pos) != null;
    }

    private Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.position(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.position(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public Optional<Entity> findNearest(Point pos, Class kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getClass() == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    /*
   Assumes that there is no entity currently occupying the
   intended destination cell.
*/
    public void addEntity(Entity entity) {
        if (withinBounds(entity.position())) {
            setOccupancyCell(entity.position(), entity);
            entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.position();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.position());
    }

    private void removeEntityAt(Point pos) {
        if (withinBounds(pos)
                && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background) {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public int numRows(){return numRows;}
    public int numCols(){return numCols;}
    public Set<Entity> getEntities(){return entities;}

    public void setBackgroundImage(List<PImage> images, Point p)
    {
        this.background[p.y][p.x] = new Background("fire", images);
    }



}
