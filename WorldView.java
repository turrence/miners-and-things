import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }


   private int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = clamp(viewport.getCol() + colDelta, 0,
              world.numCols() - viewport.getNumCols());
      int newRow = clamp(viewport.getRow() + rowDelta, 0,
              world.numRows() - viewport.getNumRows());

      viewport.shift(newCol, newRow);
   }

   private void drawBackground() {
      for (int row = 0; row < viewport.getNumRows(); row++) {
         for (int col = 0; col < viewport.getNumCols(); col++) {
            Point worldPoint = viewport.viewportToWorld(col, row);
            Optional<PImage> image = world.getBackgroundImage(
                    worldPoint);
            if (image.isPresent()) {
               screen.image(image.get(), col * tileWidth,
                       row * tileHeight);
            }
         }
      }
   }

   private void drawEntities()
   {
      for (Entity entity : world.getEntities())
      {
         Point pos = entity.position();

         if (viewport.contains(pos))
         {
            Point viewPoint = viewport.worldToViewport(pos.x, pos.y);
            screen.image(entity.getCurrentImage(),
                    viewPoint.x * tileWidth, viewPoint.y * tileHeight);
         }
      }
   }

   public void drawViewport()
   {
      this.drawBackground();
      this.drawEntities();
   }

   public PApplet getScreen() {
      return screen;
   }

   public WorldModel getWorld() {
      return world;
   }

   public int getTileWidth() {
      return tileWidth;
   }

   public int getTileHeight() {
      return tileHeight;
   }

   public Viewport getViewport() {
      return viewport;
   }

   public Point colRowToPoint(int x, int y)
   {
      return new Point(x,y);
   }
}
