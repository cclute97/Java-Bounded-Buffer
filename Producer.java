
import java.util.Random;

/* Implements Runnable to enable thread creation */

public class Producer implements Runnable {
    private final int PRODUCTION_LIMIT = 1000000; // 1 Million
    private int productionCount;
    private Double totalProductionSum;
    private Random random;

    private BoundedBuffer buffer;
    private boolean running = false;

    public Producer(BoundedBuffer buffer) {
        productionCount = 0;
        totalProductionSum = 0.0;
        random = new Random();
        this.buffer = buffer;
    }

    @Override
    public void run() {
        running = true;
        produce();
    }

    public void stop() {
        running = false;
        buffer.notifyIsNotFull(); // ensures that all waiting threads terminate
    }

    public void produce() {
        while (running) {
            if (productionCount % 100000 == 0 && productionCount != 0) {
                System.out.printf("Producer: Generated %d items, Cumulative value of consumed items=%.3f\n", productionCount, totalProductionSum);
                if (productionCount == PRODUCTION_LIMIT) {
                    running = false;
                }
            }

            if (buffer.isFull()) {
                try {
                    buffer.waitUntilNotFull();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }

            if (!running) {
                break;
            }
            buffer.addAndNotify(generateNextElement());
        }
    }

    public Double generateNextElement() {
        Double nextElement = random.nextDouble() * 100.0;
        totalProductionSum += nextElement;
        productionCount++;
        return nextElement;
    }
}