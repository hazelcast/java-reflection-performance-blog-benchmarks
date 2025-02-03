package benchmark;

/** Some dummy class to query metrics for using reflection */
class SomeSource {
    private static long counter = Long.MAX_VALUE;

    long getPrimitive() {
        // Not thread-safe but not relevant for our purposes
        return counter--;
    }

    Object getObject() {
        return getPrimitive();
    }
}