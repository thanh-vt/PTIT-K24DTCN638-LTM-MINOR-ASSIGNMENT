package ptit.edu.vn;

import java.io.EOFException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BaiTap_nhyVXici {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2211 ;

        String studentCode = "K24DTCN638";
        String qCode = "nhyVXici";

        try (SocketChannel channel = SocketChannel.open()) {
            channel.configureBlocking(true);
            channel.socket().connect(
                    new InetSocketAddress(host, port),
                    2000
            );
            channel.socket().setSoTimeout(4000);

            // a. Gửi mã sinh viên và mã câu hỏi
            writeFrame(channel, studentCode + ";" + qCode);

            // b. Nhận đúng 3 frame liên tiếp
            StringBuilder log = new StringBuilder();

            for (int i = 0; i < 3; i++) {
                String payload = readFrame(channel);
                log.append(payload);
            }

            System.out.println("Log: " + log);

            // c. Trích xuất các trường
            Map<String, String> fields = new HashMap<>();

            for (String part : log.toString().split(";")) {
                int index = part.indexOf('=');

                if (index >= 0) {
                    String key = part.substring(0, index);
                    String value = part.substring(index + 1);

                    fields.put(key, value);
                }
            }

            String result = "ip=" + fields.get("ip")
                    + ";path=" + fields.get("path")
                    + ";status=" + fields.get("status");

            System.out.println("Result: " + result);

            // Gửi kết quả về Server
            writeFrame(channel, result);
        } catch (Exception e) {
            System.err.println("Loi ket noi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Đọc đủ số byte yêu cầu
    private static void readFully(
            SocketChannel channel,
            ByteBuffer buffer
    ) throws Exception {

        while (buffer.hasRemaining()) {
            int bytesRead = channel.read(buffer);

            if (bytesRead == -1) {
                throw new EOFException(
                        "Server closed connection before frame was complete"
                );
            }
        }
    }

    // Đọc một frame: 4-byte length + UTF-8 payload
    private static String readFrame(
            SocketChannel channel
    ) throws Exception {

        // Đọc 4 byte độ dài
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.order(ByteOrder.BIG_ENDIAN);

        readFully(channel, lengthBuffer);

        lengthBuffer.flip();
        int length = lengthBuffer.getInt();

        if (length < 0 || length > 1_048_576) {
            throw new IllegalArgumentException(
                    "Invalid frame length: " + length
            );
        }

        // Đọc payload
        ByteBuffer payloadBuffer = ByteBuffer.allocate(length);

        readFully(channel, payloadBuffer);

        payloadBuffer.flip();

        return StandardCharsets.UTF_8
                .decode(payloadBuffer)
                .toString();
    }

    // Gửi một frame: 4-byte length + UTF-8 payload
    private static void writeFrame(
            SocketChannel channel,
            String message
    ) throws Exception {

        byte[] payload = message.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(4 + payload.length);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.putInt(payload.length);
        buffer.put(payload);
        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}
