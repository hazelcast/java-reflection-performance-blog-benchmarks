package benchmark;

import org.openjdk.jmh.annotations.*;
import java.lang.invoke.*;
import java.lang.reflect.Method;

public class PrimitiveReflectiveAccessBenchmark extends AbstractReflectiveAccessBenchmark {
    private Method method;
    private MethodHandle methodHandle;

    @Setup
    public void setup() {
        try {
            method = source.getClass().getDeclaredMethod("getPrimitive");
            methodHandle = MethodHandles.lookup().unreflect(method);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public long direct() throws Throwable {
        return source.getPrimitive();
    }

    @Benchmark
    public long reflection() throws ReflectiveOperationException {
        return ((Long) method.invoke(source)).longValue();
    }

    @Benchmark
    public long methodHandleInvoke() throws Throwable {
        return ((Long) methodHandle.invoke(source)).longValue();
    }

    @Benchmark
    public long methodHandleInvokeExact() throws Throwable {
        return (long) methodHandle.invokeExact(source);
    }
}
