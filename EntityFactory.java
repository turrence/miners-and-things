import processing.core.PImage;

import java.util.List;

public abstract class EntityFactory {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public static Blacksmith createBlacksmith(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        return new Blacksmith(id, position, images, actionPeriod, animationPeriod);
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
    }

    public static Phoenix createPhoenix(String id, Point position,
                                        int actionPeriod, int animationPeriod, List<PImage> images) {
        return new Phoenix(id, position, images, actionPeriod, animationPeriod);
    }

    public static Fire createFire(String id, Point position,
                                  List<PImage> images)
    {
        return new Fire(id,position,images);
    }

    public static BurntVein createBurntVein(String id, Point position,
                                            List<PImage> images) {
        return new BurntVein(id, position, images);
    }


}
