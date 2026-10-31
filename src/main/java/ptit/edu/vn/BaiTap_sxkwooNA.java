package ptit.edu.vn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class BaiTap_sxkwooNA {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "K24DTCN638";
        String qCode = "sxkwooNA";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
            socket.connect(InetAddress.getByName(host), port);

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData,
                    sendData.length
            );

            socket.send(sendPacket);

            // b. Nhận dữ liệu từ server
            byte[] buffer = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(
                    buffer,
                    buffer.length
            );

            socket.receive(receivePacket);

            String response = new String(
                    receivePacket.getData(),
                    receivePacket.getOffset(),
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            ).trim();

            System.out.println("Received: " + response);

            // Tách requestId, n và danh sách số
            String[] parts = response.split(";", 3);

            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);

            // Đánh dấu các số đã xuất hiện
            boolean[] exists = new boolean[n + 1];

            if (parts.length == 3 && !parts[2].isEmpty()) {
                String[] numbers = parts[2].split(",");

                for (String s : numbers) {
                    int num = Integer.parseInt(s.trim());

                    if (num >= 1 && num <= n) {
                        exists[num] = true;
                    }
                }
            }

            // c. Tìm các số còn thiếu
            StringJoiner missing = new StringJoiner(",");

            for (int i = 1; i <= n; i++) {
                if (!exists[i]) {
                    missing.add(String.valueOf(i));
                }
            }

            // Gửi kết quả về server
            String result = requestId + ";" + missing;

            byte[] resultData = result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket = new DatagramPacket(
                    resultData,
                    resultData.length
            );

            socket.send(resultPacket);

            System.out.println("Sent: " + result);

            // d. Socket tự đóng khi thoát khỏi try-with-resources
            System.out.println("Da dong ket noi.");

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("Het thoi gian cho: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Loi ket noi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
