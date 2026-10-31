package ptit.edu.vn;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaiTap_ww8IDb4s {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "K24DTCN638";
        String qCode = "ww8IDb4s";

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

            System.out.println("Received: " + response);

            // Tách requestId và danh sách số nguyên
            String[] parts = response.split(";", 2);

            String requestId = parts[0];
            String[] numbers = parts[1].split(",");

            // c. Tìm max và min
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (String s : numbers) {
                int num = Integer.parseInt(s.trim());

                if (num > max) {
                    max = num;
                }

                if (num < min) {
                    min = num;
                }
            }

            // Tạo thông điệp kết quả
            String result = requestId + ";" + max + "," + min;

            System.out.println("Sent: " + result);

            // Gửi kết quả lên Server
            byte[] resultData = result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket = new DatagramPacket(
                    resultData,
                    resultData.length
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
