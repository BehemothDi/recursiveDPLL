package Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utilities {


    /**
     * Returns a function which is a deepcopy of a given function.
     * @param function function to copy
     * @return new function
     */
    public static ArrayList<ArrayList<Integer>> copyFunction(ArrayList<ArrayList<Integer>> function){
        ArrayList<ArrayList<Integer>> newFunction = new ArrayList<>();
        function.forEach(array -> newFunction.add(new ArrayList<>(array)));
        return newFunction;
    }


    /**
     * Checks whether given function has an unit clause or not.
     * This method returns an int value which is an unit clause index.
     * If no unit clause has found returns -1.
     * @param function function to check
     * @return int value
     */
    public static int getUnitClauseIndex(ArrayList<ArrayList<Integer>> function) {
        for (ArrayList<Integer> array : function
             ) {
            if (array.size() == 1) return function.indexOf(array);
        }
        return -1;
    }

    /**
     * Returns new function without unit clauses.
     * Copies input function and modifies the copy.
     * @param function function to process
     * @return new function
     */
    public static ArrayList<ArrayList<Integer>> unitPropagation(ArrayList<ArrayList<Integer>> function) {
        int unitClauseIndex = getUnitClauseIndex(function);

        while (getUnitClauseIndex(function) != -1) {
            processUnitClause(function, unitClauseIndex);
            unitClauseIndex = getUnitClauseIndex(function);
        }

        return function;
    }

    /**
     * Returns a function without unit clause with given index.
     * Also removes all arrays which have unit clause's literal
     * and all reverse literals from all clauses.
     * @param function function to process
     * @param index index of an unit clause in a given function
     * @return modified input function
     */
    public static ArrayList<ArrayList<Integer>> processUnitClause(ArrayList<ArrayList<Integer>> function, int index){
        if (index < 0) return function;

        int unitLiteral = function.get(index).get(0);

        function.removeIf(array -> array.contains(unitLiteral));
        function.forEach(array -> array.removeAll(Collections.singleton(-unitLiteral)));

        return function;
    }


    public static Integer chooseLiteral(ArrayList<ArrayList<Integer>> function) {
        return  function.get(0).get(0);
    }

    public static boolean hasEmptyClause(ArrayList<ArrayList<Integer>> function) {
        for (ArrayList<Integer> array :
                function) {
            if (array.size() == 0) return true;
        }
        return false;
    }

    public static ArrayList<ArrayList<Integer>> add(ArrayList<ArrayList<Integer>> function, int literal) {
        ArrayList<ArrayList<Integer>> resultFunction = new ArrayList<>();
        resultFunction = copyFunction(function);
        resultFunction.add(new ArrayList<>(Collections.singleton(literal)));
        return resultFunction;
    }


    public static ArrayList<ArrayList<Integer>> loadFromFile(String fileName){

        List<String> lines;
        ArrayList<ArrayList<Integer>> function = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            lines.remove(0);
        } catch (IOException e) {
            System.out.println("Wrong file.");
            throw new RuntimeException(e);
        }

        lines.forEach(line -> {

            ArrayList<Integer> clause  = Stream.of(line.trim().split("\\s+")).map(String::trim).map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
            clause.remove(clause.size() - 1);

            function.add(clause);

        });

        return function;
    }

}
