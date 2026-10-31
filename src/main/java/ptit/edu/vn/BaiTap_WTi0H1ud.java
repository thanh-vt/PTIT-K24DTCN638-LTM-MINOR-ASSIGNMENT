package ptit.edu.vn;

import wsclient.CharacterService;
import wsclient.SoapCharacterService;

import java.util.*;
import java.util.stream.Collectors;

public class BaiTap_WTi0H1ud {
    public static void main(String[] args) {

        String studentCode = "K24DTCN638";
        String qCode = "WTi0H1ud";

        // Khởi tạo SOAP Service
        CharacterService service = new CharacterService();
        SoapCharacterService port = service.getSoapCharacterServicePort();

        // a. Nhận danh sách chuỗi
        List<String> data = port.requestStringArray(studentCode, qCode);

        // b. Phân nhóm theo số lượng nguyên âm
        Map<Integer, List<String>> groups = data.stream()
                .collect(Collectors.groupingBy(
                        BaiTap_WTi0H1ud::countVowels,
                        TreeMap::new,
                        Collectors.toList()
                ));

        // Sắp xếp từ trong mỗi nhóm và ghép thành chuỗi
        List<String> result = groups.values().stream()
                .map(words -> {
                    Collections.sort(words);
                    return String.join(",", words);
                })
                .collect(Collectors.toList());

        System.out.println("Result: " + result);

        // c. Gửi kết quả về Server
        port.submitCharacterStringArray(studentCode, qCode, result);
    }
    // Đếm số lượng nguyên âm trong một từ
    public static int countVowels(String word) {
        int count = 0;

        for (char c : word.toLowerCase(Locale.ROOT).toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) {
                count++;
            }
        }

        return count;
    }
}
