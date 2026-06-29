public class HomeController implements Controller {
    @Override
    public String process(HttpRequest request) {
        System.out.println("HomeController: 메인 페이지 5초 대기 시작...");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("HomeController: 5초 대기 완료!");
        return "<html><body><h1>메인 페이지입니다! (클래스 분리 완료)</h1></body></html>";
    }
}