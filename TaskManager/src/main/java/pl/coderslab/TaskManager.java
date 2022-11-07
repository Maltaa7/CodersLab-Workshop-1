package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
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
        File file = new File(filename);
        String[] tasks = new String[0];

        if (!file.exists()) {
            System.out.print("No tasks list file!");
            System.exit(0);
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                tasks = ArrayUtils.add(tasks, scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.print("No tasks list file!");
        }

        String[][] tasksArrResult = new String[tasks.length][];
        for (int i = 0; i < tasks.length; i++) {
            tasksArrResult[i] = tasks[i].split(",");
        }

        return tasksArrResult;
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
                    printArray(tasks);
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

    public static void printArray(String[][] array) {
        StringBuilder arraySB = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            arraySB.append(i).append(" : ");
            for (String s : array[i]) {
                arraySB.append(s).append(" ");
            }
            arraySB.append("\n");
        }
        System.out.println(arraySB);
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


