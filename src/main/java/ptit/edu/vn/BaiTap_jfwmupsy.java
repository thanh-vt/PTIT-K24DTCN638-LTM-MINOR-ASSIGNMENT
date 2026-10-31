package ptit.edu.vn;

import wsclient.CharacterService;
import wsclient.SoapCharacterService;

import java.util.Collections;
import java.util.List;

public class BaiTap_jfwmupsy {
    public static void main(String[] args) {

        String studentCode = "K24DTCN638";
        String qCode = "jfwmupsy";

        // Khởi tạo SOAP Service
        CharacterService service = new CharacterService();
        SoapCharacterService port = service.getSoapCharacterServicePort();

        // a. Nhận danh sách mã ASCII
        List<Integer> data = port.requestCharacter(studentCode, qCode);

        // b. Xoay vòng sang phải
        if (data != null && !data.isEmpty()) {
            int rotations = data.getFirst();
            Collections.rotate(data, rotations);
        }

        // c. Gửi danh sách đã xử lý
        port.submitCharacterCharArray(studentCode, qCode, data);
    }
}
