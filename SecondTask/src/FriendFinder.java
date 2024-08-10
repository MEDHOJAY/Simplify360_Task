import java.util.Arrays;
import java.util.Scanner;

public class FriendFinder {
    private static String[][] friend;
    private static int totalPersons = 0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        friend = new String[10][];
        getFriends("Alice", scanner);
        getFriends("Bob", scanner);
        System.out.println("Alice's friends: ");
        displayFriends("Alice");
        System.out.println("Bob's friends: ");
        displayFriends("Bob");
        findCommonFriends("Alice", "Bob");
        System.out.print("Enter the first person for connection check: ");
        String person1 = scanner.nextLine().trim();
        System.out.print("Enter the second person for connection check: ");
        String person2 = scanner.nextLine().trim();
        int connectionLevel = findConnectionLevel(person1, person2);
        if (connectionLevel != -1) {
            System.out.println("Connection level between " + person1 + " and " + person2 + ": " + connectionLevel);
        } else {
            System.out.println("No connection found between " + person1 + " and " + person2);
        }
    }
    public static void getFriends(String personName, Scanner scanner) {
        System.out.print("Enter the number of " + personName + "'s friends: ");
        int numberOfFriends = Integer.parseInt(scanner.nextLine().trim());

        String[] friends = new String[numberOfFriends + 1];
        friends[0] = personName;  // First element is the person's name
        System.out.println("Enter " + personName + "'s friends:");
        for (int i = 0; i < numberOfFriends; i++) {
            friends[i + 1] = scanner.nextLine().trim();
        }
        addPerson(friends);
    }
    public static void displayFriends(String person) {
        String[] friends = findFriends(person);
        if (friends == null) {
            System.out.println(person + " has no friends listed.");
        } else {
            for (int i = 1; i < friends.length; i++) {
                System.out.print(friends[i] + " ");
            }
            System.out.println();
        }
    }
    public static void findCommonFriends(String person1, String person2) {
        String[] friends1 = findFriends(person1);
        String[] friends2 = findFriends(person2);

        if (friends1 == null || friends2 == null) {
            System.out.println("Common friends of " + person1 + " and " + person2 + ": None");
            return;
        }
        System.out.print("Common friends of " + person1 + " and " + person2 + ": ");
        boolean hasCommon = false;
        for (int i = 1; i < friends1.length; i++) {
            for (int j = 1; j < friends2.length; j++) {
                if (friends1[i].equals(friends2[j])) {
                    System.out.print(friends1[i] + " ");
                    hasCommon = true;
                }
            }
        }
        if (!hasCommon) {
            System.out.println("None");
        } else {
            System.out.println();
        }
    }
    public static int findConnectionLevel(String startPerson, String targetPerson) {
        if (startPerson.equals(targetPerson)) {
            return 0; // Same person
        }
        String[] queue = new String[100];
        int[] levels = new int[100];
        int front = 0, rear = 0;
        boolean[] visited = new boolean[totalPersons];
        int startPersonIndex = findPersonIndex(startPerson);
        if (startPersonIndex == -1) {
            return -1; // Start person not found
        }
        queue[rear] = startPerson;
        levels[rear++] = 0;
        visited[startPersonIndex] = true;
        while (front < rear) {
            String currentPerson = queue[front];
            int currentLevel = levels[front++];
            String[] neighbors = findFriends(currentPerson);
            if (neighbors != null) {
                for (int i = 1; i < neighbors.length; i++) {
                    if (neighbors[i].equals(targetPerson)) {
                        return currentLevel + 1;
                    }
                    int personIndex = findPersonIndex(neighbors[i]);
                    if (personIndex != -1 && !visited[personIndex]) {
                        visited[personIndex] = true;
                        queue[rear] = neighbors[i];
                        levels[rear++] = currentLevel + 1;
                    }
                }
            }
        }
        return -1; // No connection found
    }
    private static void addPerson(String[] friends) {
        if (totalPersons == friend.length) {
            friend = Arrays.copyOf(friend, friend.length * 2);
        }
        friend[totalPersons++] = friends;
    }
    private static String[] findFriends(String person) {
        for (int i = 0; i < totalPersons; i++) {
            if (friend[i][0].equals(person)) {
                return friend[i];
            }
        }
        return null;
    }
    private static int findPersonIndex(String person) {
        for (int i = 0; i < totalPersons; i++) {
            if (friend[i][0].equals(person)) {
                return i;
            }
        }
        return -1; // Person not found
    }
}
