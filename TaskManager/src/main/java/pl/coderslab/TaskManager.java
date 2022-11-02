package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static final String TASKS_FILENAME = "tasks.csv";
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadToArray(TASKS_FILENAME);
        displayOptions(OPTIONS);
        performSelectedActions();

    }

    public static void displayOptions(String[] arr) {
        System.out.println(ConsoleColors.BLUE + "Please select an option" + ConsoleColors.RESET);
        for (String option : arr) {
            System.out.println(option);
        }
    }


    public static String[][] loadToArray(String filename) {
        Path filePath = Paths.get(filename);
        String[][] tasks = null;

        if (Files.exists(filePath)) {
            try (Scanner scanner = new Scanner(filePath)) {
                int countTasks = (int) Files.lines(filePath).count();
                tasks = new String[countTasks][];
                int counter = 0;
                while (scanner.hasNextLine()) {
                    tasks[counter] = scanner.nextLine().split(", ");
                    counter++;
                }
            } catch (IOException e) {
                System.out.print("No file");
                e.printStackTrace();
            }
        }

        return tasks;
    }


    private static void performSelectedActions() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.next();
            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    list();
                    break;
                case "exit":
                    saveArrayToFile(tasks, TASKS_FILENAME);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a correct option");
            }

            displayOptions(OPTIONS);
        }

    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String deadline = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        while (!scanner.hasNextBoolean()) {
            System.out.println("Please select a correct option: true/false");
            scanner.next();
        }
        String priority = Boolean.toString(scanner.nextBoolean());

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = deadline;
        tasks[tasks.length - 1][2] = priority;
    }

    private static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove");
        while (true) {
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                if (number >= 0) {
                    try {
                        tasks = ArrayUtils.remove(tasks, number);
                        System.out.println("Value was successfully deleted.");
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Data not removed. There is no number " + number + " task.");
                    }
                    break;
                }
            }
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.next();
        }
    }

    private static void list() {
        StringBuilder tasksList = new StringBuilder();
        for (int i = 0; i < tasks.length; i++) {
            tasksList.append(i).append(" : ");
            for (String s : tasks[i]) {
                tasksList.append(s).append(" ");
            }
            tasksList.append("\n");
        }
        System.out.println(tasksList);
    }

    public static void saveArrayToFile(String[][] array, String fileName) {
        Path path = Paths.get(fileName);
        List<String> outList = new ArrayList<>();
        for (String[] strings : array) {
            outList.add(String.join(", ", strings));
        }
        try {
            Files.write(path, outList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}


