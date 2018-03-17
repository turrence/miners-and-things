import processing.core.PImage;

import java.util.List;

abstract public class Entity{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }
    public String getId(){return id;}
    public Point position(){return position;}
    public void setPosition(Point p){position = p;}
    public List<PImage> getImages(){return images;}
    public int getImageIndex(){return imageIndex;}
    protected void setImageIndex(int num){imageIndex = num;}



}