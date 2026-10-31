package ptit.edu.vn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaiTap_q1EF3kkK {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "K24DTCN638";
        String qCode = "q1EF3kkK";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
            socket.connect(InetAddress.getByName(host), port);


            // a. Gửi mã sinh viên và mã câu hỏi
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length
            );

            socket.send(sendPacket);

            // b. Nhận dữ liệu từ server
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(
                    buffer, buffer.length
            );

            socket.receive(receivePacket);

            String response = new String(
                    receivePacket.getData(),
                    receivePacket.getOffset(),
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Received: " + response);

            // Tách requestId và data
            String[] parts = response.split(";", 2);
            String requestId = parts[0];
            String data = parts[1];

            // c. Đếm số lần xuất hiện của từng ký tự
            Map<Character, Integer> count = new LinkedHashMap<>();

            for (char c : data.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    count.put(c, count.getOrDefault(c, 0) + 1);
                }
            }

            // Tìm ký tự xuất hiện nhiều nhất
            char maxChar = '\0';
            int maxCount = 0;

            for (Map.Entry<Character, Integer> entry : count.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxChar = entry.getKey();
                }
            }

            // Tìm các vị trí xuất hiện
            StringBuilder positions = new StringBuilder();

            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == maxChar) {
                    positions.append(i + 1).append(",");
                }
            }

            // Tạo kết quả
            String result = requestId + ";" + maxChar + ":" + positions;

            System.out.println("Sent: " + result);

            // Gửi kết quả lên server
            byte[] resultData = result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket = new DatagramPacket(
                    resultData, resultData.length
            );

            socket.send(resultPacket);

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
