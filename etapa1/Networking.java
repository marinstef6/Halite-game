
import java.io.*;
import java.util.List;

public class Networking {

// Transforms the values in the string into a matrix of integers
    static int[][] deserializeProductions(int width, int height, String inputString) {
        String[] inputStringComponents = inputString.split(" ");
        int x = 0, y = 0, idx = 0;
        int productions[][] = new int[width][height];

        while (y < height) {
            while (x < width) {
                productions[x][y] = Integer.parseInt(inputStringComponents[idx]);
                idx++;
                x++;
            }
            y++;
        }
        return productions;
    }

// Transform the list of moves into a string
    static String serializeMoveList(List<Move> moves) {
        String result = "";
        for (Move move : moves) {
            result += move.loc.x + " " + move.loc.y + " " + move.dir.ordinal() + " ";
        }
        return result;
    }

// Updates the strength for each site
    static void coord(GameMap map, String inputStringComponents[], int currentIndex) {
        int b = 0;
        while (b < map.height) {
            int a = 0;
            while (a < map.width) {
                int strength = Integer.parseInt(inputStringComponents[currentIndex++]);
                map.getLocation(a,b).getSite().strength = strength;
                a++;
            }
            b++;
        }  
    }

// Deserializes the game map from the input string
    static GameMap deserializeGameMap(String inputString, GameMap map) {
        String[] inputStringComponents = inputString.split(" ");

        int currentIndex = 0, counter = 0, owner = 0;
        int y = 0, x = 0;
        
        while (true) {
            if (y >= map.height) {
                break;
            }

            counter = Integer.parseInt(inputStringComponents[currentIndex]);
            owner = Integer.parseInt(inputStringComponents[currentIndex + 1]);
            currentIndex += 2;

            while (counter-- > 0) {
                map.getLocation(x,y).getSite().owner = owner;
                ++x;

                if(x != map.width) 
                    continue;
                else {
                    x = 0;
                    ++y;
                }
            }
        }

        coord(map, inputStringComponents, currentIndex);

        return map;
    }

    static String getString() throws IOException {
        StringBuilder builder = new StringBuilder();

        int buffer;
        buffer = System.in.read();
        // Read until a newline is encountered
        if (buffer != '\n') {
            do {
                builder.append((char) buffer);
                buffer = System.in.read();
            } while (buffer >= 0 && buffer != '\n');
        }

        return builder.toString();
    }

    static InitPackage getInit() throws IOException {
        InitPackage initPackage = new InitPackage();
    
        String input = getString();
        // Parse and set the ID
        initPackage.myID = Integer.parseInt(input);  
    
        // Reading dimensions and parsing them
        input = getString();
        String[] dimensions = input.split(" ");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
    
        input = getString();
        int[][] productions = deserializeProductions(width, height, input);
    
        // Creating and initializing the game map
        GameMap map = new GameMap(productions, width, height);
    
        // Reading the next line for game map data and deserializing it
        input = getString();
        deserializeGameMap(input, map);
    
        initPackage.map = map;
    
        return initPackage;
    }

    static void updateFrame(GameMap map) throws IOException{
        map.reset();
        deserializeGameMap(getString(), map);
    }

    static void sendFrame(List<Move> moves) {
        System.out.println(serializeMoveList(moves));
    }
    
}