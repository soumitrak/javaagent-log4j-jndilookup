package sk;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.lang.instrument.Instrumentation;

public class Log4jAgent {

    final static String jndiClassName = "org.apache.logging.log4j.core.lookup.JndiLookup";

    private static void amain(Instrumentation instrumentation) {
        System.out.println("Starting Java Agent");

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

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        amain(instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        amain(instrumentation);
    }

    public static void main(String[] args) throws Exception {
        if(args.length >= 2) {
            System.out.println("Attaching agent to PID " + args[1]);
            VirtualMachine vm = VirtualMachine.attach(args[1]);
            System.out.println("Loading agent file " + args[0]);
            File agentFile = new File(args[0]);
            vm.loadAgent(agentFile.getAbsolutePath());
            vm.detach();
        } else {
            System.out.println("Usage: <jvm agent path> <PID to attach to>");
        }
    }
}
