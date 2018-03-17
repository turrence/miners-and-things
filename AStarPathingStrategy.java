import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        //List<Point> open = new ArrayList<>();
        //List<Point> closed = new ArrayList<>();
        List<Point> path = new ArrayList<>();

        //point then prior
        HashMap<Point, Point> prior = new HashMap<>();
        HashMap<Point,Integer> gVal = new HashMap<>();
        HashMap<Point, Integer> fVal = new HashMap<>();

        HashMap<Point,Integer> closed = new HashMap<>();
        Comparator<Point> comp = (p1,p2) -> fVal.get(p1) - fVal.get(p2);
        PriorityQueue<Point> open = new PriorityQueue<>(comp);

        open.add(start);
        Point current = start;
        gVal.put(current,0);
        fVal.put(current,hCost(current,end));
        prior.put(current,null);

        while(!withinReach.test(current,end))
        {
            //dealing with neighbors and adding to the open list
            for (Point p : potentialNeighbors.apply(current).filter(canPassThrough).collect(Collectors.toList())) {
                if (closed.get(p) != null|| open.contains(p)) {
                    continue;
                }
                //add all the neighbors and calculate their values as necessary
                prior.put(p, current);
                gVal.put(p,gVal.get(current) + 1);
                fVal.put(p,hCost(p,end) + gVal.get(p));
                open.add(p);

            }
            //take care of current because were done with it
            open.remove(current);
            closed.put(current,1);

            //returns empty list if no where to move
            if (open.isEmpty())
                return path;

            //find the smallest fval point
            /*Point temp = open.get(0);
            for (int i = 1; i< open.size(); i++)
            {
                if (fVal.get(open.get(i)) < fVal.get(temp))
                {
                    temp = open.get(i);
                }
            }
            current = temp;*/

            //changed data structure to priority queue
            current = open.poll();
        }

        //build path using prior
        while(prior.get(current) != null)
        {
            path.add(0,current);
            current = prior.get(current);
        }
        return path;

    }

    private int hCost(Point start, Point goal)
    {
        //change in x plus change in y
        return Math.abs((goal.getX() - start.getX())) + Math.abs((goal.getY() - start.getY()));
    }

}
