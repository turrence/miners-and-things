import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough, // within bounds and not obstacle
                                   BiPredicate<Point, Point> withinReach, // checks adjacency
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        Point initial = start;
        LinkedList<Node> closeList = new LinkedList<>();
        LinkedList<Node> openList = new LinkedList<>();
        List<Point> path = new LinkedList<>();

        Node currentNode = new Node(start, null, Math.abs(start.x - end.x) + Math.abs(start.y - end.y));
        openList.add(currentNode);

        while(openList.size() > 0){
//            System.out.println("Start: " + initial);
//            System.out.println("Current Node: "+ currentNode);
//            System.out.println("Destination Node: "+ end);
            if(adjacent(currentNode.point, end)){// end case
//                System.out.println("PATH FOUND....");
                Node temp = currentNode.prior;
                path.add(currentNode.point);
                while (temp.prior != null) {
//                    System.out.println("ADDING " + temp + " TO LIST");
                    path.add(0, temp.point);
                    temp = temp.prior;
                }
//                System.out.println("PATH: ");
//                path.forEach(point -> System.out.print(point + " -> "));
//            throw new RuntimeException("well u found it");
                return path;
            }

            Node finalCurrentNode = currentNode;
            List<Point> neighbors = potentialNeighbors.apply(currentNode.point)
                    .filter(canPassThrough) // will vary from Miners to OreBlob
                    .filter(point -> withinReach.test(point, finalCurrentNode.point))
//                   .limit(1)
                    /*.filter(pt -> !isInList(pt, closeList))
                    !pt.equals(start)
                    && !pt.equals(end)
                    && Math.abs(end.x - pt.x) <= Math.abs(end.x - start.point.x)
                    && Math.abs(end.y - pt.y) <= Math.abs(end.y - start.point.y)) // replace with a withinReach*/
                    .collect(Collectors.toList());
//                    neighbors.forEach(n -> System.out.println("Neighbors: "+n));

/*            if (neighbors.size() == 0){
                Node deleted = openList.remove(0);
                closeList.add(deleted);
                Collections.sort(openList, Comparator.comparing(Node :: total));
            }*/

            for(Point pt : neighbors){
                Node node = new Node(pt, currentNode, Math.abs(pt.x - end.x) + Math.abs(pt.y - end.y));
                if (!(closeList.contains(node) || openList.contains(node))){// not in close list or open list
                    openList.add(node);
                }
                else if (openList.contains(node) && !closeList.contains(node)){// in open list replace
                    if(openList.get(openList.indexOf(node)).fromStart < node.fromStart)
                        openList.set(openList.indexOf(node), node);
                }
            }

            openList.remove(0);
            closeList.add(currentNode);
            Collections.sort(openList, Comparator.comparing(Node :: total));

            if(openList.size() == 0){
//                System.out.println("PATH NOT FOUND");
                return path;
            }

            currentNode = openList.get(0);
        }
        return path;
    }
    private boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }



    private class Node{
        private final Point point;
        private final Node prior;
        private int fromStart;  // g value
        private int toEnd;      // h value

        private Node(Point pt, Node prior, int h){
            point = pt;
            this.prior = prior;
            fromStart = prior == null ? 0 : prior.fromStart + 1;
            toEnd = h;
        }

        private int total(){return fromStart + toEnd;}

        private Node prior(){return prior;}

        private Point point(){return point;}

        public boolean equals(Object o){
            if (o == null)
                return false;
            if(o.getClass() != getClass())
                return false;
            Node obj = (Node) o;
            return point.equals(obj.point);
        }

        public String toString(){
            return point.toString() + " PRIOR: " + ((prior == null) ? "NONE" : prior.point.toString());
        }
    }
}

