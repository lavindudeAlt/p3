package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private int lo;
    private int hi;
    private int[] D1;
    private int[] D2;
    private int[] P;
    private ArrayList<HashMap<Integer, Integer>> parsed;
    public RelaxOutTaskBad(int lo, int hi, int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed) {
        this.lo = lo;
        this.hi = hi;
        this.D1 = D1;
        this.D2 = D2;
        this.P = P;
        this.parsed = parsed;
    }

    protected void compute() {
        if (hi-lo <= CUTOFF) {
            sequential(lo, hi, D1, D2, P, parsed);
            return;
        }

        int mid = lo + (hi-lo)/2;

        RelaxOutTaskBad left = new RelaxOutTaskBad(lo, mid, D1, D2, P, parsed);
        RelaxOutTaskBad right = new RelaxOutTaskBad(mid, hi, D1, D2, P, parsed);

        left.fork();
        right.compute();
        left.join();
    }

    public static void sequential(int lo, int hi, int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed) {
        for (int v = lo; v < hi; v++) {
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

    public static void parallel(int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed) {
        pool.invoke(new RelaxOutTaskBad( 0, D1.length, D1, D2, P, parsed));
    }

}
