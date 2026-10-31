package ptit.edu.vn;

import TCP.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BaiTap_x9VKXZ9h {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "K24DTCN638";
        String qCode = "x9VKXZ9h";

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

            // b. Nhận đối tượng Customer
            Customer customer = (Customer) in.readObject();

            // Lấy tên ban đầu
            String originalName = customer.getName()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            String[] words = originalName.split("\\s+");

            // c1. Chuẩn hóa tên
            StringBuilder normalizedName = new StringBuilder();

            String lastName = words[words.length - 1]
                    .toUpperCase(Locale.ROOT);

            normalizedName.append(lastName).append(", ");

            for (int i = 0; i < words.length - 1; i++) {
                String word = words[i];

                normalizedName.append(
                        Character.toUpperCase(word.charAt(0))
                );

                normalizedName.append(word.substring(1));

                if (i < words.length - 2) {
                    normalizedName.append(" ");
                }
            }

            customer.setName(normalizedName.toString());

            // c2. Chuẩn hóa ngày sinh
            // Định dạng đầu vào: MM-dd-yyyy
            DateTimeFormatter inputFormatter =
                    DateTimeFormatter.ofPattern("MM-dd-yyyy");

            // Định dạng đầu ra: dd/MM/yyyy
            DateTimeFormatter outputFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Chuyển chuỗi thành LocalDate
            LocalDate date = LocalDate.parse(
                    customer.getDayOfBirth(), inputFormatter
            );

            // Chuyển LocalDate thành chuỗi theo định dạng mới
            customer.setDayOfBirth(date.format(outputFormatter));

            // c3. Tạo username
            StringBuilder username = new StringBuilder();

            for (int i = 0; i < words.length - 1; i++) {
                username.append(words[i].charAt(0));
            }

            username.append(words[words.length - 1]);

            customer.setUserName(username.toString());

            // Gửi đối tượng đã chuẩn hóa
            out.writeObject(customer);
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
