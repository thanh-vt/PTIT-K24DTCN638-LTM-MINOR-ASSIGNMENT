package ptit.edu.vn;

import RMI.ByteService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BaiTap_pRQYIMhy {

    public static void main(String[] args) throws Exception {
        String host = "36.50.135.242";
        int port = 1099;

        String studentCode = "K24DTCN638";
        String qCode = "pRQYIMhy";

        // Kết nối RMI Registry
        Registry registry = LocateRegistry.getRegistry(host, port);

        // Lấy đối tượng từ xa
        ByteService service = (ByteService) registry.lookup("RMIByteService");

        // a. Nhận mảng byte
        byte[] data = service.requestData(studentCode, qCode);

        // b. Đếm số lần xuất hiện
        int[] frequency = new int[256];

        for (byte b : data) {
            frequency[b & 0xFF]++;
        }

        // Tìm phần tử có tần suất nhỏ nhất
        // Duyệt theo thứ tự ban đầu để ưu tiên phần tử xuất hiện trước
        byte minByte = 0;
        int minCount = Integer.MAX_VALUE;

        for (byte b : data) {
            int count = frequency[b & 0xFF];

            if (count < minCount) {
                minCount = count;
                minByte = b;
            }
        }

        // c. Gửi kết quả về Server
        byte[] result = {
                minByte,
                (byte) minCount
        };

        service.submitData(studentCode, qCode, result);
    }

}
