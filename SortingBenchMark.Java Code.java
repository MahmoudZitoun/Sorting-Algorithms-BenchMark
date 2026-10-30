
package com.mycompany.sortingbenchmark;
import java.util.Arrays;
import java.util.Random;

public class SortingBenchMark {

    // Input size N = 100,000
    private static final int SIZE = 100000;
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        System.out.println("Generating datasets and benchmarking sorting algorithms...");
        System.out.println("Please wait, this may take a few seconds due to N = 100,000 elements...\n");

        // 1. Generate four test cases
        int[] originalRandom = generateRandomArray(SIZE);
        int[] originalReversed = generateReversedArray(originalRandom);
        int[] originalPartial = generatePartiallySortedArray(originalRandom);
        int[] originalFullySorted = generateFullySortedArray(originalRandom);

        String[] algorithms = {"Selection Sort", "Insertion Sort", "Bubble Sort", "Merge Sort", "Quick Sort"};
        
        // 2D Array to hold results: 5 algorithms x 4 test cases
        long[][] results = new long[5][4];

        // 2. Run benchmarks for each algorithm across all test cases
        for (int i = 0; i < algorithms.length; i++) {
            results[i][0] = benchmarkAlgorithm(algorithms[i], originalRandom);
            results[i][1] = benchmarkAlgorithm(algorithms[i], originalReversed);
            results[i][2] = benchmarkAlgorithm(algorithms[i], originalPartial);
            results[i][3] = benchmarkAlgorithm(algorithms[i], originalFullySorted);
        }

        // 3. Print the formatted output table
        printTable(algorithms, results);
    }

    // Measures execution time in microseconds
    private static long benchmarkAlgorithm(String algoName, int[] sourceArray) {
        int[] testArray = Arrays.copyOf(sourceArray, sourceArray.length);
        
        long startTime = System.nanoTime();
        
        switch (algoName) {
            case "Selection Sort":
                selectionSort(testArray);
                break;
            case "Insertion Sort":
                insertionSort(testArray);
                break;
            case "Bubble Sort":
                bubbleSort(testArray);
                break;
            case "Merge Sort":
                mergeSort(testArray, 0, testArray.length - 1);
                break;
            case "Quick Sort":
                quickSort(testArray, 0, testArray.length - 1);
                break;
        }
        
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000; // Convert nanoseconds to microseconds
    }

    // --- 1. Selection Sort ---
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    // --- 2. Insertion Sort ---
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // --- 3. Bubble Sort ---
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    // --- 4. Merge Sort ---
    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // --- 5. Quick Sort (Optimized with Random Pivot to prevent StackOverflow) ---
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        // Pick a random index and swap with high to act as pivot
        int randomIndex = low + RAND.nextInt(high - low + 1);
        int temp = arr[randomIndex];
        arr[randomIndex] = arr[high];
        arr[high] = temp;

        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = swapTemp;
        return i + 1;
    }

    // --- Data Generation Helpers ---
    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        return arr;
    }

    private static int[] generateReversedArray(int[] source) {
        int[] arr = Arrays.copyOf(source, source.length);
        Arrays.sort(arr);
        int[] reversed = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }

    private static int[] generatePartiallySortedArray(int[] source) {
        int[] arr = Arrays.copyOf(source, source.length);
        Arrays.sort(arr);
        Random rand = new Random();
        int swaps = (int) (arr.length * 0.2); // Swap 20% to make it ~80% sorted
        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(arr.length);
            int idx2 = rand.nextInt(arr.length);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        return arr;
    }

    private static int[] generateFullySortedArray(int[] source) {
        int[] arr = Arrays.copyOf(source, source.length);
        Arrays.sort(arr);
        return arr;
    }

    // --- Output Table Print Function ---
    private static void printTable(String[] algorithms, long[][] results) {
        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s | %-18s | %-18s | %-18s | %-18s\n", "Algorithm", "Random", "Reversely Sorted", "Partially Sorted", "Fully Sorted");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (int i = 0; i < algorithms.length; i++) {
            System.out.printf("%-20s | %-15d μs | %-15d μs | %-15d μs | %-15d μs\n", 
                algorithms[i], results[i][0], results[i][1], results[i][2], results[i][3]);
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }
}