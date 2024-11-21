import java.util.*;

class Flight {
    String destination;
    int time;      // Flight duration in minutes
    int layover;   // Layover duration in minutes (if applicable)

    public Flight(String destination, int time, int layover) {
        this.destination = destination;
        this.time = time;
        this.layover = layover;
    }
}

class Graph {
    private Map<String, List<Flight>> adjacencyList = new HashMap<>();

    // Add a flight connection
    public void addFlight(String source, String destination, int time, int layover) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(new Flight(destination, time, layover));
    }

    // Get neighbors of a city
    public List<Flight> getFlights(String city) {
        return adjacencyList.getOrDefault(city, new ArrayList<>());
    }

    // Dijkstra's Algorithm to find shortest time
    public Map<String, Integer> findShortestTimes(String start) {
        PriorityQueue<Flight> queue = new PriorityQueue<>(Comparator.comparingInt(f -> f.time));
        Map<String, Integer> shortestTimes = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new Flight(start, 0, 0));
        shortestTimes.put(start, 0);

        while (!queue.isEmpty()) {
            Flight current = queue.poll();
            String currentCity = current.destination;

            if (visited.contains(currentCity)) continue;
            visited.add(currentCity);

            for (Flight flight : getFlights(currentCity)) {
                String neighbor = flight.destination;
                int totalTravelTime = shortestTimes.get(currentCity) + flight.time + flight.layover;

                if (totalTravelTime < shortestTimes.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    shortestTimes.put(neighbor, totalTravelTime);
                    queue.offer(new Flight(neighbor, totalTravelTime, 0));
                }
            }
        }

        return shortestTimes;
    }
}

public class FlightOptimizer {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Add flight routes between popular Indian cities with travel and layover times
        graph.addFlight("Delhi", "Mumbai", 120, 30);
        graph.addFlight("Delhi", "Bangalore", 150, 20);
        graph.addFlight("Delhi", "Kolkata", 130, 15);
        graph.addFlight("Mumbai", "Chennai", 110, 25);
        graph.addFlight("Mumbai", "Hyderabad", 90, 20);
        graph.addFlight("Bangalore", "Kolkata", 180, 40);
        graph.addFlight("Bangalore", "Chennai", 60, 10);
        graph.addFlight("Kolkata", "Hyderabad", 150, 30);
        graph.addFlight("Hyderabad", "Chennai", 70, 15);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the starting city: ");
        String startCity = scanner.nextLine();

        System.out.print("Enter the destination city: ");
        String endCity = scanner.nextLine();

        // Find shortest times
        Map<String, Integer> shortestTimes = graph.findShortestTimes(startCity);

        // Output the shortest time
        if (shortestTimes.containsKey(endCity)) {
            int totalMinutes = shortestTimes.get(endCity);
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            System.out.println("Shortest time from " + startCity + " to " + endCity + ": "
                    + hours + " hours and " + minutes + " minutes.");
        } else {
            System.out.println("No path found from " + startCity + " to " + endCity);
        }

        scanner.close();
    }
}
