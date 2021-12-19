package sk;

import java.lang.instrument.Instrumentation;

public class Log4jAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) throws InstantiationException {

        System.out.println("Starting Java Agent");
        final String jndiClassName = "org/apache/logging/log4j/core/lookup/JndiLookup";

        JndiLookupTransformer xformer = new JndiLookupTransformer(jndiClassName);
        instrumentation.addTransformer(xformer, true);

        for(Class<?> clas: instrumentation.getAllLoadedClasses()) {
            if(clas.getName().equals(jndiClassName)) {
                try {
                    instrumentation.retransformClasses(clas);
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }
}
