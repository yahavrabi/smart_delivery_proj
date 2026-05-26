package yahavr.smart_delivery_proj.services;

import org.springframework.stereotype.Service;
import yahavr.smart_delivery_proj.datamodels.*;
import java.util.*;

@Service
public class GeneticRoutingService {

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 500;
    private static final double MUTATION_RATE = 0.05;

    public Map<String, List<Order>> calculateRoutes(List<Order> orders, List<Vehicle> vehicles, Warehouse depot,
            double[][] matrix) {
        if (orders.isEmpty() || vehicles.isEmpty() || matrix == null)
            return new HashMap<>();

        List<List<Order>> population = initializePopulation(orders);
        List<Order> bestSequence = null;
        double bestFitness = -1.0;

        for (int i = 0; i < GENERATIONS; i++) {
            population.sort((a, b) -> Double.compare(calculateFitness(b, vehicles, matrix),
                    calculateFitness(a, vehicles, matrix)));

            double currentBestFitness = calculateFitness(population.get(0), vehicles, matrix);
            if (currentBestFitness > bestFitness) {
                bestFitness = currentBestFitness;
                bestSequence = new ArrayList<>(population.get(0));
            }

            List<List<Order>> nextGen = new ArrayList<>();
            for (int j = 0; j < 10; j++)
                nextGen.add(new ArrayList<>(population.get(j)));

            while (nextGen.size() < POPULATION_SIZE) {
                List<Order> parent1 = selectParent(population);
                List<Order> parent2 = selectParent(population);
                List<Order> child = crossover(parent1, parent2);
                mutate(child);
                nextGen.add(child);
            }
            population = nextGen;
        }

        return decodeSolution(bestSequence, vehicles);
    }

    private double calculateFitness(List<Order> sequence, List<Vehicle> vehicles, double[][] matrix) {
        Map<String, List<Order>> routes = decodeSolution(sequence, vehicles);
        double totalDistance = 0;

        Map<Order, Integer> orderToIdx = new HashMap<>();
        for (int i = 0; i < sequence.size(); i++) {
            orderToIdx.put(sequence.get(i), i + 1);
        }

        for (List<Order> route : routes.values()) {
            if (route.isEmpty())
                continue;

            totalDistance += matrix[0][orderToIdx.get(route.get(0))];

            for (int i = 0; i < route.size() - 1; i++) {
                int fromIdx = orderToIdx.get(route.get(i));
                int toIdx = orderToIdx.get(route.get(i + 1));
                totalDistance += matrix[fromIdx][toIdx];
            }

            totalDistance += matrix[orderToIdx.get(route.get(route.size() - 1))][0];
        }
        return 1.0 / (totalDistance + 1.0);
    }

    /**
     * ה-Decoder המעודכן: חלוקה פרופורציונלית לפי קיבולת הרכבים.
     * מונע מצב שבו רכב אחד עמוס מדי והשני ריק, מה שמשפר את יעילות המסלולים הכוללת.
     */
    private Map<String, List<Order>> decodeSolution(List<Order> sequence, List<Vehicle> vehicles) {
        Map<String, List<Order>> solution = new LinkedHashMap<>();
        int totalOrders = sequence.size();
        
        // 1. חישוב סך הקיבולת המשותפת של כל הרכבים
        double totalCapacity = vehicles.stream()
                               .mapToDouble(Vehicle::getMaxCapacity)
                               .sum();
        
        int orderIdx = 0;
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            List<Order> route = new ArrayList<>();

            // 2. חישוב כמה הזמנות הרכב הזה אמור לקבל באופן יחסי (Target Load)
            // נוסחה: (קיבולת הרכב / סך הקיבולת) * סך ההזמנות
            int targetLoad = (int) Math.round(((double) v.getMaxCapacity() / totalCapacity) * totalOrders);
            
            // הגנה: שלא יחרוג מהקיבולת הפיזית שלו ושלא ינסה לקחת יותר ממה שנשאר ברצף
            int actualLimit = (int) Math.min(v.getMaxCapacity(), targetLoad);
            
            // אם זה הרכב האחרון, הוא לוקח את כל השארית כדי שלא נאבד הזמנות
            if (i == vehicles.size() - 1) {
                actualLimit = totalOrders - orderIdx;
            }

            while (orderIdx < totalOrders && route.size() < actualLimit) {
                route.add(sequence.get(orderIdx));
                orderIdx++;
            }
            solution.put(v.getLicensePlate(), route);
        }
        return solution;
    }

    private List<Order> crossover(List<Order> p1, List<Order> p2) {
        int size = p1.size();
        Order[] child = new Order[size];
        Random rand = new Random();

        int start = rand.nextInt(size);
        int end = rand.nextInt(size);

        for (int i = Math.min(start, end); i <= Math.max(start, end); i++) {
            child[i] = p1.get(i);
        }

        int childIdx = 0;
        for (Order o : p2) {
            if (!Arrays.asList(child).contains(o)) {
                while (childIdx < size && child[childIdx] != null)
                    childIdx++;
                if (childIdx < size)
                    child[childIdx] = o;
            }
        }
        return new ArrayList<>(Arrays.asList(child));
    }

    private void mutate(List<Order> individual) {
        if (Math.random() < MUTATION_RATE) {
            int i = (int) (Math.random() * individual.size());
            int j = (int) (Math.random() * individual.size());
            Collections.swap(individual, i, j);
        }
    }

    private List<List<Order>> initializePopulation(List<Order> orders) {
        List<List<Order>> pop = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Order> shuffled = new ArrayList<>(orders);
            Collections.shuffle(shuffled);
            pop.add(shuffled);
        }
        return pop;
    }

    private List<Order> selectParent(List<List<Order>> population) {
        return population.get(new Random().nextInt(10));
    }
}