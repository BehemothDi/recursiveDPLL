import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static Utils.Utilities.*;


public class Main {
    public static void main(String[] args) {

        ArrayList<String> inputList = new ArrayList<>();
        ArrayList<Double> timeList = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("src/largeInputs/"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> inputList.add(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputList.forEach(fileName -> {

            ArrayList<ArrayList<Integer>> function = loadFromFile(fileName);

            long startTime = System.nanoTime();
            boolean result = dpll(function);
            long endTime = System.nanoTime();

            double time = (endTime - startTime) / 1_000_000_000.0;
            timeList.add(time);

            System.out.print(inputList.indexOf(fileName)+1 + ": ");
            if (result) System.out.printf("SAT %.10f\n", time);
            else System.out.printf("UNSAT %.10f\n", time);
        });

        double time = timeList.stream().mapToDouble(tmeStamp -> tmeStamp).sum();
        System.out.printf("Full time: %.10f seconds\nAverage time: %.10f seconds", time, (time / timeList.size()));
    }

    static boolean dpll(ArrayList<ArrayList<Integer>> function) {
        unitPropagation(function);
       // pureLiterals(function);

        if (function.size() == 0) return true;
        else if (hasEmptyClause(function)) return false;

        int literal = chooseLiteral(function);

        return dpll(add(function, literal)) || dpll(add(function, -literal));
    }


}
