package ptit.edu.vn;

import TCP.Product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;

public class BaiTap_3onrQq95 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "K24DTCN638";
        String qCode = "3onrQq95";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            out.writeObject(request);
            out.flush();

            // b. Nhận dữ liệu từ server
            Product product = (Product) in.readObject();

            BigDecimal price = BigDecimal.valueOf(product.getPrice());

            BigDecimal discountedPrice = price
                    .multiply(BigDecimal.valueOf(100 - product.getDiscount()))
                    .divide(
                            BigDecimal.valueOf(100),
                            2,
                            RoundingMode.HALF_UP
                    );

            product.setPrice(discountedPrice.doubleValue());

            out.writeObject(product);
            out.flush();

            System.out.println("Sent: " + product);

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
