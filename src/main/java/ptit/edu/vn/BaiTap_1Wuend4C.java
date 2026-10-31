package ptit.edu.vn;

import wsclient.DataService;
import wsclient.SoapDataService;

import java.util.List;
import java.util.stream.Collectors;

public class BaiTap_1Wuend4C {
    public static void main(String[] args) {

        String studentCode = "K24DTCN638";
        String qCode = "1Wuend4C";

        // Khởi tạo SOAP Service
        DataService service = new DataService();
        SoapDataService port = service.getSoapDataServicePort();

        // a. Nhận danh sách số nguyên
        List<Integer> data = port.getData(studentCode, qCode);

        // b. Tìm số lớn nhất bằng cách ghép các số
        List<String> numbers = data.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        numbers.sort((a, b) -> (b + a).compareTo(a + b));

        String result = String.join("", numbers);

        // Xử lý trường hợp toàn số 0
        if (!result.isEmpty() && result.charAt(0) == '0') {
            result = "0";
        }

        System.out.println("Result: " + result);

        // c. Gửi kết quả về Server
        port.submitDataString(studentCode, qCode, result);
    }
}
