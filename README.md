## 대규모 AI 시스템 설계 프로젝트
![image](https://github.com/user-attachments/assets/fdfe1353-be66-4fd1-9d18-1cf4296fbfd7)

### 1. 목표
이 프로젝트는 허브 간 물류 및 배송 관리 시스템을 구축하기 위한 마이크로서비스 아키텍처(MSA) 기반의 웹 애플리케이션입니다.
회사, 제품, 주문, 배송, 사용자 관리 등 물류 허브를 중심으로 한 종합적인 물류 관리 기능을 제공하며,
허브 간의 경로 최적화, 실시간 배송 추적, 자동 재고 관리 등 물류 운영의 효율성을 극대화하는 것을 목표로 합니다.

<br>

## 👨‍👩‍👧‍👦 Our Team

| 박지혜        <br>[@jeeheaG](https://github.com/jeeheaG) | 강희승<br>[@Hxxseung](https://github.com/Hxxseung) | 김주한<br>[@Hany-Kim](https://github.com/Hany-Kim) | 김한준<br>[@hanjoon](https://github.com/wkdehf217) |
|:-----------------------------------------------------:|:-----------------------------------------------:|:-----------------------------------------------:|:-----------------------------------------------:|
|                   Order<br>Delivery                   |               Company<br>Product                |                     Hub<br>                     |                  User<br>Slack                  |

<br>

### 📚 STACKS
<img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat&logo=Intellij IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/postman-FF6C37?style=flat&logo=postman&logoColor=white"/><img src="https://img.shields.io/badge/notion-000000?style=flat&logo=notion&logoColor=white"/><img src="https://img.shields.io/badge/slack-4A154B?style=flat&logo=slack&logoColor=white"/><br>
<img src="https://img.shields.io/badge/MSA -535D6C?style=flat&logo=awesomewm&logoColor=white"/><img src="https://img.shields.io/badge/swagger -85EA2D?style=flat&logo=swagger&logoColor=white"/><img src="https://img.shields.io/badge/Zipkin -FE5F50?style=flat&logo=Zipkin&logoColor=white"/><img src="https://img.shields.io/badge/Docker -2496ED?style=flat&logo=docker&logoColor=white"/><br>
<img src="https://img.shields.io/badge/Java 17 -C70D2C?style=flat&logo=java&logoColor=white"/><img src="https://img.shields.io/badge/springboot 3.4-6DB33F?style=flat&logo=springboot&logoColor=white"/><img src="https://img.shields.io/badge/springsecurity (Auth,JWT)-6DB33F?style=flat&logo=springsecurity&logoColor=white"/><img src="https://img.shields.io/badge/QueryDSL-5395FD?style=flat&logo=QueryDSL&logoColor=white"/><br>
<img src="https://img.shields.io/badge/postgresql -4169E1?style=flat&logo=postgresql&logoColor=white"/><img src="https://img.shields.io/badge/redis -FF4438?style=flat&logo=redis&logoColor=white"/><img src="https://img.shields.io/badge/GoogleGemini -8E75B2?style=flat&logo=googlegemini&logoColor=white"/><img src="https://img.shields.io/badge/Naver Direction5 -03C75A?style=flat&logo=naver&logoColor=white"/>
<img src="https://img.shields.io/badge/GoogleGeocode -4285F4?style=flat&logo=googlemaps&logoColor=white"/>

## 문서 자료
[📘 테이블 명세서](https://docs.google.com/spreadsheets/d/1xiXvHmo2wijXeWZmYdi3OQq0XHNwmVog8oWbCUE3zuE/edit?gid=2112576932#gid=2112576932)<br>

[📙 API 명세서](https://functional-iron-b15.notion.site/API-15e5724d1eee80d9a313ccb8c9f88bfe?pvs=4)

## ERD
![rush-logistic-system-2024](https://github.com/user-attachments/assets/37c36d73-cb23-49e8-bf84-ad1a3148ad96)
<br>

## 인프라 아키텍처
![rush_(3)](https://github.com/user-attachments/assets/4b9d9c3b-efd8-4e0f-9bad-31f4b2313161)

### 📃 Order
- 주문의 생성, 조회, 수정, 삭제 기능을 통해 주문 처리 및 물류 흐름 관리를 수행합니다.

### 📦 Delivery
- 주문에 해당하는 출발 및 도착 허브 정보와 전체 배송 상태 등의 배송 정보를 관리합니다. 생성, 조회, 수정, 삭제 기능을 제공합니다.

### 🚛 Delivery Managers

- 배송 관리자 생성, 조회, 수정, 삭제 기능을 제공하여 배송 현장의 관리와 배송 관리자 배정을 지원합니다.
- 업체 <-> 허브를 담당하는 인원은 10명씩 배정합니다.

### 🏢 Company
- 업체 생성, 조회, 수정, 삭제 기능을 제공하며, 각 업체는 물류 허브에서 제품을 관리하고 유통합니다.

### 🎁 Product
- 제품의 생성, 조회, 수정, 삭제를 통해 제품 카탈로그 및 재고 관리를 지원합니다.

### 🏭 Hub
- 물류 허브 간 경로를 관리하고 최적화하여, 허브 간 배송 프로세스를 지원합니다.
### 🔐 Auth / User
- 사용자 로그인, 회원가입과 역할 기반 인증 시스템을 통해 각 사용자(기업, 관리자, 일반 사용자)에게 적합한 권한을 부여합니다.

### 📩 Slack
- Slack 과의 연동을 통해 물류 관련 알림을 실시간으로 받아볼 수 있어, 신속한 협업과 문제 해결이 가능합니다.

## 💡 Trouble Shooting
- 문제 정의 : 사용자 인가 관리
    - AS-IS : 메인 비즈니스 로직과 섞여 퍼져있는 검증 로직
    - TO-BE : gateway 에서 토큰 복호화 일괄 처리, 커스텀 어노테이션과 Util 클래스 사용하여 로직 단일화
      <img width="1167" alt="image" src="https://github.com/user-attachments/assets/63315793-784f-42c3-986e-c94f7ed8df61" />
- 문제 정의 : 검색 구현
    - AS-IS : 복잡한 검색 조건 및 불필요하게 많은 페이징 정보
    - TO-BE : QueryDsl 인터페이스 상속으로 Predicate 기반 검색 구현, PagedModel 사용으로 불필요한 데이터 전파 방지
- 문제 정의 : DB와 Response 반환값 불일치
    - AS-IS : Updated_at 등 현재 데이터 값이 아닌 1차 캐싱 데이터 반환
    - TO-BE : EntityManager flush, clear를 통한 캐싱 데이터 삭제 후 ReLoad를 통한 실시간 값 반영
- 문제 정의 : Hub 간 경로 비효율
    - AS-IS : 기존 P2P 방식으로 허브 간 경로를 설정했을 때에 너무 비효율적인 배송 경로를 사용하게 됨
    - TO-BE : 허브 간 경로 탐색에 dijkstra 알고리즘을 적용, 출발, 도착 허브 간 최적 경로 사용, 배송 소요시간과 거리 최적화로 효율적인 배송 시스템 구축
    <img width="1167" alt="image" src="https://github.com/user-attachments/assets/ada8e7e0-aa39-4285-afbb-f9b481f2208f" />

- 문제 정의 : Naver Map API Response Body 파싱 문제
    - AS-IS : Naver Map API로 응답을 받았으나 body에 정상적으로 담기지 않음, 출력된 문자열을 그대로 URL에 담기 위해 복사/붙여넣기 해보니 일붜 공백은 NBSP로 이루어진 것을 확인, [\\\s\\\u00A0]+ 을 통해 공백과 NBSP로 이루어진 문자를 모두 %20으로 변환 → 실패 
    - TO-BE : URI인코딩 과정에서 잘못된 인코딩이 있었음,명시적으로 encode 타입 지정
      <img width="709" alt="image" src="https://github.com/user-attachments/assets/9d860b4e-6bc0-421b-b197-0e659f545c8a" />