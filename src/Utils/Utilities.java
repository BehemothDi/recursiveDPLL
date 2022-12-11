package Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utilities {


    /**
     * Returns a function which is a deep copy of a given function.
     * @param function function to copy
     * @return new function
     */
    public static ArrayList<ArrayList<Integer>> copyFunction(ArrayList<ArrayList<Integer>> function){
        ArrayList<ArrayList<Integer>> newFunction = new ArrayList<>();
        function.forEach(array -> newFunction.add(new ArrayList<>(array)));
        return newFunction;
    }

    /**
     * Checks whether given function has a unit clause or not.
     * This method returns an int value which is a unit clause index.
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
     */
    public static void unitPropagation(ArrayList<ArrayList<Integer>> function) {
        int unitClauseIndex = getUnitClauseIndex(function);

        while (getUnitClauseIndex(function) != -1) {
            processUnitClause(function, unitClauseIndex);
            unitClauseIndex = getUnitClauseIndex(function);
        }
    }

    /**
     * Returns a function without unit clause with given index.
     * Also removes all arrays which have unit clause's literal
     * and all reverse literals from all clauses.
     * @param function function to process
     * @param index index of a unit clause in a given function

     */
    public static void processUnitClause(ArrayList<ArrayList<Integer>> function, int index){
        assert index > 0;

        int unitLiteral = function.get(index).get(0);
        function.removeIf(array -> array.contains(unitLiteral));
        function.forEach(array -> array.removeAll(Collections.singleton(-unitLiteral)));
    }

    /**
     * Returns a literal from a function.
     * @param function boolean function
     * @return literal
     */
    public static Integer chooseLiteral(ArrayList<ArrayList<Integer>> function) {
        return  function.get(0).get(0);
    }

    /**
     * Returns true if function has empty clause and false otherwise.
     * @param function function to check
     * @return boolean value
     */
    public static boolean hasEmptyClause(ArrayList<ArrayList<Integer>> function) {
        for (ArrayList<Integer> array :
                function) {
            if (array.size() == 0) return true;
        }
        return false;
    }

    /***
     * Returns new function with a new unit clause which consists of a given literal.
     * @param function ArrayList<ArrayList<Integer>>
     * @param literal int
     * @return new function
     */
    public static ArrayList<ArrayList<Integer>> add(ArrayList<ArrayList<Integer>> function, int literal) {
        ArrayList<ArrayList<Integer>> resultFunction = copyFunction(function);
        resultFunction.add(new ArrayList<>(Collections.singleton(literal)));
        return resultFunction;
    }

    /**
     * Returns a function loaded from a file.
     * @param fileName String
     * @return new function
     */
    public static ArrayList<ArrayList<Integer>> loadFromFile(String fileName){

        List<String> lines;
        ArrayList<ArrayList<Integer>> function = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            lines.removeIf(line -> line.length() == 0);
            lines.removeIf(line ->
                    line.charAt(0) == 'c' ||
                    line.charAt(0) == 'p' ||
                    line.charAt(0) == '%' ||
                    line.charAt(0) == '0');
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

    /**
     * Returns a function without pure clauses which had pure literals.
     * @param function function to process
     * @return modified function
     */
    public static ArrayList<ArrayList<Integer>> pureLiterals(ArrayList<ArrayList<Integer>> function) {

        HashSet<Integer> literals = new HashSet<>();
        HashSet<Integer> pureLiterals = new HashSet<>();

        function.forEach(literals::addAll);
        literals.forEach(l -> {if (!literals.contains(-l)) pureLiterals.add(l);});
        pureLiterals.forEach(literal -> function.removeIf(clause -> clause.contains(literal)));

        return function;
    }


}
