package ptit.edu.vn;

import java.io.*;
import java.net.Socket;

public class BaiTap_frtH2fPF {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "K24DTCN638";
        String qCode = "frtH2fPF";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            DataInputStream in = new DataInputStream(
                    socket.getInputStream()
            );

            DataOutputStream out = new DataOutputStream(
                    socket.getOutputStream()
            );

            // a. Gửi mã sinh viên và mã câu hỏi
            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            // b. Nhận hai số nguyên từ server
            int a = in.readInt();
            int b = in.readInt();

            System.out.println("a = " + a);
            System.out.println("b = " + b);

            // c. Tính tổng và tích
            int sum = a + b;
            int product = a * b;

            System.out.println("Sum = " + sum);
            System.out.println("Product = " + product);

            // Gửi tổng trước
            out.writeInt(sum);

            // Gửi tích sau
            out.writeInt(product);

            out.flush();

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
