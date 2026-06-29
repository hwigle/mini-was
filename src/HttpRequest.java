import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;

    // 브라우저가 보낸 파라미터를 저장할 바구니
    private Map<String, String> parameters = new HashMap<>();

    // 생성자 : 객체가 만들어질 떄 소켓의 빨대(InputStream)를 넘겨 받아 바로 분석을 끝냄
    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String requestLine = br.readLine();

        System.out.println("브라우저의 요청: " + requestLine);

        if(requestLine != null && !requestLine.isEmpty()) {
            String[] parts = requestLine.split(" ");
            this.method = parts[0];
            String fullPath = parts[1]; // 예: "/item?id=5"

            // [핵심 로직] 주소에 '?"가 포함되어 있는지 확인하고 쪼개기
            if(fullPath.contains("?")) {
                String[] pathParts = fullPath.split("\\?");
                this.path = pathParts[0];

                // 쿼리 스트링 파싱 로직("id=5&name=mac" -> 맵에 저장)
                String queryString = pathParts[1];
                String[] paramPairs = queryString.split("&");
                for(String pair : paramPairs) {
                    String[] keyAndValue = pair.split("=");
                    if(keyAndValue.length == 2) {
                        parameters.put(keyAndValue[0], keyAndValue[1]);
                    }
                }
            } else {
                // '?'가 없으면 그냥 원래 주소 그대로 사용
                this.path = fullPath;
            }
        }
    }

    // 분석이 끝난 데이터를 밖에서 꺼내 쓸 수 있게 Getter만 열어둔다.
    public String getMethod() {return method;}
    public String getPath() {return path;}


    // 밖에서 파라미터 값을 꺼내 쓸 수 있도록 메서드 제공
     public String getParameter(String key) {
         return parameters.get(key);
     };
}
