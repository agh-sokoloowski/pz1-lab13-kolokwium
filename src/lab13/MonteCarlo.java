package lab13;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class MonteCarlo {
    static BlockingQueue<Double> results = new ArrayBlockingQueue<>(100);
    static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        integrateSequential(100);
        integrateParallel(100, 100);
    }

    static void integrateSequential(int n) {
        MCThread mcThread = new MCThread((Double x) -> (1 / Math.sqrt(2 * Math.PI)) * Math.exp((-x * x) / 2), 0f, Math.PI, n);

        double t1 = System.nanoTime() / 1e6;
        new Thread(mcThread).run();
        double t2 = System.nanoTime() / 1e6;

        System.out.printf("Sequential:\t%f\n", mcThread.sum);
        System.out.printf("\tCzas pracy metody:\t%f\n", t2 - t1);
    }

    static void integrateParallel(int k, int n) {
        MCThread[] mcThreads = new MCThread[k];

        for (int i = 0; i < k; i++) {
            mcThreads[i] = new MCThread((Double x) -> (1 / Math.sqrt(2 * Math.PI)) * Math.exp((-x * x) / 2), 0f, Math.PI, n);
        }

        double t1 = System.nanoTime() / 1e6;

        for (MCThread mcThread : mcThreads) {
            new Thread(mcThread).start();
        }

        double t2 = System.nanoTime() / 1e6;

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double mean = 0;
        for (MCThread mcThread : mcThreads) {
            try {
                mean += results.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mean /= k;
        double t3 = System.nanoTime() / 1e6;

        System.out.printf("Parallel:\t%f\n", mean);
        System.out.printf("\tCzas wykonywania wątków:\t%f\n", t2 - t1);
        System.out.printf("\tCzas liczenia średniej:\t%f\n", t3 - t2);
        System.out.printf("\tCzas pracy metody:\t%f\n", t3 - t1);
    }

    static class MCThread implements Runnable {
        Function<Double, Double> f;
        double a;
        double b;
        int n;
        double sum;

        MCThread(Function<Double, Double> f, double a, double b, int n) {
            this.f = f;
            this.a = a;
            this.b = b;
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < this.n; i++) {
                Random random = new Random();
                double x = random.nextDouble(this.b - this.a) + this.a;
                this.sum += this.f.apply(x) * (this.b - this.a) / this.n;
            }
            try {
                results.put(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            semaphore.release();
        }
    }
}
