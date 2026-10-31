package ptit.edu.vn;

import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class BaiTap_nnd9j3tu {


        public static void main(String[] args) throws Exception {
            String host  = "36.50.135.242";
            int port = 2208;

            String studentCode = "K24DTCN638";
            String qCode = "nnd9j3tu";

            try (Socket socket = new Socket(host, port)) {
                socket.setSoTimeout(4000);

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

                // b. Nhận câu tiếng Anh từ Server
                String input = in.readLine();

                // c. Chuẩn hóa và đếm tần suất bigram
                String result = process(input);

                System.out.println("Input: " + input);
                System.out.println("Result: " + result);

                // d. Gửi kết quả về Server
                out.write(result);
                out.newLine();
                out.flush();
            }
        }

        public static String process(String input) {
            // Chuyển chữ thường, loại bỏ ký tự không hợp lệ
            String normalized = input.toLowerCase(Locale.ROOT)
                    .replaceAll("[^a-z0-9 ]", "")
                    .trim();

            if (normalized.isEmpty()) {
                return "";
            }

            // Tách các từ, xử lý nhiều dấu cách liên tiếp
            String[] words = normalized.split(" +");

            // Đếm tần suất bigram
            Map<String, Integer> frequency = new HashMap<>();

            for (int i = 0; i < words.length - 1; i++) {
                String bigram = words[i] + "_" + words[i + 1];

                frequency.merge(bigram, 1, Integer::sum);
            }

            // Sắp xếp theo tần suất giảm dần,
            // nếu bằng nhau thì theo từ điển tăng dần
            return frequency.entrySet()
                    .stream()
                    .sorted(
                            Comparator.<Map.Entry<String, Integer>>
                                            comparingInt(Map.Entry::getValue)
                                    .reversed()
                                    .thenComparing(Map.Entry::getKey)
                    )
                    .limit(3)
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("|"));
        }
}
