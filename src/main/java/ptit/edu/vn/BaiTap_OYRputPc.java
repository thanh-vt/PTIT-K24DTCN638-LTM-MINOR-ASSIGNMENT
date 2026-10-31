package ptit.edu.vn;

import TCP.Laptop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BaiTap_OYRputPc {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "K24DTCN638";
        String qCode = "OYRputPc";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(
                    socket.getOutputStream()
            );
            out.flush();

            ObjectInputStream in = new ObjectInputStream(
                    socket.getInputStream()
            );

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            out.writeObject(request);
            out.flush();

            // b. Nhận đối tượng Laptop
            Laptop laptop = (Laptop) in.readObject();

            // c. Sửa tên sản phẩm
            String[] words = laptop.getName().trim().split("\\s+");

            // Hoán đổi từ đầu và từ cuối
            if (words.length > 1) {
                String temp = words[0];
                words[0] = words[words.length - 1];
                words[words.length - 1] = temp;
            }

            laptop.setName(String.join(" ", words));

            // Đảo ngược số lượng
            int quantity = laptop.getQuantity();

            int reversed = 0;

            while (quantity > 0) {
                reversed = reversed * 10 + quantity % 10;
                quantity /= 10;
            }

            laptop.setQuantity(reversed);

            // Gửi đối tượng đã sửa
            out.writeObject(laptop);
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
