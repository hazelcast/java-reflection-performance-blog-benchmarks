package benchmark;

import org.openjdk.jmh.annotations.*;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ReferenceReflectiveAccessBenchmark extends AbstractReflectiveAccessBenchmark {
    private Method method;
    private MethodHandle methodHandle;
    private Function<SomeSource, ?> lambdaMetafactoryFunction;

    @Setup
    public void setup() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();

            method = source.getClass().getDeclaredMethod("getObject");

            methodHandle = lookup.unreflect(method);

            lambdaMetafactoryFunction = (Function<SomeSource, ?>) LambdaMetafactory
                    .metafactory(lookup, "apply", MethodType.methodType(Function.class),
                            MethodType.methodType(Object.class, Object.class), methodHandle, methodHandle.type())
                    .getTarget().invokeExact();
        } catch (final Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    @Benchmark
    public Object direct() throws Throwable {
        return source.getObject();
    }

    @Benchmark
    public Object reflection() throws ReflectiveOperationException {
        return method.invoke(source);
    }

    @Benchmark
    public Object methodHandle() throws Throwable {
        return methodHandle.invoke(source);
    }

    @Benchmark
    public Object lambdaMetafactory() {
        return lambdaMetafactoryFunction.apply(source);
    }
}
