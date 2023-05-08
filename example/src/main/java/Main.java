// javac Main.java
// java -XX:-Inline -XX:-TieredCompilation Main 36 8000000

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public interface I1 {}

    public interface I2 {}

    public interface I3 extends I1, I2 {}

    public static class B implements I3 {}

    public static class C implements I3 {}

    public static void main(String[] args) {
        int numThreads = Integer.parseInt(args[0]);
        int loopCount = Integer.parseInt(args[1]);
        ExecutorService es = Executors.newFixedThreadPool(numThreads);
        I3 b = new B();
        I3 c = new C();
        for (int i = 0; i != numThreads; i++) {
            es.submit(() -> {
                for (int j = 0; j != loopCount; j++) {
                    foo(b);
                    foo(c);
                    goo(b);
                    goo(c);
                }
            });
        }
        es.shutdown();
    }

    public static boolean foo(I3 i) {
        return i instanceof I1;
    }

    public static boolean goo(I3 i) {
        return i instanceof I2;
    }
}
