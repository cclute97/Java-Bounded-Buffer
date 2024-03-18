

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
        buffer.notifyIsNotEmpty(); // ensures that all waiting threads terminate
    }

    public void consume() {
        while (running) {
            //System.out.printf("Count=%d -- Sum=%.3f\n", consumptionCount, totalConsumptionSum); // TEST// 
            if (consumptionCount % 100000 == 0 && consumptionCount != 0) {
                System.out.printf("Consumer: Consumed %d items, Cumulative value of consumed items=%.3f\n", consumptionCount, totalConsumptionSum);
                if (consumptionCount == CONSUMPTION_LIMIT) {
                    running = false;
                }
            }

            if (buffer.isEmpty()) {
                try {
                    buffer.waitUntilNotEmpty();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }

            if (!running) {
                break;
            }
            consumeNextElement(buffer.removeAndNotify());
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