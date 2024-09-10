import java.util.*;

public class MyBot {
    // Finds the closest site in the given direction that has a different owner
    static public Location findClosestSite(Location currentLocation, Direction direction, Site currentSite, GameMap map) {
        int counter = 0;
        int maxDistance = direction == Direction.NORTH || direction == Direction.SOUTH ? map.height / 2 : map.width / 2;
        Location nextLocation = currentLocation;
        
        while (counter < maxDistance) {
            nextLocation = map.getLocation(nextLocation, direction);
            if (nextLocation.getSite().owner != currentSite.owner) {
                break;
            }
            counter++;
        }
        
        return nextLocation;
    }
    
    public static void main(String[] args) throws java.io.IOException {

        // Initialize the game package
        final InitPackage iPackage = Networking.getInit();
        System.out.println("MyJavaBot");

        while (true) {

            List<Move> moves = new ArrayList<Move>();

            // Update the map for the current frame
            Networking.updateFrame(iPackage.map);

            // Iterate over all locations on the map
            for (int y = 0; y < iPackage.map.height; y++) {
                for (int x = 0; x < iPackage.map.width; x++) {

                    // Fetch the current location and its site data
                    final Location location = iPackage.map.getLocation(x, y);
                    final Site site = location.getSite();

                    // Check if the current site belongs to MyBot
                    final int myID = iPackage.myID;
                    if (site.owner == myID) {
                        if (site.strength >= 5) { 
                            // Create a priority queue to store the best directions to move
                            PriorityQueue<Move> bestDirection = new PriorityQueue<>((o1, o2) ->
                                Integer.compare(o1.loc.getSite().strength, o2.loc.getSite().strength));

                            // Directions to check for potential moves
                            Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
                            Location[] adjacentLocations = {
                                iPackage.map.getLocation(location, Direction.NORTH),
                                iPackage.map.getLocation(location, Direction.SOUTH),
                                iPackage.map.getLocation(location, Direction.WEST),
                                iPackage.map.getLocation(location, Direction.EAST)
                            };

                            // Evaluate all adjacent locations for potential moves
                            for (int i = 0; i < directions.length; i++) {
                                if (adjacentLocations[i].getSite().owner != site.owner) {
                                    bestDirection.add(new Move(adjacentLocations[i], directions[i]));
                                }
                            }

                            // Add the best move
                            if (!bestDirection.isEmpty()) {
                                moves.add(new Move(location, bestDirection.peek().dir));
                            }

                            // If surrounded by own sites, find the closest site and plan a move towards it
                            if (bestDirection.isEmpty()) {
                                Location farNorth = findClosestSite(location, Direction.NORTH, site, iPackage.map);
                                Location farSouth = findClosestSite(location, Direction.SOUTH, site, iPackage.map);
                                Location farWest = findClosestSite(location, Direction.WEST, site, iPackage.map);
                                Location farEast = findClosestSite(location, Direction.EAST, site, iPackage.map);

                                // Create a priority queue to compare the distance to the closest site
                                PriorityQueue<Move> farMoves = new PriorityQueue<>(Comparator.comparingInt(o -> (int) iPackage.map.getDistance(location, o.loc)));

                                Location[] locationFar = {farNorth, farSouth, farWest, farEast}; 
                                for (int i = 0; i < locationFar.length; i++) {
                                    farMoves.add(new Move(locationFar[i], directions[i]));
                                }

                                // Plan the best move towards the most strategic distance
                                moves.add(new Move(location, farMoves.peek().dir));
                            }
                        }
                    }
                }
            }
            Networking.sendFrame(moves);
        }
    }
}
