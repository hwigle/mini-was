import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpResponse {
    private PrintWriter pw;

    // 생성자 : 클라이언트와 연결된 출력 빨대(OutputStream)를 받아서 준비
    public HttpResponse(OutputStream out) {
        this.pw = new PrintWriter(out, true);
    }

    // 상태 코드와 바디(HTML)를 넘겨 받아 응답을 조립해서 쏴주는 메서드
    public void send(int statusCode, String body) {
        pw.println("HTTP/1.1 " + statusCode + " OK");
        pw.println("Content-Type: text/html; charset=UTF-8");
        pw.println(""); // 절대 빠지면 안 되는 빈 줄
        pw.println(body);
    }
}
