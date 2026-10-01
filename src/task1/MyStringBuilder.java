package task1;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class MyStringBuilder {

    private static final int DEFAULT_CAPACITY = 16;

    private char[] value;
    private int length;

    private final Stack<Snapshot> history = new Stack<>();

    public MyStringBuilder() {
        value = new char[DEFAULT_CAPACITY];
    }

    public MyStringBuilder(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("capacity < 0");
        value = new char[capacity];
    }

    public MyStringBuilder(String str) {
        value = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            value[i] = str.charAt(i);
        }
        length = str.length();
    }

    private void increaseCapacity(int minCapacity) {
        if (value.length < minCapacity) {
            value = Arrays.copyOf(value, value.length * 2);
        }
    }

    public MyStringBuilder append(String str) {
        save();

        int expectedLength = length + str.length();
        increaseCapacity(expectedLength);

        for (int i = 0; i < str.length(); i++) {
            value[length++] = str.charAt(i);
        }

        return this;
    }

    public MyStringBuilder append(char c) {
        save();

        increaseCapacity(length + 1);

        value[length++] = c;

        return this;
    }

    public MyStringBuilder append(int n) {
        save();
        return append(String.valueOf(n));
    }

    public char charAt(int index) {
        if (index >= value.length || index < 0) {
            throw new IllegalArgumentException("Некорретный индекс символа");
        }
        return value[index];
    }

    public int length() { return length; }

    public MyStringBuilder insert(int pos, String str) {
        save();

        if (pos < 0 || pos > length) {
            throw new IllegalArgumentException("pos: " + pos + " length: " + length);
        }
        int expectedLength = length + str.length();
        increaseCapacity(expectedLength);

        char[] buff = Arrays.copyOfRange(value, pos, length);
        for (int i = 0; i < str.length(); i++) {
            value[i + pos] = str.charAt(i);

        }

        length = pos + str.length();
        for (char c : buff) {
            value[length++] = c;
        }

        return this;
    }

    public MyStringBuilder delete(int start, int end) {
        save();

        if (start >= end || start < 0 || start > length || end > length) {
            throw new IllegalArgumentException("Некорректные аргументы для удаления");
        }

        System.arraycopy(value, end, value, start, length - end);
        length -= (end - start);

        return this;
    }

    @Override
    public String toString() {
        return new String(value, 0, length);
    }

    public MyStringBuilder undo() {
        if (history.empty()) {
            throw new RuntimeException("Не было изменений строки");
        }
        Snapshot snapshot = history.pop();
        value = snapshot.value;
        length = snapshot.length;

        return this;
    }

    private void save() {
        history.push(new Snapshot(Arrays.copyOf(value, value.length), length));
    }

    private record Snapshot(char[] value, int length) {}

    public static void main(String[] args) {
        MyStringBuilder s = new MyStringBuilder();
        s.append("Hello").append(',').append(" world").append(123);
        System.out.println(s);
        s.insert(6, " my");
        System.out.println(s);
        s.delete(6, 9);
        System.out.println(s);
        s.undo();
        System.out.println(s);
    }
}
