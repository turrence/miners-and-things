import processing.core.PApplet;
import processing.core.PImage;

import java.util.Scanner;

public class Load {

    public static final int COLOR_MASK = 0xffffff;

    public static final int PROPERTY_KEY = 0;

    public static final String BGND_KEY = "background";

    public static final String MINER_KEY = "miner";

    public static final String OBSTACLE_KEY = "obstacle";

    public static final String ORE_KEY = "ore";

    public static final String SMITH_KEY = "blacksmith";

    public static final String VEIN_KEY = "vein";


    public static void setAlpha(PImage img, int maskColor, int alpha)
    {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++)
        {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
            {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public static void load(Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine())
        {
            try
            {
                if (!processLine(in.nextLine(), world, imageStore))
                {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            }
            catch (IllegalArgumentException e)
            {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    public static boolean processLine(String line, WorldModel world,
                                      ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0)
        {
            switch (properties[PROPERTY_KEY])
            {
                case BGND_KEY:
                    return Parse.parseBackground(properties, world, imageStore);
                case MINER_KEY:
                    return Parse.parseMiner(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return Parse.parseObstacle(properties, world, imageStore);
                case ORE_KEY:
                    return Parse.parseOre(properties, world, imageStore);
                case SMITH_KEY:
                    return Parse.parseSmith(properties, world, imageStore);
                case VEIN_KEY:
                    return Parse.parseVein(properties, world, imageStore);
            }
        }

        return false;
    }

}
