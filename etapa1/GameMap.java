
public class GameMap {
    
    private final Site[][] contents;
    private final Location[][] locations;
    public final int width, height;
    

    public GameMap(int[][] productions, int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new Location[this.width][this.height];
        this.contents = new Site[this.width][this.height];
    
        initializeGameMap(productions);
    }
    
    private void initializeGameMap(int[][] productions) {
        int y = 0;
        while (y < height){
            for (int x = 0; x < width; x++) {
                initializePosition(x, y, productions[x][y]);
            }
            y++;
        }
    }

    private void initializePosition(int x, int y, int production) {
        
        Site site = new Site(production);
        contents[x][y] = site;
        Location newLocation = new Location(x, y, site);
        locations[x][y] = newLocation;
    }
     
    public double getDistance(Location loc1, Location loc2) {
        int dx = 0;
        int dy = 0;
        
        if (loc1.x > loc2.x) {
            dx = loc1.x - loc2.x;
        } else {
            dx = loc2.x - loc1.x;
        }

        if (loc1.y > loc2.y) {
            dy = loc1.y - loc2.y;
        } else {
            dy = loc2.y - loc1.y;
        }

        if(2.0 * dx > width)
            dx = width - dx;

        if(dy * 2.0 > height) 
            dy = height - dy;
 
        int sumDistance = dx + dy;
        return sumDistance;
    }
    
    public Location getLocation(Location location, Direction direction) {
        int locY = location.getY();
        int locX = location.getX();
        int newX, newY;
    
        if (direction == Direction.STILL) {
            return location;
        } else if (direction == Direction.NORTH) {
            if (locY == 0) {
                newY = height - 1;
            } else {
                newY = locY - 1;
            }
            return locations[locX][newY];
        } else if (direction == Direction.EAST) {
            if (locX == width - 1) {
                newX = 0;
            } else {
                newX = locX + 1;
            }
            return locations[newX][locY];
        } else if (direction == Direction.SOUTH) {
            if (locY == height - 1) {
                newY = 0;
            } else {
                newY = locY + 1;
            }
            return locations[locX][newY];
        } else if (direction == Direction.WEST) {
            if (locX == 0) {
                newX = width - 1;
            } else {
                newX = locX - 1;
            }
            return locations[newX][locY];
        } else {
            return null;
        }
    }
    

    public Site getSite(Location loc, Direction dir) {
        Location targetedLocation = getLocation(loc, dir);
        return getSite(targetedLocation);
    }

    public Site getSite(Location loc) {
        return loc.getSite();
    }

    public Location getLocation(int x, int y) {
        return locations[x][y];
    }

    void reset() {
        int width = this.width;
        int height = this.height;
        int x = 0, y = 0;
        while (y < height) {
            while (x < width) {
                contents[x][y].owner = 0;
                contents[x][y].strength = 0;
                x++;
            }
            y++;
        }
    }
}