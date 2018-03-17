import processing.core.PImage;

import java.util.List;

public class BurntVein extends Obstacle{
    private int actionPeriod;
    public BurntVein(String id, Point position,
                     List<PImage> images)
    {
        super(id, position, images);

    }
}