
package st;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

public class Connector {

    public static void main(final String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java Connector class_to_analyse");
            System.exit(1);
        } else {
            System.out.println("Analyzing class: " + args[0]);
        }

        // set parameter as main class
        final String mainClass = args[0];
        
        final String separator = System.getProperty("path.separator");

        // set class path of soot, rt and jce files
        final String classPath = "." + separator
        						 + System.getProperty("user.dir") + "/bin/" + separator
                                 + System.getProperty("user.dir") + "/src/lib/soot-2.5.0.jar" + separator
                                 + System.getProperty("java.home") + "/lib/rt.jar" + separator
                                 + System.getProperty("java.home") + "/lib/jce.jar";        
        
        // set arguments for Soot
        final String[] sootArgs = { 
               // "-cp", classPath,             // Use path as the classpath for finding classes. 
                "-pp",                          // Prepend the given soot classpath to the default classpath.
                "-w",                           // Run in whole-program mode
                //"-src-prec", "java",            // Sets source precedence to format files 
                //"-main-class", mainClass,     // Sets the main class for whole-program analysis.
                "-f", "J",                      // Set output format for Soot to J=jimple 
                mainClass                       // https://soot-build.cs.uni-paderborn.de/public/origin/develop/soot/soot-develop/options/soot_options.htm
                };
        
        // Set Soot's internal classpath
        Options.v().set_soot_classpath(classPath);
        Options.v().set_src_prec(Options.src_prec_only_class);
        
        // Enable whole-program mode
        Options.v().set_whole_program(true);
        Options.v().set_app(true);

        // Call-graph options
        Options.v().setPhaseOption("cg", "safe-newinstance:true");
        Options.v().setPhaseOption("cg.cha","enabled:false");

        // Enable SPARK call-graph construction
        Options.v().setPhaseOption("cg.spark","enabled:true");
        Options.v().setPhaseOption("cg.spark","verbose:true");
        Options.v().setPhaseOption("cg.spark","on-fly-cg:true");

        // Set the main class of the application to be analysed
        Options.v().set_main_class(mainClass);

        // Load the main class
        final SootClass c = Scene.v().loadClassAndSupport(mainClass);        
        Scene.v().loadNecessaryClasses();        
        c.setApplicationClass();
        Scene.v().setMainClass(c);

        PackManager.v().getPack("cg").apply();
        
        // Call main function with arguments
        soot.Main.main(sootArgs);

    }

}
