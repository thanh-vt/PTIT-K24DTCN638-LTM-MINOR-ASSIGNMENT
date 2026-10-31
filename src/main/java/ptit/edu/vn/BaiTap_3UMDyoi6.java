package ptit.edu.vn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class BaiTap_3UMDyoi6 {
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

            String response = new String(buffer, 0, bytesRead).trim();
            System.out.println("Received: " + response);

            // Chuyển chuỗi thành mảng số nguyên
            String[] parts = response.split(",");
            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            // c. Tìm giá trị lớn thứ hai
            Integer max = null;
            Integer secondMax = null;

            for (int num : numbers) {
                if (max == null || num > max) {
                    secondMax = max;
                    max = num;
                } else if (num < max &&
                        (secondMax == null || num > secondMax)) {
                    secondMax = num;
                }
            }

            if (secondMax == null) {
                throw new Exception("Không tìm thấy giá trị lớn thứ hai");
            }

            // Tìm vị trí xuất hiện của giá trị lớn thứ hai
            int position = -1;

            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] == secondMax) {
                    position = i;
                    break;
                }
            }

            // Gửi kết quả lên server
            String result = secondMax + "," + position;
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
