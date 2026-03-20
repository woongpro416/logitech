# Logitech Store ShoppingMall Web Application

## 1. 프로젝트 소개

본 프로젝트는 **1인 개발로 진행한 쇼핑몰 웹 애플리케이션**으로,  
회원 관리, 상품 조회, 장바구니, 주문, 리뷰, Q&A 기능을 구현한 웹 서비스입니다.

프론트엔드는 **Vue.js**, 백엔드는 **Spring Boot**, 데이터베이스는 **PostgreSQL**을 사용하여 개발하였으며,  
사용자 기능과 관리자 기능을 구분하여 실제 쇼핑몰 서비스 흐름에 가깝게 구현하는 것을 목표로 하였습니다.

---
<br>

## 2. 개발 목적

- 웹 애플리케이션의 전체 구조를 직접 설계하고 구현하기 위함
- 회원, 상품, 주문 등 주요 도메인 기능의 흐름을 이해하고 적용하기 위함
- 프론트엔드와 백엔드, DB를 연동하는 전반적인 개발 과정을 경험하기 위함
- 1인 프로젝트를 통해 기능 구현, 오류 수정, 문서화까지 전 과정을 정리하기 위함

---
<br>

## 3. 주요 기능

### 사용자 기능
- 회원가입 / 로그인 / 로그아웃
- 상품 목록 조회 / 상품 상세 조회
- 장바구니 담기 / 수량 변경 / 삭제
- 주문 생성 / 주문 내역 조회 / 주문 상세 조회
- 리뷰 작성
- Q&A 등록 및 조회
- 마이페이지 조회 및 회원 정보 확인

### 관리자 기능
- 관리자 페이지 접근
- 회원 관리
- 상품 관리
- 주문 관리

---
<br>

## 4. 기술 스택

### Frontend
- Vue.js
- JavaScript
- Bootstrap
- Axios
- Pinia
- Vue Router

### Backend
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Gradle

### Database
- PostgreSQL

### Development Environment
- IntelliJ IDEA
- VS Code
- Docker Desktop
- pgAdmin4
- Git / GitHub

---
<br>

## 5. 프로젝트 구조

```bash
frontend/
 ├── src/
 │   ├── assets/
 │   ├── components/
 │   ├── router/
 │   ├── stores/
 │   ├── views/
 │   └── App.vue

backend/
 ├── src/main/java/
 │   ├── controller/
 │   ├── service/
 │   ├── repository/
 │   ├── entity/
 │   ├── dto/
 │   └── config/
 ├── src/main/resources/
 │   ├── application.yml
 │   └── static/
 └── build.gradle
```

---
<br>

 ## 6. 데이터베이스 설계


```
member : 회원 정보 관리

item : 상품 정보 관리

cart : 장바구니 정보 관리

orders : 주문 정보 관리

review : 리뷰 정보 관리

qna : Q&A 게시글 정보 관리

주요 관계

회원 1 : N 주문

회원 1 : 1 장바구니

주문 1 : N 주문상품

상품 1 : N 리뷰

상품 1 : N Q&A
```
---
<br>

## 7. 주요 화면

### 1) 메인 페이지

- 상품 목록 조회

- 사용자 주요 진입 화면

### 2) 로그인 / 회원가입

- 회원 인증 및 세션 처리

### 3) 상품 상세 페이지

- 상품 정보 확인

- 장바구니 담기 및 주문 기능 연결

### 4) 장바구니 페이지

- 담은 상품 목록 확인

- 수량 변경 및 삭제 가능(관리자)

### 5) 주문 페이지

- 주문 상품 확인

- 주문 생성 처리

### 6) 주문 내역 / 주문 상세 페이지

- 주문 이력 조회

- 주문 상세 정보 확인

### 7) 마이페이지

- 회원 정보 조회

### 8) 리뷰 / Q&A 페이지

- 리뷰 작성 및 문의 등록 기능 제공

---
<br>


## 8. 주요 API 예시
- 회원가입	| POST	/members/join
- 로그인 | 	POST	/members/login
- 상품 목록 조회 |	GET	/items
- 상품 상세 조회 |	GET	/items/{id}
- 장바구니 조회 |	GET	/cart
- 장바구니 추가 |	POST	/cart/add
- 주문 생성 |	POST	/orders
- 주문 내역 조회 |	GET	/orders/my
- Q&A 등록	POST |	/qna
- 리뷰 등록	POST |	/review

---
<br>


## 9. 실행 방법
### 1) 백엔드 실행
- IntelliJ에서 백엔드 프로젝트를 연다.
- `main` 메서드가 포함된 Spring Boot 실행 클래스를 선택한다.
- Run 버튼을 눌러 애플리케이션을 실행한다.

또는

java -jar build/libs/logitech.jar
### 2) 프론트엔드 실행
npm install
npm run dev
### 3) DB 실행

PostgreSQL 실행

DB 및 계정 정보는 application.yml 설정에 맞게 적용


---
<br>

## 10. 테스트 및 검증

본 프로젝트는 다음과 같은 방식으로 기능을 점검하였습니다.

화면 동작 확인

주요 기능 직접 실행 테스트

PostgreSQL 조회문을 통한 데이터 반영 여부 확인

주문, 장바구니, Q&A 등 핵심 기능의 정상/예외 흐름 점검

---
<br>

## 11. 문서 구성

프로젝트 관련 상세 산출물은 별도 문서로 정리하였습니다.

1. 프로젝트 개요서

2. 프로젝트 제작 일정표

3. WBS

4. 이슈 관리표

5. 요구사항 정의서

6. 유스케이스 명세서

7. 기능 명세서

8. API 명세서

9. DB 설계서

10. 화면 설계서

11. 시퀀스/클래스 다이어그램

12. 프로젝트 구조도

13. 데이터 모델 및 비즈니스 로직

14. 테스트 문서

15. 사용자 메뉴얼

16. 운영 메뉴얼

17. 로그 및 모니터링 메뉴얼

18. 백업 및 복구 계획서


---
<br>

## 12. 개선 사항

```
1. 예외 처리 및 사용자 메시지 개선

2. 관리자 기능 고도화

3. API 테스트 자동화

4. 배포 환경 구성 및 운영 자동화

5. UI/UX 개선
```

---
<br>

## 13. 프로젝트 회고

```
본 프로젝트는 1인 개발로 진행하면서
기획, 구현, DB 설계, 오류 수정, 문서화까지 전 과정을 직접 경험한 프로젝트입니다.

기능 구현 자체뿐만 아니라,
프론트엔드-백엔드-DB 연동 구조를 이해하고
프로젝트 산출물을 체계적으로 정리하는 과정까지 포함하여
실무형 개발 흐름을 학습하는 데 의미가 있었습니다.
```