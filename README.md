# Mini WAS (Web Application Server)

> **순수 Java로 구현한 멀티스레드 기반 웹 애플리케이션 서버** <br>
> 프레임워크(Spring, Tomcat)의 '블랙박스'를 열고, 웹 서버의 핵심 동작 원리를 직접 코드로 증명한 프로젝트입니다.

## 프로젝트 기획 동기
실무에서 관성적으로 스프링(Spring) 프레임워크를 사용해 왔지만, 내부에서 HTTP 요청이 어떻게 처리되고 라우팅되는지 정확히 알지 못하는 아쉬움이 있었습니다. 
이를 극복하고 프레임워크의 동작 원리를 꿰뚫어 보는 엔지니어로 성장하기 위해, 순수 Java 기능만으로 톰캣(Tomcat)과 디스패처서블릿(DispatcherServlet)의 코어 로직을 직접 구현해 보았습니다.

## 🛠️ 기술 스택
* **Language:** Java (JDK 8+)
* **Core API:** Java `java.net.Socket`, `java.io.InputStream`/`OutputStream`
* **Concurrency:** Java `Thread`, `Runnable`
* **Architecture:** OOP Design (Interface, Map-based Routing)

## 핵심 구현 기능 및 기술적 원리

### 1. 멀티스레딩(Multi-Threading)을 통한 동시성 제어 (Tomcat 원리)
* **문제 인식:** 단일 스레드 구조에서는 특정 클라이언트의 요청이 지연될 경우(예: 무거운 DB 조회), 이후 접속한 모든 클라이언트가 무한정 대기(Blocking)해야 하는 치명적 문제가 발생함.
* **해결 및 증명:** `Runnable` 인터페이스를 구현한 `ClientHandler`를 설계. `ServerSocket`이 클라이언트 접속을 받을 때마다 새로운 `Thread`를 생성하여 비동기로 위임함. 
* **결과:** 특정 페이지에 5초의 강제 지연(`Thread.sleep`)을 발생시켜도 다른 클라이언트의 접속과 응답이 전혀 방해받지 않음을 테스트로 검증 완료.

### 2. OCP 원칙을 준수한 객체지향 라우팅 (DispatcherServlet 원리)
* **문제 인식:** 요청 경로(URL)가 늘어날 때마다 `main` 메서드 내부의 `if-else` 분기문이 무한정 길어지는 안티 패턴 발생.
* **해결 및 증명:** 공통 `Controller` 인터페이스를 정의하고, `HashMap`을 활용해 경로(Path)를 Key로, 담당 컨트롤러 객체를 Value로 등록하는 '주소록' 구조 설계.
* **결과:** 새로운 페이지 추가 시 기존 코드를 전혀 수정할 필요 없이 맵에 객체만 추가하면 되는 **개방-폐쇄 원칙(OCP)** 달성. 실무의 `@RestController` 매핑 원리 체득.

### 3. HTTP 요청 파싱 및 쿼리 스트링 처리 (@RequestParam 원리)
* **문제 인식:** 브라우저가 전송하는 Raw HTTP 텍스트 덩어리에서 비즈니스 로직에 필요한 데이터만 정확히 추출해야 함.
* **해결 및 증명:** 분석 전담 객체인 `HttpRequest`를 분리. `split()` 메서드를 정교하게 활용해 URI에서 진짜 경로(Path)와 데이터(Query String)를 분리.
* **결과:** 앰퍼샌드(`&`)와 등호(`=`)를 기준으로 파라미터를 쪼개어 전용 `Map`에 저장함으로써, 단 1개의 컨트롤러로 수많은 동적 페이지를 처리할 수 있는 구조 완성.

## 📝 회고 및 배운 점
* **마법에서 기술로:** 매일 습관처럼 띄우던 서버의 정체가 결국 무한 루프를 도는 `ServerSocket`이며, 컨트롤러 매핑이 맵(Map)의 $O(1)$ 탐색 과정임을 코드로 직접 확인했습니다.
* **단일 책임 원칙(SRP)의 위력:** 요청(`HttpRequest`) 파싱과 응답(`HttpResponse`) 출력을 별도의 객체로 캡슐화하니, 메인 흐름을 통제하는 코드가 극적으로 간결해지고 유지보수가 쉬워지는 객체지향의 진짜 힘을 체감했습니다.
