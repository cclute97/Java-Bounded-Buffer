
public class BoundedBuffer {
    private final short MAX_SIZE;
    private final Double[] buffer;
    private int frontIndex;
    private int backIndex;
    private short size;

    public BoundedBuffer(short max) {
        buffer = new Double[max];
        frontIndex = 0;
        backIndex = -1;
        size = 0;
        MAX_SIZE = max;
    }

    public void add(Double element) {
        int index = (backIndex + 1) % MAX_SIZE;
        size++;
        buffer[index] = element;
        backIndex++;
    }

    public Double remove() {
        int index = frontIndex % MAX_SIZE;
        Double element = buffer[index];
        frontIndex++;
        size--;
        return element;
    }

    public void addAndNotify(Double element) throws InterruptedException {
        try {
            synchronized (this) {
                while (size == 1000) {
                    wait();
                }
                this.add(element);
                notify();
            }
        } catch (InterruptedException e) {
            throw e;
        }
    }

    public Double removeAndNotify() throws InterruptedException {
        try {
            synchronized (this) {
                while (size == 0) {
                    wait();
                }
                Double element = this.remove();
                notify();
                return element;
            }
        } catch (InterruptedException e) {
            throw e;
        }
    }

    public boolean isFull() {
        if (size == MAX_SIZE) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public short size() {
        return size;
    }
}