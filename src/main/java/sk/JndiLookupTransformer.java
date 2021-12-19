package sk;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.scopedpool.ScopedClassPoolFactoryImpl;
import javassist.scopedpool.ScopedClassPoolRepositoryImpl;
import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JndiLookupTransformer implements ClassFileTransformer {

    private ScopedClassPoolFactoryImpl scopedClassPoolFactory = new ScopedClassPoolFactoryImpl();

    private ClassPool rootPool = ClassPool.getDefault();

    private final String jndiClassName;

    JndiLookupTransformer(String jndiClassName) {
        this.jndiClassName = jndiClassName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        if (className.equals(this.jndiClassName)) {
            try {
                ClassPool classPool = scopedClassPoolFactory.create(loader, rootPool,
                        ScopedClassPoolRepositoryImpl.getInstance());
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod[] methods = ctClass.getDeclaredMethods();

                for (CtMethod method : methods) {
                    if (method.getName().equals("lookup")){
                        System.out.println("Transforming JndiLookup.lookup method");
                        method.insertBefore("return null;");
                    }
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (Throwable ex) {
                System.out.println("Error in transforming the class: " + className + " " + ex);
            }
        }
        return byteCode;
    }
}
