package st;

import java.util.HashMap;
import java.util.Map;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

public class SootConnector {

    private static final String JDK_PATH = "C:\\Tools\\jdk\\jdk1.7.0_55\\jre\\lib\\rt.jar";
    private static Map<String, SootConnector> cache = new HashMap<>();

    public static SootConnector getInstance(final String packageName, final String className, final String classPathExtension) {
        System.out.println("-------Initializing SootConnection------");
        return getInstance(packageName, className, classPathExtension, false);
    }

    public static SootConnector getInstance(final String packageName, final String className, String classPathExtension, final boolean forceReload) {
        classPathExtension = classPathExtension.replace(',', ';');
        final String qualifiedName = packageName + "." + className;
        if (!cache.containsKey(qualifiedName) || forceReload) {
            final SootConnector instance = new SootConnector(packageName, className, classPathExtension);
            cache.put(qualifiedName, instance);
        }
        return cache.get(qualifiedName);
    }

    private final String packageName;
    private final String className;

    private SootConnector(final String packageName, final String className, final String classPathExtension) {
        this.packageName = packageName;
        this.className = className;
        init(classPathExtension);
    }

    @SuppressWarnings("static-access")
    private void init(final String classPathExtension) {
        final long startTime = System.nanoTime();
        soot.G.v()
                .reset(); // TODO really necessary? think about performance
        Options.v()
                .set_soot_classpath(JDK_PATH
                        // to fix couldn't find class: javax.crypto.SecretKey
                        // + ":/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jce.jar"
                        + ";C:\\Tools\\jdk\\jdk1.7.0_55\\jre\\lib\\jce.jar" + ";" + classPathExtension);
        Options.v()
                .set_src_prec(Options.src_prec_only_class);
        Options.v()
                .set_keep_line_number(true);
        Options.v()
                .set_whole_program(true); // important for interprocedural analysis
        // Options.v().set_verbose(true);
        final SootClass c = Scene.v()
                .loadClassAndSupport(packageName + "." + className);
        Scene.v()
                .loadNecessaryClasses();
        c.setApplicationClass();
        Scene.v()
                .setMainClass(c);

        PackManager.v()
                .getPack("cg")
                .apply(); // builds whole CallGraph
        final long endTime = System.nanoTime();
        System.out.println("Loading costs " + (endTime - startTime) + " ns, equal to " + (endTime - startTime) / 1000000000 + " s.");
    }

    public static void main(final String args[]) {
        final String packageName = "test";
        final String className = "Hello";
        final String pathExtensions = "C:\\Tools\\eclipse-oxygen\\workspace\\Studienprojekt\\bin\\";
        final String methodName = "testString";

        final SootConnector sc = SootConnector.getInstance(packageName, className, pathExtensions);
    }
}
