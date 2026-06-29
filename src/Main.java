import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        // --- [1] 초기화: 라우터(주소록) 세팅 ---
        // 경로(Path)와 해당 경로의 요청을 처리할 로직(Controller) 매핑
        Map<String, Controller> controllerMap = new HashMap<>();

        controllerMap.put("/", new HomeController());
        controllerMap.put("/hello", new HelloController());
        controllerMap.put("/item", new ItemController());

        int port = 8080;

        // --- [2] 서버 구동 ---
        // ServerSocket을 생성하고 8080 포트를 열고 클라이언트 접속 대기
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("미니 WAS가 " + port + "포트에서 시작되었습니다.");

            // 서버는 종료되지 않고 무한 루프를 돌며 클라이언트를 맞이
            while(true) {
                // 1. 손님 맞이(대기) 브라우저가 접속할 때까지 대기(Blocking)하다가, 접속하면 Socket을 반환
                Socket clientSocket = serverSocket.accept();
                System.out.println("새로운 클라이언트가 접속했습니다.");

                // 2. 알바생 생성(손님 소켓과 주소록 전달)
                ClientHandler handler = new ClientHandler(clientSocket, controllerMap);

                // 3. 알바생을 새로운 스레드에 태워서 일 시작
                Thread thread = new Thread(handler);
                thread.start();

                // 사장님(Main 스레드)은 알바생이 일을 끝낼 때까지 기다리지 않고,
                // 즉시 while문의 처음으로 돌아가 다음 손님(accept)을 기다림.
            }
        } catch (IOException e) {
            System.err.println("서버를 띄우는 중 에러가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
