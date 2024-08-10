import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TaskScheduler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of tasks: ");
        int numTasks = scanner.nextInt();
        int[] D = new int[numTasks];
        int[] EST = new int[numTasks];
        int[] EFT = new int[numTasks];
        int[] LST = new int[numTasks];
        int[] LFT = new int[numTasks];
        int[][] dependencies = new int[numTasks][];
        System.out.println("Enter the durations for each task:");
        for (int i = 0; i < numTasks; i++) {
            System.out.print("Task " + i + " duration: ");
            D[i] = scanner.nextInt();
        }
        System.out.println("Enter the dependencies for each task (space-separated, end with -1):");
        scanner.nextLine(); // Consume the leftover newline
        for (int i = 0; i < numTasks; i++) {
            System.out.print("Task " + i + " dependencies (space-separated, end with -1): ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            int[] taskDeps = new int[parts.length];
            for (int j = 0; j < parts.length; j++) {
                taskDeps[j] = Integer.parseInt(parts[j]);
            }
            dependencies[i] = taskDeps;
        }
        for (int i = 0; i < numTasks; i++) {
            EST[i] = 0;
            EFT[i] = 0;
            LST[i] = Integer.MAX_VALUE;
            LFT[i] = Integer.MAX_VALUE;
        }
        calculateEarliestTimes(numTasks, D, dependencies, EST, EFT);
        calculateLatestTimes(numTasks, D, dependencies, EST, EFT, LST, LFT);
        int earliestCompletionTime = 0;
        int latestCompletionTime = 0;
        for (int i = 0; i < numTasks; i++) {
            if (EFT[i] > earliestCompletionTime) {
                earliestCompletionTime = EFT[i];
            }
            if (LFT[i] > latestCompletionTime) {
                latestCompletionTime = LFT[i];
            }
        }
        System.out.println("Earliest time all tasks will be completed: " + earliestCompletionTime);
        System.out.println("Latest time all tasks will be completed: " + latestCompletionTime);
    }
    static void calculateEarliestTimes(int numTasks, int[] D, int[][] dependencies, int[] EST, int[] EFT) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[numTasks];
        queue.add(0);
        visited[0] = true;
        while (!queue.isEmpty()) {
            int currentTask = queue.poll();
            int currentEST = EST[currentTask];
            int currentEFT = currentEST + D[currentTask];
            EFT[currentTask] = currentEFT;
            for (int i = 0; i < numTasks; i++) {
                if (!visited[i] && isDependent(currentTask, i, dependencies)) {
                    EST[i] = Math.max(EST[i], currentEFT);
                    queue.add(i);
                    visited[i] = true;
                }
            }
        }
    }
    static void calculateLatestTimes(int numTasks, int[] D, int[][] dependencies, int[] EST, int[] EFT, int[] LST, int[] LFT) {
        int projectEndTime = 0;
        // Find the maximum EFT to set as initial LFT
        for (int i = 0; i < numTasks; i++) {
            if (EFT[i] > projectEndTime) {
                projectEndTime = EFT[i];
            }
        }
        for (int i = 0; i < numTasks; i++) {
            LFT[i] = projectEndTime;
        }
        boolean[] visited = new boolean[numTasks];
        Queue<Integer> queue = new LinkedList<>();
        // Start from tasks with no dependencies
        for (int i = 0; i < numTasks; i++) {
            if (dependencies[i].length == 0) {
                queue.add(i);
                visited[i] = true;
            }
        }
        while (!queue.isEmpty()) {
            int currentTask = queue.poll();
            int currentLFT = LFT[currentTask];
            int currentLST = currentLFT - D[currentTask];
            LST[currentTask] = currentLST;
            for (int i = 0; i < numTasks; i++) {
                if (!visited[i] && isDependent(i, currentTask, dependencies)) {
                    LFT[i] = Math.min(LFT[i], currentLST);
                    queue.add(i);
                    visited[i] = true;
                }
            }
        }
    }
    static boolean isDependent(int a, int b, int[][] dependencies) {
        for (int dep : dependencies[b]) {
            if (dep == a) {
                return true;
            }
        }
        return false;
    }
}