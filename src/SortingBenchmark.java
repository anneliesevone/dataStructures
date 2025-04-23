import java.util.*;

public class SortingBenchmark {

    public static void main(String[] args) {
        int[] sizes = {100, 1000, 10000};
        String[] patterns = {"random", "reverse", "nearlySorted"};

        for (int size : sizes) {
            for (String pattern : patterns) {
                int[] base = generateData(size, pattern);
                System.out.printf("--- Size: %d | Pattern: %s ---%n", size, pattern);

                benchmark("TreapSort", base.clone(), SortingBenchmark::treapSort);
                benchmark("PQSort", base.clone(), SortingBenchmark::pqSort);
                benchmark("TimSort", base.clone(), SortingBenchmark::timSort);
                benchmark("QuickSort", base.clone(), SortingBenchmark::quickSort);
                benchmark("MergeSort", base.clone(), SortingBenchmark::mergeSort);

                System.out.println("-------------------------------------------------");
            }
        }
    }

    interface Sorter {
        void sort(int[] arr);
    }

    static void benchmark(String name, int[] arr, Sorter sorter) {
        long start = System.nanoTime();
        sorter.sort(arr);
        long duration = System.nanoTime() - start;
        System.out.printf("%s: %.2f ms%n", name, duration / 1e6);
    }

    static void treapSort(int[] arr) {
        TreapMap<Integer, Integer> treap = new TreapMap<>();
        for (int x : arr) treap.put(x, x);
        int i = 0;
        for (Integer key : treap.keySet()) arr[i++] = key;
    }

    static void pqSort(int[] arr) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : arr) pq.add(x);
        for (int i = 0; i < arr.length; i++) arr[i] = pq.poll();
    }

    static void timSort(int[] arr) {
        Arrays.sort(arr);
    }

    static void quickSort(int[] arr) {
        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);
    }

    static void mergeSort(int[] arr) {
        SortingAlgorithms.mergeSort(arr, 0, arr.length - 1);
    }

    static int[] generateData(int size, String pattern) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = i;

        switch (pattern) {
            case "random" -> {
                Random rand = new Random();
                for (int i = size - 1; i > 0; i--) {
                    int j = rand.nextInt(i + 1);
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
            case "reverse" -> {
                for (int i = 0; i < size; i++) arr[i] = size - i;
            }
            case "nearlySorted" -> {
                for (int i = 0; i < size; i++) arr[i] = i;
                Random rand = new Random();
                for (int i = 0; i < size / 10; i++) {
                    int a = rand.nextInt(size);
                    int b = rand.nextInt(size);
                    int tmp = arr[a];
                    arr[a] = arr[b];
                    arr[b] = tmp;
                }
            }
        }
        return arr;
    }
}