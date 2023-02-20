package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.BellmanFordSolver;
import cse332.graph.GraphUtil;
import main.Parser;

import java.util.*;

public class OutSequential implements BellmanFordSolver {

    //sets everything to +inf except source to 0
    private void initializeArrays(int source, int[] D1, int[] D2, int[] P) {
        for (int i = 0; i < D1.length; i++) {
            if (i == source) {
                D1[i] = 0;
                D2[i] = 0;
            } else {
                D1[i] = Integer.MAX_VALUE;
                D2[i] = Integer.MAX_VALUE;
            }

            P[i] = -1;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<HashMap<Integer, Integer>> parsed = (ArrayList<HashMap<Integer, Integer>>) Parser.parse(adjMatrix);

        int numVertices = adjMatrix.length;

        int[] D1 = new int[numVertices];
        int[] D2 = new int[numVertices];
        int[] P = new int[numVertices];
        initializeArrays(source, D1, D2, P);

        // Step 2 of Bellman Ford
        for (int n = 0; n < numVertices; n++) {
            // array copying
            for (int v = 0; v < numVertices; v++) {
                D2[v] = D1[v];
            }

            // relaxing edges BFS style :)
            for (int v = 0; v < numVertices; v++) {
                Set<Integer> neighbors = parsed.get(v).keySet();
                Iterator<Integer> itr = neighbors.iterator();

                while (itr.hasNext()) {
                    int neighbor = itr.next();
                    Integer cost = parsed.get(v).get(neighbor);

                    Integer edgeCost;
                    if (D2[v] == Integer.MAX_VALUE) {
                        edgeCost = Integer.MAX_VALUE;
                    } else {
                        edgeCost = D2[v]+cost;
                    }

                    if (D1[neighbor] > edgeCost) {
                        D1[neighbor] = edgeCost;
                        P[neighbor] = v;
                    }
                }
            }
        }

        // find negative cost cycles
        return GraphUtil.getCycle(P);
    }

}