package ptit.edu.vn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaiTapTcpCharacter {
    public static void main(String[] args) {
        String host = "36.50.135.242"; // Thay bằng IP server thực tế
        int port = 2208;

        String studentCode = "K24DTCN638";
        String qCode = "kp7GcAKf";

        try (
                Socket socket = new Socket(host, port)
        ) {
            // Thời gian chờ đọc dữ liệu tối đa 5 giây
            socket.setSoTimeout(5000);

            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream(),
                                    StandardCharsets.UTF_8
                            )
                    )
            ) {
                // a. Gửi mã sinh viên và mã câu hỏi
                String request = studentCode + ";" + qCode;
                out.write(request);
                out.newLine();
                out.flush();

                System.out.println("Da gui: " + request);

                // b. Nhận danh sách tên miền từ server
                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server da dong ket noi.");
                    return;
                }

                System.out.println("Danh sach nhan duoc: " + response);

                // c. Lọc các tên miền có đuôi .edu
                List<String> eduDomains = Arrays.stream(
                                response.split("\\s*,\\s*")
                        )
                        .map(String::trim)
                        .filter(domain ->
                                domain.toLowerCase().endsWith(".edu")
                        )
                        .collect(Collectors.toList());

                String result = String.join(", ", eduDomains);

                // Gửi kết quả lên server
                out.write(result);
                out.newLine();
                out.flush();

                System.out.println("Danh sach .edu: " + result);
            }

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
