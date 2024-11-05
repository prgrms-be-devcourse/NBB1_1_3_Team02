## 🙌 Book Your Seat!
> 공연 티켓 예매 서비스

## 프로젝트 소개 📌
공연 티켓팅을 효율적으로 처리하기 위한 서비스 입니다.

* 회원 관리 시스템
* 트래픽 분산을 위한 대기열 시스템
* 콘서트 정보 및 리뷰 제공
* 실시간 티켓 예약 및 토스 결제
* 결제 완료, 예외 발생 시 SNS 알림 구현

## 기술 스택 🦾

### Languages
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=flat-square&logo=kotlin&logoColor=white)

### Frameworks
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5C7F4E?style=flat-square&logo=querydsl&logoColor=white)

### Testing
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit&logoColor=white)

### Databases
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![H2](https://img.shields.io/badge/H2-1E8CBE?style=flat-square&logo=h2database&logoColor=white)

### Tools
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat-square&logo=intellij-idea&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white)

### Infrastructure
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=flat-square&logo=amazonaws&logoColor=white)
![nGrinder](https://img.shields.io/badge/nGrinder-2F6EB0?style=flat-square&logo=ngrinder&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens&logoColor=white)



## 팀원 소개 🧑‍🤝‍🧑
| 김관현 | 김도우 | 김수민 | 안태호 | 이성원 |
|:---:|:---:|:---:|:---:|:---:|
| <img src="https://avatars.githubusercontent.com/u/129512238?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/103844957?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/83461362?s=400&u=25e719b72f905561d1f8c6cd130170815cf88c29&v=4" width="150">  | <img src="https://avatars.githubusercontent.com/u/83914354?v=4" width="150">  | <img src="https://avatars.githubusercontent.com/u/122003584?v=4" width="150">  |
| [kwanse](https://github.com/kwanse)  | [kimdw0823](https://github.com/kimdw0823)  | [tnals2384](https://github.com/tnals2384)  | [AnTaeho](https://github.com/AnTaeho)  | [kcsc2217](https://github.com/kcsc2217) |
|공연 API, 예매,결제 API|Swagger API, 예매, 결제 API|회원 API, 쿠폰 API, 대기열 API|럭키드로우 API, 쿠폰 API, 대기열 API|쿠폰 API, Slack API, 리뷰 API|


## ERD
![BookYourSeat](https://github.com/user-attachments/assets/ba4b1f2e-5123-401a-b2cb-307d161350da)

## 프로젝트 구조
* 레이어드 아키텍처 + 도메인 기반 패키지 구조
* Facade : CommandService + QueryService
  
**EX) User 도메인**
```
book_your_seat
│
├── user
     ├── UserConst.java
     ├── controller
     │   ├── AdminUserController.java
     │   ├── UserController.java
     │   └── dto
     │       ├── UserCreateRequest.java
     │       ├── UserDetailResponse.java
     │       ├── UserResponse.java
     │       └── UserUpdateRequest.java
     ├── domain
     │   ├── User.java
     │   └── UserRole.java
     ├── repository
     │   ├── UserRepository.java
     │   ├── UserRepositoryCustom.java
     │   └── UserRepositoryImpl.java
     └── service
         ├── command
         │   └── UserCommandService.java
         ├── facade
         │   └── UserFacade.java
         └── query
             └── UserQueryService.java

```

## 기능 목록
**1. 회원**
* Spring Security + SMTP + JWT를 이용한 회원가입, 로그인을 구현
* [Security + Mail + Redis + Jwt 회원가입, 로그인](https://www.notion.so/Security-Mail-Redis-Jwt-e87d8f699e8d4c02be6bacf58fb794bd?pvs=4)
  
<img width="796" alt="스크린샷 2024-11-04 오후 2 57 37" src="https://github.com/user-attachments/assets/6ec1cb3f-c46c-4912-8a4d-f5d5184a5e0d">

---

**2. 선착순 쿠폰**
* 쿠폰 재고 동시성을 고려하여 다양한 방식의 Lock 구현 후, 성능테스트를 진행하여 최종 비관적 락 + Pub/sub 락 이중락 구현
* 비관적 락, 낙관적 락, Named 락 (RDB 이용)
* Spin 락, Pub/sub 분산 락 (Redis 이용)
* [선착순 쿠폰 락(Lock) 페스티벌](https://www.notion.so/Lock-eefea01ce31e4f9eae0134d43e6cee99?pvs=4)

---

**3. 공연 및 리뷰**
* 공연 목록과 상세 정보 조회
* 공연 리뷰 작성 및 리뷰 조회 가능
* 관심 공연 설정 가능
* No Offset 방식의 공연 목록 페이징 적용
* 캐싱을 통한 효율적인 공연 목록 반환
* 다중 컬럼 인덱스를 통한 리뷰 목록 조회 성능 개선
---

**4. 대기열**
* 티켓 예매 시 트래픽 분산을 위한 Redis Zset을 이용한 대기열 구현
* 완전한 의존성 분리로 멀티 서버로 분리 가능 -> 대기열 서버 / 메인 서버로 나누어 운영할 수 있도록 설계
* [대기열](https://www.notion.so/Redis-83d0177f966348d59a9c8feb73ef203d?pvs=4)

  ![image](https://github.com/user-attachments/assets/9d3a1dd7-715b-4f70-8906-29e730f0904a)
<img width="731" alt="스크린샷 2024-11-04 오후 3 02 58" src="https://github.com/user-attachments/assets/3efff7cc-5b09-456e-b2af-670a933b91ab">

---

**5. 좌석 선점 및 예매, 결제**
* 대기열을 다 기다려 진행열에 들어오게되면, 좌석 선점 및 예매, 결제 가능
* [예매, 결제 flow](https://www.notion.so/flow-7a1f350c3cce490db17447a718f68777?pvs=4)

| 좌석 선점 | 결제 |
| --- | --- |
| <img src="https://github.com/user-attachments/assets/9c0819e8-16c9-44f1-bc2d-eb905134044b" width="500"/> | <img src="https://github.com/user-attachments/assets/26d97aac-bbb0-42c9-acaf-e6961eeb454f" width="450"/><br> <img src="https://github.com/user-attachments/assets/d3672355-cb6c-4c79-a6f6-30556f817a15" width="450"/> |

---

**6. 럭키드로우**
* 이벤트 기간 내 티켓을 예매한 고객에게 랜덤 럭키드로우 쿠폰 지급
* 1등 -> 5명
* 2등 -> 10명
* 3등 -> 15명
* 4등 -> 20명
* 스케줄러를 통해 추첨 후, 예매자 email로 당첨 알림

## Notion
### [Notion](https://www.notion.so/2-bedcbbc5e4764192b2b56fc006769ddf?pvs=4)
트러블 슈팅, 일정관리, 프로젝트 문서 등은 여기 2팀 노션에서 보실 수 있습니다!

