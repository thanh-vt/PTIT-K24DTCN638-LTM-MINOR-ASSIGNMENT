package ptit.edu.vn;

import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaiTap_pRFTEFru {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "K24DTCN638";
        String qCode = "pRFTEFru";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            // a. Gửi mã sinh viên và mã câu hỏi
            out.write(studentCode + ";" + qCode);
            out.newLine();
            out.flush();

            // b. Nhận chuỗi từ server
            String data = in.readLine();
            System.out.println("Received: " + data);

            // c. Đếm số lần xuất hiện của từng ký tự
            Map<Character, Integer> count = new LinkedHashMap<>();

            for (char c : data.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    count.put(c, count.getOrDefault(c, 0) + 1);
                }
            }

            // Tạo chuỗi kết quả
            StringBuilder result = new StringBuilder();

            for (Map.Entry<Character, Integer> entry : count.entrySet()) {
                if (entry.getValue() > 1) {
                    result.append(entry.getKey())
                            .append(":")
                            .append(entry.getValue())
                            .append(",");
                }
            }

            // Gửi kết quả lên server
            out.write(result.toString());
            out.newLine();
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
