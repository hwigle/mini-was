import java.io.IOException;
import java.net.Socket;
import java.util.Map;

// Runnable을 구현하면 이 클래스는 독립적인 스레드 위에서 실행될 수 있다.
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, Controller> controllerMap;

    // 생성자: 알바생이 일할 떄 필요한 '손님(Socket)'과 '주소록(Map)'을 사장님으로부터 넘겨받음.
    public ClientHandler(Socket clientSocket, Map<String, Controller> controllerMap) {
        this.clientSocket = clientSocket;
        this.controllerMap = controllerMap;
    }

    // 스레드가 시작되면 자동으로 실행되는 메서드(알바생의 실제 업무)
    @Override
    public void run() {
        try {
            HttpRequest request = new HttpRequest(clientSocket.getInputStream());

            // 방어 로직: 빈 요청(파비콘 오류 등)이 들어오면 무시하고 다음 접속을 기다림
            if(request.getPath() == null) {
                clientSocket.close();
                return;
            }
            // 결과를 전송할 전담 객체인 HttpResponse를 준비
            HttpResponse response = new HttpResponse(clientSocket.getOutputStream());

            // 주소록(Map)에서 클라이언트가 요청한 경로를 담당하는 컨트롤러를 찾는다.
            Controller controller = controllerMap.get(request.getPath());

            if(controller != null) {
                String html = controller.process(request);
                response.send(200, html); // 성공(200) 응답 전송
            } else {
                response.send(404, "<html><body><h2>404 Not Found</h2></body></html>"); // 실패(404) 응답 전송
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
