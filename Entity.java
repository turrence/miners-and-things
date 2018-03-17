import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    public String id;
    private Point position;
    private List<PImage> images;

    public Entity(String id, Point position,
                  List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
    }
    public String getId(){return id;}

    public List<PImage> getImageList()
    {
        return images;
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point pos)
    {
        position = pos;
    }

    public PImage getCurrentImage() { return images.get(0); }

}
