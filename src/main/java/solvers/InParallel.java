package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxInTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InParallel implements BellmanFordSolver {

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

    public List<Integer> solve(int[][] adjMatrix, int source) {
        ArrayList<HashMap<Integer, Integer>> parsed = (ArrayList<HashMap<Integer, Integer>>) Parser.parseInverse(adjMatrix);

        int numVertices = adjMatrix.length;

        int[] D1 = new int[numVertices];
        int[] D2 = new int[numVertices];
        int[] P = new int[numVertices];
        initializeArrays(source, D1, D2, P);

        // Step 2 of Bellman Ford
        for (int n = 0; n < numVertices; n++) {
            // array copying
            D2 = ArrayCopyTask.copy(D1);

            // be sure to iterate from 0 to numVertices (parallelize the vertices)
            RelaxInTask.parallel(D1, D2, P, parsed);
        }

        // find negative cost cycles
        return GraphUtil.getCycle(P);
    }

}