package task3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public BlockingQueue() {
        this(DEFAULT_CAPACITY);
    }

    public synchronized void enqueue(T value) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println(Thread.currentThread().getName() + " ожидает освобождения места, очередь переполнена");
            wait();
        }

        queue.add(value);
        System.out.println("Добавлен элемент в очередь потоком " + Thread.currentThread().getName());
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " ожидает новый элемент, очередь пуста");
            wait();
        }

        T value = queue.poll();
        System.out.println("Прочитан элемент из очереди потоком " + Thread.currentThread().getName());
        notifyAll();
        return value;
    }

    public synchronized int size() {
        return queue.size();
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(2);

        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int p = 1; p <= 2; p++) {
            int id = p;
            pool.submit(() -> {
                for (int i = 1; i <= 3; i++) {
                    int value = id * 10 + i;
                    queue.enqueue(value);
                    System.out.println("Размер очереди: " + queue.size());
                }
                return null;
            });
        }

        for (int c = 1; c <= 2; c++) {
            pool.submit(() -> {
                Thread.sleep(1000);
                for (int i = 0; i < 3; i++) {
                    queue.dequeue();
                    System.out.println("Размер очереди: " + queue.size());
                }
                return null;
            });
        }

        pool.shutdown();
    }

}
