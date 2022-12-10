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

        try (Stream<Path> paths = Files.walk(Paths.get("src/inputs/"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> inputList.add(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        inputList.forEach(fileName -> {

            ArrayList<ArrayList<Integer>> function = loadFromFile(fileName);

            boolean result = dpll(function);

            System.out.print(inputList.indexOf(fileName)+1 + ": ");
            if (result) System.out.println("SAT");
            else System.out.println("UNSAT");
        });

    }

    static boolean dpll(ArrayList<ArrayList<Integer>> function) {
        unitPropagation(function);

        if (function.size() == 0) return true;
        else if (hasEmptyClause(function)) return false;

        int literal = chooseLiteral(function);

        return dpll(add(function, literal)) || dpll(add(function, -literal));
    }


}
