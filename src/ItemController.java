public class ItemController implements Controller {
    @Override
    public String process(HttpRequest request) {
        String id = request.getParameter("id");
        return "<html><body>" +
                "<h1>상품 상세 페이지</h1>" +
                "<p>당신이 조회한 상품 번호는 <b>[" + id + "]</b> 번 입니다.</p>" +
                "</body></html>";
    }
}
