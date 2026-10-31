package ptit.edu.vn;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class BaiTap_2nzSr57Q {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2206;

        String studentCode = "K24DTCN638";
        String qCode = "3UMDyoi6";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            out.write(request.getBytes());
            out.flush();

            // b. Nhận dữ liệu từ server
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);

            if (bytesRead == -1) {
                throw new Exception("Server không gửi dữ liệu");
            }

            String response = new String(buffer, 0, bytesRead);
            System.out.println("Received: " + response);

            // Chuyển chuỗi thành mảng số nguyên
            int[] numbers = Arrays.stream(response.trim().split(","))
                    .mapToInt(s -> Integer.parseInt(s.trim()))
                    .toArray();

            if (numbers.length < 2) {
                throw new Exception("Cần ít nhất 2 số nguyên");
            }

            // c. Tìm khoảng cách nhỏ nhất
            Arrays.sort(numbers);

            long minDistance = Long.MAX_VALUE;
            int first = 0;
            int second = 0;

            for (int i = 1; i < numbers.length; i++) {
                long distance = (long) numbers[i] - numbers[i - 1];

                if (distance <= minDistance) {
                    minDistance = distance;
                    first = numbers[i - 1];
                    second = numbers[i];
                }
            }

            // Gửi kết quả lên server
            String result = minDistance + "," + first + "," + second;

            out.write(result.getBytes());
            out.flush();

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
