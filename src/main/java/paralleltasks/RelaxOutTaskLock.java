package paralleltasks;

import java.util.concurrent.locks.ReentrantLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private int lo;
    private int hi;
    private int[] D1;
    private int[] D2;
    private int[] P;
    private ArrayList<HashMap<Integer, Integer>> parsed;
    private ReentrantLock[] locks;

    public RelaxOutTaskLock(int lo, int hi, int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed, ReentrantLock[] locks) {
        this.lo = lo;
        this.hi = hi;
        this.D1 = D1;
        this.D2 = D2;
        this.P = P;
        this.parsed = parsed;
        this.locks = locks;
    }

    protected void compute() {
        if (hi-lo <= CUTOFF) {
            sequential(lo, hi, D1, D2, P, parsed, locks);
            return;
        }

        int mid = lo + (hi-lo)/2;

        RelaxOutTaskLock left = new RelaxOutTaskLock(lo, mid, D1, D2, P, parsed, locks);
        RelaxOutTaskLock right = new RelaxOutTaskLock(mid, hi, D1, D2, P, parsed, locks);

        left.fork();
        right.compute();
        left.join();
    }

    public static void sequential(int lo, int hi, int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed, ReentrantLock[] locks) {
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

                // ** critical section, lock neighbor
                locks[v].lock();

                if (edgeCost < D1[neighbor]) {
                    D1[neighbor] = edgeCost;
                    P[neighbor] = v;
                }

                locks[v].unlock();
                // ** critical section
            }
        }
    }

    public static void parallel(int[] D1, int[] D2, int[] P, ArrayList<HashMap<Integer, Integer>> parsed) {
        ReentrantLock[] locks = new ReentrantLock[D1.length];

        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }

        pool.invoke(new RelaxOutTaskLock( 0, D1.length, D1, D2, P, parsed, locks));
    }
}
