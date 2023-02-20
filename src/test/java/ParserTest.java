import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solvers.OutSequential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserTest {
    static final int X = GraphUtil.INF;

    @Test
    public void test_random_graph_20() {
        int[][] adjMatrix = {
                {5, X, X},
                {X, 5, X},
                {X, X, 5}
        };

        ArrayList<HashMap<Integer, Integer>> parsed = (ArrayList<HashMap<Integer, Integer>>) Parser.parse(adjMatrix);
        System.out.println(parsed);
    }

}
