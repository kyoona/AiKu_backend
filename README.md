<h1 align="center" style="font-weight: bold;">지각을 방지하는 약속 관리 어플리케이션 AIKU ✨</h1>
<p align="center">
  <img align='center' src='https://github.com/user-attachments/assets/6c5c8c08-c355-4ccb-a0b7-19613ba97fd6' width="700"/></img>
</p>
<p align="center">
  약속을 지키는 그날까지 <b>"약속을 편리하게 관리하고, 재미있게 지각을 방지하자”</b>
</p>
<p align="center">기간 | 2024.04 ~ 2024.06.10</p>
<p align="center">팀원 | 곽유나, 최원탁</p>

<p align="center">
 <a href="https://www.notion.so/thene/API-47d29e4b3a4342feae2cbb5f2d82a11f?pvs=4">API 명세서</a> • 
  <a href="https://www.notion.so/thene/ER-766f1354eb824c528243be0a721e2296?pvs=4">E-R 다이어그램</a> • 
  <a href="https://www.notion.so/thene/JPA-Entity-c6ba42135bb24551a96520efd55def0a?pvs=4">Entity 설계</a> • 
</p>
<br/>

<h2 id="technologies">🛠️ 기술</h2>

| Category | Stack |
| --- | --- |
| Language | Java |
| Framework | Spring Boot |
| Library | Spring Data JPA, Query DSL, JWT |
| Database | H2 |
| Infra | AWS |
| Cloud Service | Firebase Messaging |

</br>
<h2>💻주요 화면 및 기능</h2>

### 1. 그룹 생성 및 그룹 내 약속 생성
- 그룹을 생성하고 카카오톡 url 공유를 통해 사용자를 초대할 수 있습니다.
- 그룹 내 약속을 생성할 수 있고 사용자는 자유롭게 약속에 참가할 수 있습니다.
- 약속 시간의 30분 전까지 '꼴찌 고르기' 베팅을 할 수 있습니다.
![그림1](https://github.com/user-attachments/assets/52e1a1a5-f48a-4d60-9f13-ce01506e43a5)
### 2. 맵 & 실시간 위치 공유
- 약속 시간 30분 전 알림과 함께 맵 기능이 열립니다.
- 맵에서 참가자들의 실시간 위치를 확인할 수 있습니다.
- 다른 참가자와 포인트를 걸고 '레이싱'게임을 진행할 수 있습니다.
- 참가자 모두가 약속 장소에 도착하거나, 약속 시간 30분 후에 알림과 함께 맵이 종료됩니다.
![그림2](https://github.com/user-attachments/assets/e7505a27-7059-4c15-9416-27346d327bca)
### 3. 결과 분석
- 맵이 종료된 후 도착 순위와 베팅 결과를 확인할 수 있습니다.
- 그룹 내 모든 약속 결과를 분석한 결과를 확인할 수 있습니다.
![그림3](https://github.com/user-attachments/assets/4d29de98-dff2-4ae9-a06c-99e666d80cb6)

<h2>👩🏻‍💻구현 파트</h2>

### 곽유나
<b>그룹 기능</b><br/>
- @SpringBootTest를 통한 통합 테스트를 작성하고 실행하였습니다.
- Mokito를 통해 외부 라이브러리의 의존성을 제거 하여 단위 테스트를 작성하고 실행하였습니다.


<b>약속 기능</b><br/>
- ApplicationEvent를 통해 서비스간 의존성 최소화
  약속 서비스에는 약속 알람, 맵 오픈 로직 예약, 맵 자동 종료 로직 예약과 같은 많은 부가 기능을 요구합니다. 서비스에서 서비스를 호출 하며 SchedulerService에서 많은 책임을 지기 보다는 ApplicationEvent를 통해 event를 publish 하고 observer가 처리하며 서비스간 의존성을 최소화 했습니다.
- Executors.newScheduledThreadPool()을 통해 지정된 시간에 실행되어야 할 로직들을 예약합니다. 반환받은 ScheduledFuture은 스케줄에 변동이 있을떄 예약한 로직에 대한 변동을 관리하기 위해 따로 저장하고 관리합니다.
  
<b>맵 기능</b><br/>
- Firebase Messaging을 통해 다른 사용자들의 실시간 위치 공유, 다른 사용자에게 이모지 보내기 등의 기능을 구현하였습니다.

<b>공통 클래스 개발</b><br/>
- data transfer object를 conroller계층과 service계층, repository 계층으로 분리하였습니다.
  DTO의 계층 구조는 controller dto, service dto, entity로 되어 있으며 controller dto와 service dto의 분리로 서비스 계층은 view에 대한 의존을 최소화 합니다. view에 변화가 생겨 제공해야 할 변수에 변경이 있어도 service계층은 영향을 받지 않습니다. 이는 디자인과 구현이 동시에 진행되어 뷰의 구조가 계속해서 바꼈던 저희 프로젝트에 알맞은 구조였습니다.
- ExceptionHandler를 통해 오류를 감지하고 사용자에게 일관된 응답을 할 수 있도록 합니다.
- MDC를 통해 Interception에서 다중 스레드 별 고유 id를 부여하고 이를 추적할 수 있도록 합니다.

<h2>🤩개선하고 싶은 점</h2>

- 개발 주기가 여유롭지 않았던 터라 전체적인 코드 리팩토링이 필요합니다.
- 조회 기능에 paging을 처리해야합니다.
- 예약해야할 로직을 Executors.newScheduledThreadPool()를 통해 시스템에서 관리하기 보다는 외부 서비스를 통해 관리할 필요성이 있습니다.
- Git Issue, PR template을 통한 깃을 더 구체적으로 활용하고 싶습니다.
현재 런칭을 위해 https://github.com/AiKU-Dev/Backend 에서 개선하여 프로젝트를 진행하고 있습니다 :)
