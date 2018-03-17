import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

public abstract class Parser
{

   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_ID = 1;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_ACTION_PERIOD = 5;
   private static final int MINER_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   private static final int ORE_ID = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_ACTION_PERIOD = 4;

   private static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 4;
   private static final int SMITH_ID = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;

   private static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 5;
   private static final int VEIN_ID = 1;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
   private static final int VEIN_ACTION_PERIOD = 4;

    // PImage getCurrentImage()
    // int getAnimationPeriod()
    // Entity void nextImage()
    // Action executeAction(Scheduler)
    // Action executeActivityAction(Scheduler)
    // Scheduler scheduleEvent()
    // Action executeAnimationAction(Scheduler)
    // Entity executeMinerFullActivity(WorldModel, ImageStore, EventScheduler)
    // Entity executeMinerNotFullActivity(WorldMode, ImageStore, EventScheduler)
    // Entity executeOreActivity(WorldModel, ImageStore, EventScheduler)
    // Entity executeOreBlobActivity(WorldModel, ImageStore, EventScheduler)
    // Entity executeQuakeActivity(WorldModel, ImageStore, EventScheduler)
    // Entity executeVeinActivity(WorldModel, ImageStore, EventScheduler)
    // Entity transformNotFull(WorldModel, EventScheduler, ImageStore)
    // Entity moveToNotFull(WorldModel, Entity, EventScheduler)
    // Entity moveToFull(WorldModel, Entity, EventScheduler)
    // Entity moveToOreBlob(WorldModel, Entity, EventScheduler)
    // Entity nextPositionMiner(WorldModel, Point)
    // Entity nextPositionOreBlob(WorldModel, Point)
    // Entity adjacent(Point, Point)
    // WorldModel findOpenAround(Point)
    // Scheduler unscheduleAllEvents(Entity)
    // Scheduler removePendingEvent(Event)
    // Scheduler updateOnTime(long)
    // ImageStore getImageList(ImageStore, String)
    // ImageStore loadImages(Scanner, Screen)
    // ImageStore processImageLine(Map<String, List<PImage>>, String, PApplet)
    // ImageStore getImages(Map<String, List<PImage>>, String)
    // ImageStore setAlpha(PImage, int, int)
    // ViewPort shift(int, int)
    // ViewPort contains(int)
    // WorldModel load(Scanner, ImageStore)
    // WorldModel processLine(String, ImageStore)
    // WorldModel tryAddEntity(Entity)
    // WorldModel withinBounds(pos)
    // WorldModel isOccupied(pos)
    // WorldModel nearestEntity(List<Entity>, Point)
    // WorldModel distanceSquared(Point, Point)
    // WorldModel findNearest(Point, EntityKind)
    // WorldModel addEntity(entity)
    // WorldModel moveEntity(entity, Point)
    // WorldModel removeEntity(entity)
    // WorldModel removeEntityAt(entity, Point)
    // WorldModel getBackgroundImage(Point)
    // WorldModel setBackground(Point, Background)
    // WorldModel getOccupant(Point)
    // WorldModel getOccupancyCell(Point)
    // WorldModel setOccupancyCell(Point, Entity)
    // WorldModel getBackgroundCell(Point)
    // WorldModel setBackgroundCell(Point, Background)
    // ViewPort viewportToWorld(int, int)
    // ViewPort worldToViewport(int, int)
    // WorldView clamp(int, int, int)
    // WorldView shiftView(int, int)
    // WorldView drawBackground()
    // WorldView drawEntities()
    // WorldView drawEntities()
    // Entity createAnimationAction(int)
    // Entity createAnimationAction(world, ImageStore)

    //createBlacksmith(String, Point, List<PImage>)
    //createMinerFull(String, Point, List<PImage>)
    //createMinerNotFull(String, int, Point, int, int, List<PImage>)
    //createObstacle(String, Point, List<PImage>)
    //createOre(String, Point, int, List<PImage>)
    //createOreBlob(String, Point, int, int, List<PImage>)
    //createQuake(Point, List<PImage>)
    //createVein(String, Point, int, List<PImage>)
    //parseBackground(WorldModel, String[], ImageStore)
    //parseMiner(WorldModel, String[], ImageStore)
    //parseObstacle(WorldModel, String[], ImageStore)
    //parseOre(WorldModel, String[], ImageStore)
    //parseSmith(WorldModel, String[], ImageStore)
    //parseVein(WorldModel, String[], ImageStore)

/*
   public static Blacksmith createBlacksmith(String id, Point position,
                                  List<PImage> images) {
      return new Blacksmith(id, position, images);
   }

   public static MinerFull createMinerFull(String id, int resourceLimit,
                                 Point position, int actionPeriod, int animationPeriod,
                                 List<PImage> images) {
      return new MinerFull(id, position, images, resourceLimit, actionPeriod, animationPeriod);
   }

   public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                    Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images) {
      return new MinerNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public static Obstacle createObstacle(String id, Point position,
                                List<PImage> images) {
      return new Obstacle(id, position, images);
   }

   public static Ore createOre(String id, Point position, int actionPeriod,
                           List<PImage> images) {
      return new Ore(id, position, images, actionPeriod);
   }

   public static OreBlob createOreBlob(String id, Point position,
                               int actionPeriod, int animationPeriod, List<PImage> images) {
      return new OreBlob(id, position, images, actionPeriod, animationPeriod);
   }

   public static Quake createQuake(Point position, List<PImage> images) {
      return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public static Vein createVein(String id, Point position, int actionPeriod,
                            List<PImage> images) {
      return new Vein(id, position, images, actionPeriod);
   }*/


    public static boolean parseBackground(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public static boolean parseMiner(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            Entity entity = EntityFactory.createMinerNotFull(properties[MINER_ID],
                    Integer.parseInt(properties[MINER_LIMIT]),
                    pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(MINER_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    public static boolean parseObstacle(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = EntityFactory.createObstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public static boolean parseOre(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                    Integer.parseInt(properties[ORE_ROW]));
            Entity entity = EntityFactory.createOre(properties[ORE_ID],
                    pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                    imageStore.getImageList(ORE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    public static boolean parseSmith(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = EntityFactory.createBlacksmith(properties[SMITH_ID],
                    pt, imageStore.getImageList(SMITH_KEY), MINER_ACTION_PERIOD, MINER_ANIMATION_PERIOD);
            world.tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    public static boolean parseVein(WorldModel world, String[] properties, ImageStore imageStore) {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = EntityFactory.createVein(properties[VEIN_ID],
                    pt,
                    Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(VEIN_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }



}
