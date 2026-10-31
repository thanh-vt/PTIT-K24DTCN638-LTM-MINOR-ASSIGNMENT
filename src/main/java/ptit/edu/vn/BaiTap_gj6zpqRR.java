package ptit.edu.vn;

import UDP.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class BaiTap_gj6zpqRR {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "K24DTCN638";
        String qCode = "gj6zpqRR";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
            socket.connect(InetAddress.getByName(host), port);

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = ";" + studentCode + ";" + qCode;
            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(
                    sendData,
                    sendData.length
            ));

            // b. Nhận requestId (8 byte) và đối tượng Student
            byte[] buffer = new byte[65507];

            DatagramPacket receivePacket = new DatagramPacket(
                    buffer,
                    buffer.length
            );

            socket.receive(receivePacket);

            byte[] receivedData = receivePacket.getData();
            int offset = receivePacket.getOffset();
            int length = receivePacket.getLength();

            // Lấy 8 byte requestId
            byte[] requestId = new byte[8];
            System.arraycopy(
                    receivedData,
                    offset,
                    requestId,
                    0,
                    8
            );

            // Giải tuần tự hóa đối tượng Student
            Student student;

            try (ByteArrayInputStream bis = new ByteArrayInputStream(
                    receivedData,
                    offset + 8,
                    length - 8
            );
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                student = (Student) ois.readObject();
            }

            // c1. Chuẩn hóa tên
            String[] words = student.getName()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .split("\\s+");

            StringBuilder normalizedName = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                normalizedName.append(
                        Character.toUpperCase(word.charAt(0))
                );
                normalizedName.append(word.substring(1));

                if (i < words.length - 1) {
                    normalizedName.append(" ");
                }
            }

            student.setName(normalizedName.toString());

            // c2. Tạo email
            StringBuilder email = new StringBuilder();

            // Lấy tên (từ cuối cùng)
            email.append(words[words.length - 1]);

            // Lấy chữ cái đầu của họ và tên đệm
            for (int i = 0; i < words.length - 1; i++) {
                email.append(words[i].charAt(0));
            }

            email.append("@ptit.edu.vn");

            student.setEmail(email.toString());

            // Serialize đối tượng Student đã sửa
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(student);
                oos.flush();
            }

            byte[] studentData = bos.toByteArray();

            // Ghép requestId + Student
            byte[] resultData = new byte[8 + studentData.length];

            System.arraycopy(
                    requestId,
                    0,
                    resultData,
                    0,
                    8
            );

            System.arraycopy(
                    studentData,
                    0,
                    resultData,
                    8,
                    studentData.length
            );

            // Gửi dữ liệu lên Server
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

    public static String normalize(String data) {
        StringBuilder result = new StringBuilder();

        boolean newWord = true;

        for (char c : data.toCharArray()) {
            if (Character.isWhitespace(c)) {
                result.append(c);
                newWord = true;
            } else {
                if (newWord) {
                    result.append(Character.toUpperCase(c));
                    newWord = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }
}
