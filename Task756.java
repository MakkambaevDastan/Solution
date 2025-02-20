import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class Task756 {
    private static int BUFFER_SIZE;
    private static DataInputStream dataInputStream;
    private static byte[] buffer;
    private static int bufferPointer, bytesRead;
    public static void main(String[] args) throws IOException {
        BUFFER_SIZE = 1 << 16;
        dataInputStream = new DataInputStream(System.in);
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = 0;
        bytesRead = 0;
        int n = nextInt();
        int k = nextInt();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = nextInt();
        }
        Deque<Integer> deque = new ArrayDeque<>();
        StringBuilder result = new StringBuilder((n - k + 1) * 6);
        for (int i = 0; i < n; i++) {
            if (!deque.isEmpty() && deque.getFirst() < i - k + 1) {
                deque.removeFirst();
            }
            while (!deque.isEmpty() && array[deque.getLast()] > array[i]) {
                deque.removeLast();
            }
            deque.addLast(i);
            if (i >= k - 1) {
                result.append(array[deque.getFirst()]).append('\n');
            }
        }
        System.out.write(result.toString().getBytes());
    }
    private static int nextInt() throws IOException {
        int result = 0;
        byte currentByte = read();
        boolean isNegative = (currentByte == '-');
        if (isNegative) {
            currentByte = read();
        }
        do {
            result = result * 10 + currentByte - '0';
        } while ((currentByte = read()) >= '0' && currentByte <= '9');
        return isNegative ? -result : result;
    }

    private static byte read() throws IOException {
        if (bufferPointer == bytesRead) {
            bytesRead = dataInputStream.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) {
                buffer[0] = -1;
            }
        }
        return buffer[bufferPointer++];
    }
}
