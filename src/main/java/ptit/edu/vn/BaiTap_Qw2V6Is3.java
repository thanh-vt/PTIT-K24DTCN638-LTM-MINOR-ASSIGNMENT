package ptit.edu.vn;

import UDP.Customer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public class BaiTap_Qw2V6Is3 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "K24DTCN638";
        String qCode = "Qw2V6Is3";

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
            byte[] buffer = new byte[65507];
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length
            );

            socket.receive(packet);

            byte[] receivedData = packet.getData();
            int offset = packet.getOffset();
            int length = packet.getLength();

            if (length < 8) {
                throw new IOException("Invalid response");
            }

            // Lấy 8 byte requestId
            byte[] requestId = Arrays.copyOfRange(
                    receivedData,
                    offset,
                    offset + 8
            );

            // Deserialize đối tượng Customer
            Customer customer;

            try (ByteArrayInputStream bis = new ByteArrayInputStream(
                    receivedData, offset + 8, length - 8
            );
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                customer = (Customer) ois.readObject();
            }

            // c1. Tách tên ban đầu
            String[] words = customer.getName()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .split("\\s+");

            // c2. Tạo username từ tên ban đầu
            StringBuilder username = new StringBuilder();

            for (int i = 0; i < words.length - 1; i++) {
                username.append(words[i].charAt(0));
            }

            username.append(words[words.length - 1]);

            customer.setUserName(username.toString());

            // c3. Chuẩn hóa tên
            StringBuilder name = new StringBuilder();

            // Từ cuối cùng viết hoa toàn bộ
            name.append(words[words.length - 1].toUpperCase(Locale.ROOT));

            // Các từ còn lại viết hoa chữ cái đầu
            if (words.length > 1) {
                name.append(", ");
            }

            for (int i = 0; i < words.length - 1; i++) {
                String word = words[i];

                name.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));

                if (i < words.length - 2) {
                    name.append(" ");
                }
            }

            customer.setName(name.toString());

            // c4. Chuẩn hóa ngày sinh bằng DateTimeFormatter
            DateTimeFormatter inputFormatter =
                    DateTimeFormatter.ofPattern("MM-dd-uuuu");

            DateTimeFormatter outputFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/uuuu");

            LocalDate date = LocalDate.parse(
                    customer.getDayOfBirth(), inputFormatter
            );

            customer.setDayOfBirth(date.format(outputFormatter));

            // d. Serialize đối tượng Customer
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(customer);
                oos.flush();
            }

            byte[] customerData = bos.toByteArray();

            // Ghép 8 byte requestId + đối tượng Customer
            byte[] resultData = new byte[8 + customerData.length];

            System.arraycopy(
                    requestId, 0, resultData, 0, 8
            );

            System.arraycopy(
                    customerData, 0, resultData, 8, customerData.length
            );

            // e. Gửi kết quả lên Server
            socket.send(new DatagramPacket(
                    resultData,
                    resultData.length
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

}
