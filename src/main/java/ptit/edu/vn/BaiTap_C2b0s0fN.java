package ptit.edu.vn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class BaiTap_C2b0s0fN {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "K24DTCN638";
        String qCode = "C2b0s0fN";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
            socket.connect(InetAddress.getByName(host), port);

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);

            socket.send(new DatagramPacket(
                    sendData, sendData.length
            ));

            // b. Nhận dữ liệu từ Server
            byte[] buffer = new byte[4096];
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

            // Tách requestId và data
            String[] parts = response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // c. Chuẩn hóa chuỗi
            String normalized = normalize(data);

            // Gửi kết quả
            String result = requestId + ";" + normalized;
            byte[] resultData = result.getBytes(StandardCharsets.UTF_8);

            socket.send(new DatagramPacket(
                    resultData, resultData.length
            ));

            // d. Socket tự đóng khi thoát khỏi try-with-resources
            System.out.println("Da dong ket noi.");

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("Het thoi gian cho: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Loi ket noi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String normalize(String data) {
        StringBuilder result = new StringBuilder();

        boolean newWord = true;

        for (char c : data.toCharArray()) {
            if (Character.isWhitespace(c)) {
                result.append(c);
                newWord = true;
            } else {
                if (newWord) {
                    result.append(Character.toUpperCase(c));
                    newWord = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }
}
