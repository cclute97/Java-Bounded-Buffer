
public class Consumer implements Runnable {
    private final int CONSUMPTION_LIMIT = 1000000; // 1 million
    private int consumptionCount;
    private Double totalConsumptionSum;

    private BoundedBuffer buffer;
    private boolean running = false;

    public Consumer(BoundedBuffer buffer) {
        consumptionCount = 0;
        totalConsumptionSum = 0.0;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        running = true;
        consume();
    }

    public void stop() {
        running = false;
    }

    public void consume() {
        while (running) {
            if (consumptionCount % 100000 == 0 && consumptionCount != 0) {
                System.out.printf("Consumer: Consumed %d items, Cumulative value of consumed items=%.3f\n", consumptionCount, totalConsumptionSum);
                if (consumptionCount == CONSUMPTION_LIMIT) {
                    System.out.printf("Consumer: Finished consuming %d items\n", consumptionCount);
                    running = false;
                }
            }

            try {
                consumeNextElement(buffer.removeAndNotify());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    public void consumeNextElement(Double element) {
        if (element != null) {
            Double currentBufferElement = element;
            totalConsumptionSum += currentBufferElement;
            consumptionCount++;
        }

    }
}