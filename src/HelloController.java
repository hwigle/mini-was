public class HelloController implements Controller {
    @Override
    public String process(HttpRequest request) {
        System.out.println("HelloController: 안녕 페이지 로직 실행됨");
        return "<html><body><h1>안녕하세요! 새로운 페이지입니다. (클래스 분리 완료)</h1></body></html>";
    }
}