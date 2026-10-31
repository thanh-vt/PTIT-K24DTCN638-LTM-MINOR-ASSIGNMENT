package ptit.edu.vn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BaiTap_ObiaTQsS {


    static void main() {
        String host  = "36.50.135.242";
        int port = 2207;

        String studentCode = "K24DTCN638";
        String qCode = "ObiaTQsS";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(port);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            String encrypted = in.readUTF();
            int s = in.readInt();

            int shift = Math.floorMod(s, 26);
            StringBuilder sb = new StringBuilder();

            for (char c : encrypted.toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    char decoded = (char) ('A' + (c - 'A' - shift + 26) % 26);
                    sb.append(decoded);
                } else if (c >= 'a' && c <= 'z') {
                    char decoded = (char) ('a' + (c - 'a' - shift + 26) % 26);
                    sb.append(decoded);
                } else {
                    sb.append(c);
                }
            }

            String decrypted = sb.toString();
            out.writeUTF(decrypted);
            out.flush();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
