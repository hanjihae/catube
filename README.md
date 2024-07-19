<p align="left">
  <img src="https://postfiles.pstatic.net/MjAyNDA3MThfMjcx/MDAxNzIxMjk5NTA1NDk2.199m28oL60IQjI6xpNFSdXotVfwXejWVMRFtXUYLAWIg.Jj4AFDSQzMFU85d9K9trq9nUJUdP_UaOuqpTst70e4Qg.PNG/2.png?type=w2000">
</p>

# **💸PROJECT CATUBE💸**

### 프로젝트 소개
캣튜브(CATUBE)는 크리에이터가 동영상 창작물을 효율적으로 관리하고 그에 따른 수익을 투명하게 관리할 수 있는 동영상 플랫폼입니다. JWT 토큰을 이용한 소셜 로그인과 회원가입 및 자동 로그인 기능을 제공하여 높은 접근성을 제공합니다. 또한, 동영상 조회수와 광고 시청 횟수 데이터를 실시간으로 생성하며, 1일, 1주일, 1달 동안의 주요 지표를 통계적으로 제공하여 투명한 정산 과정을 지원합니다.

### 프로젝트 실행 기간
2024.06.19 ~ 2024.07.19

## ERD
<p align="left">
  <img src="https://postfiles.pstatic.net/MjAyNDA3MjBfMjQ2/MDAxNzIxNDA2NjQ3MzI1.ZGJ0PU7MqhzjWNI6Mud8HD2MkMl7_dTfA1rNZXcraLMg.VbqIIziJs880PPOMoFtr_926H3-pPo_ulZAFw9n-0FIg.PNG/image.png?type=w2000" height="1000">
</p>

## 주요 기능
- 동영상 관리
- 동영상 및 광고 시청기록 관리
- 동영상 및 광고 통계 생성
- 매일 자정 통계 및 정산 작업 수행

## 기술 스택
- 언어: Java 21
- DB: MySQL
- 빌드 도구: Gradle
- 프레임워크: Spring Boot 3.3.0
- 컨테이너화 도구: Docker / Docker Compose
- 배치 처리: Spring Batch 5
- 인증 방식: JWT (JSON Web Token)
- 통신 방식: HTTP Request / Response

## 프로젝트 아키텍처
모놀리식 아키텍처를 따르며 Docker와 MySQL을 사용해 환경을 구성합니다.

## 트러블슈팅
- #### JWT 토큰 파싱 에러 해결
  액세스 토큰이 만료되었을 때 DB에 저장된 리프레시 토큰을 비교하여 새로운 액세스 토큰을 생성하는 API를 구현하였습니다. 토큰 값 전달 과정과 파싱 로직에 문제가 없음에도 불구하고, 체인 필터에 의해 서비스 로직으로 전달되지 않는 문제가 있었습니다. 이를 해결하기 위해 JwtRequestFilter에서 UserDetailsService를 이용해 사용자 정보를 가져오도록 코드를 수정했습니다. 또한, HttpMessageNotReadableException을 처리하는 RequestDto 파일을 작성한 후 에러를 해결할 수 있었습니다. 이를 통해 이후 Authorization 헤더 값을 받아오는 방식으로 보다 간단하게 리팩토링할 수 있는 기반을 마련할 수 있었습니다.

- #### 동영상 및 광고 통계를 구현하기 위한 시청 기록 생성 방식 변경
  기존에는 유저별, 동영상별 시청 기록을 개별적으로 생성하여 시청할 때마다 업데이트하는 방식으로 서비스 로직을 구현하였으나, 이 방법으로는 기간별 시청 기록을 생성할 수 없음을 인지하였습니다. 이에 따라 시청 기록을 행 단위로 저장하여 기간별로 쌓인 행을 쿼리로 COUNT하여 통계를 완성할 수 있도록 방식 변경을 수행하였습니다.

- #### 배치 병렬 처리로 처리 시간 감소
  통계, 정산, 동영상 및 광고 누적 조회수 업데이트 작업이 순차적으로 이루어질 때, 각 잡에 들어있는 동영상과 광고 처리를 서로 독립적으로 서비스 로직을 구성해 배치 작업이 동시에 이루어질 수 있도록 taskexecutor를 사용했고 처리 시간을 단축하였습니다.

