package ptit.edu.vn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaiTap_oOlEfaCu {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "K24DTCN638";
        String qCode = "oOlEfaCu";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            DataInputStream in = new DataInputStream(
                    socket.getInputStream()
            );

            DataOutputStream out = new DataOutputStream(
                    socket.getOutputStream()
            );

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;
            out.writeUTF(request);
            out.flush();

            // b. Nhận chuỗi mã hóa và giá trị dịch chuyển
            String encrypted = in.readUTF();
            int s = in.readInt();
            s = Math.floorMod(s, 26);

            System.out.println("Encrypted: " + encrypted);
            System.out.println("Shift: " + s);

            // c. Giải mã Caesar
            StringBuilder decrypted = new StringBuilder();

            // dịch lùi
            for (char c : encrypted.toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    c = (char) ('A' + (c - 'A' - s + 26) % 26);
                } else if (c >= 'a' && c <= 'z') {
                    c = (char) ('a' + (c - 'a' - s + 26) % 26);
                } else {
                    decrypted.append(c);
                }

                decrypted.append(c);
            }

            // Gửi kết quả về server
            out.writeUTF(decrypted.toString());
            out.flush();

            System.out.println("Decrypted: " + decrypted);

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
