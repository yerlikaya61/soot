package st;

import java.util.Iterator;
import java.util.Map;

import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class AnalysisTransformer extends SceneTransformer {

    @Override
    protected void internalTransform(final String arg0, final Map arg1) {

        // Get Main Method
        final SootMethod sMethod = Scene.v()
                .getMainMethod();

        // Create graph based on the method
        if (sMethod.hasActiveBody()) {
            final UnitGraph graph = new BriefUnitGraph(sMethod.getActiveBody());
            // Print live variables at the entry and exit of each node

            final Iterator<Unit> unitIt = graph.iterator();

            while (unitIt.hasNext()) {
                final Unit s = unitIt.next();

                System.out.print(s);

                int d = 40 - s.toString()
                        .length();
                while (d > 0) {
                    System.out.print(".");
                    d--;
                }
            }
        }
    }
}
