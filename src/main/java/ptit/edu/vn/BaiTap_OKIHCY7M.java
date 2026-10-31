package ptit.edu.vn;

import wsclient.DataService;
import wsclient.SoapDataService;

import java.util.List;

public class BaiTap_OKIHCY7M {
    public static void main(String[] args) {

        String studentCode = "K24DTCN638";
        String qCode = "OKIHCY7M";

        // Khởi tạo SOAP Service
        DataService service = new DataService();
        SoapDataService port = service.getSoapDataServicePort();

        // a. Nhận danh sách số nguyên
        List<Integer> numbers = port.getData(studentCode, qCode);

        // b. Tính tổng các phần tử
        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Numbers: " + numbers);
        System.out.println("Sum: " + sum);

        // c. Gửi kết quả về Server
        port.submitDataInt(studentCode, qCode, sum);
    }
}
