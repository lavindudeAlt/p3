package main;

import cse332.exceptions.NotYetImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list
     */
    public static Object parse(int[][] adjMatrix) {
        // turn into array of hashmaps<vertex, edge cost>

        ArrayList<HashMap<Integer, Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < adjMatrix.length; i++) {
            adjList.add(new HashMap<Integer, Integer>());

            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    adjList.get(i).put(j, adjMatrix[i][j]);
                }
            }
        }

        return adjList;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list with incoming edges
     */
    public static Object parseInverse(int[][] adjMatrix) {
        ArrayList<HashMap<Integer, Integer>> adjList = new ArrayList<>();

        int n = adjMatrix.length; // dimensions of this square matrix

        for (int j = 0; j < n; j++) {
            adjList.add(new HashMap<>());

            for (int i = 0; i < n; i++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    adjList.get(j).put(i, adjMatrix[i][j]);
                }
            }
        }

        return adjList;
    }

}
