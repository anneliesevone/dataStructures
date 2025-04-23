import java.util.*;

public class Benchmark {

    public static void main(String[] args) {
        int[] sizes = {100, 1_000, 10_000};
        String[] patterns = {"random", "sorted", "reverse", "partial"};

        for (int size : sizes) {
            for (String pattern : patterns) {
                int[] data = generateData(size, pattern);
                System.out.printf("--- Size: %d | Pattern: %s ---%n", size, pattern);
                benchmark("Treap", new TreapMap<>(), data);
                benchmark("AVLTree", new AVLTree<>(), data);
                benchmark("TreeMap", new TreeMap<>(), data);
                System.out.println("-------------------------------------------------");
            }
        }
    }

    static int[] generateData(int size, String pattern) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) data[i] = i;

        switch (pattern) {
            case "random" -> {
                Random rand = new Random();
                for (int i = 0; i < size; i++) {
                    int j = rand.nextInt(size);
                    int temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
            case "reverse" -> {
                for (int i = 0; i < size; i++) data[i] = size - i;
            }
            case "sorted" -> Arrays.sort(data);
            case "partial" -> {
                Arrays.sort(data);
                for (int i = size - size / 10; i < size; i++) {
                    data[i] = size + i;
                }
            }
        }
        return data;
    }

    static void benchmark(String label, Map<Integer, Integer> map, int[] data) {
        int size = data.length;

        // Insertion
        long start = System.nanoTime();
        for (int x : data) map.put(x, x);
        long insertTime = System.nanoTime() - start;

        // Successful get
        start = System.nanoTime();
        for (int x : data) map.get(x);
        long getSuccessTime = System.nanoTime() - start;

        // Unsuccessful get
        start = System.nanoTime();
        for (int x : data) map.get(x + 100_000);
        long getFailTime = System.nanoTime() - start;

        // In-order traversal
        start = System.nanoTime();
        for (int x : map.keySet()) {}
        long traversalTime = System.nanoTime() - start;

        // Deletion
        start = System.nanoTime();
        for (int x : data) map.remove(x);
        long deleteTime = System.nanoTime() - start;

        System.out.printf("%s | Insert: %.2f ms | Get✓: %.2f ms | Get✗: %.2f ms | Traverse: %.2f ms | Delete: %.2f ms%n",
                label,
                insertTime / 1e6,
                getSuccessTime / 1e6,
                getFailTime / 1e6,
                traversalTime / 1e6,
                deleteTime / 1e6
        );
    }
}