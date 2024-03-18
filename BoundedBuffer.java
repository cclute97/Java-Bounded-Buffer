
import java.util.LinkedList;
import java.util.Queue;

public class BoundedBuffer {
    private final short MAX_SIZE;

    // References used for synchronization only
    private final Object IS_NOT_FULL = new Object(); 
    private final Object IS_NOT_EMPTY = new Object();

    private Queue<Double> buffer; 

    public BoundedBuffer(short max) {
        buffer = new LinkedList<>();
        MAX_SIZE = max;
    }

    /* The producer uses waitUntilNotFull to wait for the consumer to consume from the buffer and the consumer notifies the producer that the buffer 
     * is not full with notifyIsNotFull */

    public void waitUntilNotFull() throws InterruptedException {
        synchronized (IS_NOT_FULL) {
            IS_NOT_FULL.wait();
        }
    }

    public void notifyIsNotFull() {
        synchronized (IS_NOT_FULL) {
            IS_NOT_FULL.notify();
        }
    }

    /* The consumer uses waitUntilNotEmpty to wait for the producer to produce to the buffer and the producer notifies the consumer that the buffer 
     * is not empty with notifyIsNotEmpty */

    public void waitUntilNotEmpty() throws InterruptedException {
        synchronized (IS_NOT_EMPTY) {
            IS_NOT_EMPTY.wait();
        }
    }

    public void notifyIsNotEmpty() {
        synchronized (IS_NOT_EMPTY) {
            IS_NOT_EMPTY.notify();
        }
    }

    /* Producer uses add(), Consumer uses remove() */

    public void addAndNotify(Double element) {
        try {
            synchronized (this) {
                while (buffer.size() == 1000) {
                    wait();
                }
                buffer.add(element);
                notifyIsNotEmpty();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public Double removeAndNotify() {
        try {
            synchronized (this) {
                while (buffer.size() == 0) {
                    wait();
                }
                Double element = buffer.poll();
                notifyIsNotFull();
                return element;
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

    public boolean isFull() {
        if (buffer.size() == MAX_SIZE) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        if (buffer.size() == 0) {
            return true;
        }
        return false;
    }
}