import java.util.List;
import processing.core.PImage;

final class Background
{
   public String id;
   private List<PImage> images;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
   public PImage getCurrentImage()
   {
         return images.get(0);
   }
}
