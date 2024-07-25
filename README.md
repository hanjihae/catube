<p align="left">
  <img src="https://postfiles.pstatic.net/MjAyNDA3MjBfMjM0/MDAxNzIxNDEwNDExMDUz.CKXpW2vWbk0CpB2vtjDPlnixI046ROcbx28qJ6heHbog.ShKsdHjEK7f0I2penVHVqeNcT-5fPCIjMG56lOQbt4wg.PNG/SE-17a259f2-2116-4cd0-84b3-579aa4bc5bdc.png?type=w2000" width="500">
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
- 소셜(카카오) 로그인/회원가입
- 동영상 CRUD
- 동영상 및 광고 시청기록 관리
- 동영상 및 광고 통계 생성
- 매일 자정 통계 및 정산 작업 수행

## 기술 스택
<img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java Badge"><img src="https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot Badge"><img src="https://img.shields.io/badge/Gradle-8.8-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle Badge"><img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white" alt="Spring Security Badge"><img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white" alt="JWT Badge"><img src="https://img.shields.io/badge/Spring%20Batch-5.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Batch 5 Badge"><img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" alt="JPA Badge"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white" alt="MySQL Badge"><img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="Docker Badge"><img src="https://img.shields.io/badge/Docker Compose-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="Docker Compose Badge"><img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=GitHub%20Actions&logoColor=white" alt="GitHub Actions Badge">

## 프로젝트 아키텍처
모놀리식 아키텍처를 따르며 Docker와 MySQL을 사용해 환경을 구성합니다.

## API 문서


## 트러블슈팅
- [동영상/광고 시청기록 생성 방식 변경](https://candle-wasp-12a.notion.site/0ccde46cc76a4a48b4614b2cfa5a4fc7?pvs=4)
- [API 코드와 배치 작업을 별도의 서버로 분리](https://candle-wasp-12a.notion.site/API-07e37cd7df134f98996c614f9c39cb34?pvs=4)
- [매일 자정 실행되는 통계/정산 배치를 병렬 처리해 총 소요시간 11.81% 감소](https://candle-wasp-12a.notion.site/11-81-74f69e5a189f48688724922f44dad937?pvs=4)