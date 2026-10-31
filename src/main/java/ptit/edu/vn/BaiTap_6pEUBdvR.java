package ptit.edu.vn;

import RMI.CharacterService;

import java.net.URI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BaiTap_6pEUBdvR {

    public static void main(String[] args) throws Exception {
        String host = "36.50.135.242";
        int port = 1099;

        String studentCode = "K24DTCN638";
        String qCode = "6pEUBdvR";

        // Kết nối RMI Registry
        Registry registry = LocateRegistry.getRegistry(host, port);

        // Lấy đối tượng RMI
        CharacterService service = (CharacterService)
                registry.lookup("RMICharacterService");

        // a. Nhận chuỗi từ Server
        String data = service.requestCharacter(studentCode, qCode);

        // b. URL Encoding theo quy tắc URI path
        String encoded = new URI(null, null, data, null)
                .toASCIIString();

        System.out.println("Input: " + data);
        System.out.println("Encoded: " + encoded);

        // c. Gửi kết quả về Server
        service.submitCharacter(studentCode, qCode, encoded);
    }

}
