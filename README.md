<div align="center">

[<img src="https://img.shields.io/badge/프로젝트 기간-2025.07.25~2025.07.31-fab2ac?style=flat&logo=&logoColor=white" />]()

<!-- logo -->
![screenshot](https://github.com/user-attachments/assets/f69214cd-5088-486d-bb5e-1e789b59c627)
</div>

## 📝 소개

사용자의 취향과 상황에 맞춰 맞춤형 선물 꾸러미를 추천하는 서비스 플랫폼입니다.  
간단한 설문을 통해 관계, 기념일, 예산, 취향 등을 입력하면 AI 기반 추천 알고리즘을 통해 최적의 선물 꾸러미를 제안합니다.

<br />

### 화면 구성

#### 1) 진입 · 설문 시작
| 로그인 |
| --- |
| <img width="300" src="https://github.com/user-attachments/assets/cd9358d1-6a64-4877-a347-3f594f805ce9" />
| 카카오 간편 로그인. JWT 토큰 발급 및 자동 저장|

---

#### 2) 선물 추천 플로우
| 메인 화면 | 설문 진행 예시 | 추천 결과 |
| --- | --- | --- |
| <img width="300" src="https://github.com/user-attachments/assets/8382fc0e-6df7-48b9-94bd-7eb83284fb01" /> | <img width="300" src="https://github.com/user-attachments/assets/b1eaad91-d9ee-4955-90d3-eb901d2a3af4" /> | <img width="300" src="https://github.com/user-attachments/assets/a736585e-1041-48bb-bd76-54c7041807e3" /> |
| ‘선물 추천받기’ 버튼 선택 | 관계·예산 등 설문 응답 | AI 기반 맞춤 선물 꾸러미 표시 |

#### 2-1) 선물 추천 전체 플로우 (GIF)
| 전체 과정 |
| --- |
| <img width="300" src="https://github.com/user-attachments/assets/89f47667-e3ae-4508-938f-bcef6b170d83" /> |
| 추천 시작 → 설문 응답 → 추천 결과 |

---

#### 3) 메시지 추천
| 메시지 톤 선택 | 추천 메시지 표시 |
| --- | --- |
| <img width="300" src="https://github.com/user-attachments/assets/c26d632c-5069-498a-8bdb-9388c1b206c5" /> | <img width="300" src="https://github.com/user-attachments/assets/de9df99b-9420-4ec0-b02a-d5c2cc920643" /> |
| 선물에 담을 메시지 톤(감동형, 재치형 등) 선택 | 선택한 톤에 맞춰 추천 문구 자동 생성 |

#### 3-1) 메시지 추천 전체 플로우 (GIF)
| 전체 과정 |
| --- |
| <img width="300" src="https://github.com/user-attachments/assets/6c5ccc93-aa6c-4372-9fef-f1992ab0e9a5" /> |
| 톤 선택 → 추천 메시지 확인|

---

#### 4) 추가 추천 기능
| 연령대별 추천 | 상품 상세 페이지 |
| --- | --- |
| <img width="300" src="https://github.com/user-attachments/assets/2651d580-a362-4445-9c23-8e3ef2604f1d" /> | <img width="300" src="https://github.com/user-attachments/assets/e7d7000c-19ec-48ab-ac03-0bc8d06ef46e" /> |
| 연령대별 인기 상품 기반 AI 추천 | 현대백화점 상품 상세와 연동, 외부 구매 페이지 이동 가능 |

---

#### 5) 캘린더 기반 추천
| 달력에 이벤트 추가 | 달력에서 추천 |
| --- | --- |
| <img width="300" src="https://github.com/user-attachments/assets/527c6334-43d9-4150-b20f-4397cef063a0" /> | <img width="300" src="https://github.com/user-attachments/assets/175cd41e-8a43-490d-9d6c-3d66b6e0fd72" /> |
| 기념일, 생일, 모임 등 일정 등록 및 알림 설정 | 등록된 이벤트 기반 선물 추천(Lite 설문 퍼널 적용) |

---

#### 6) 결제 · 주변인 관리
| 결제 | 주변인 수정 |
| --- | --- |
| <img width="300" src="https://github.com/user-attachments/assets/ce44c097-b38f-4d2d-b233-f5e6bf747d11" /> | <img width="300" src="https://github.com/user-attachments/assets/4cc536b3-99ee-4298-a507-a79183c3f47d" /> |
| 아임포트(Iamport) 결제 연동. 카드/간편결제 지원 | 주변인 정보(이름, 관계, 연령대) 수정 및 실시간 반영 |

<br />

### Front-end

<div>
<img src="https://github.com/user-attachments/assets/4954e980-c5d0-426a-829e-c85ddf88cba8" width=80 />
<img src="https://github.com/user-attachments/assets/ee0e05ce-bd0d-4d66-a931-d470495adaf1" width=80/>
</div>

### Back-end

<div>
<img width="80" alt="springboot" src="https://github.com/user-attachments/assets/9cf606c8-05b8-4073-9e79-a64d3ba41ca8" />
<img width="80" alt="jpa" src="https://github.com/user-attachments/assets/66108e2c-85cc-44d1-a041-28f700fa458c" />
<img width="80" alt="kibana" src="https://github.com/user-attachments/assets/3edbd1ad-3366-4f08-845f-763fbbd7238c" />
<img width="80" alt="mysql" src="https://github.com/user-attachments/assets/ea75f72a-8e79-47f7-ac27-c6102d880a7c" />
<img width="80" alt="elasticsearch" src="https://github.com/user-attachments/assets/7900d7b7-27c2-45a9-bfa3-ca9a8ab5e953" />
<img width="80" alt="springsecurity" src="https://github.com/user-attachments/assets/a3523750-3215-4dbf-8c9a-9abcca54c87f" />
</div>

### Infra

<div>
<img width="80" alt="nginx" src="https://github.com/user-attachments/assets/76d2ecf4-b428-44ff-a6dd-35dd9f82b1d0" />
<img width="80" alt="duckdns" src="https://github.com/user-attachments/assets/263a861a-4f91-437e-81ec-96dd272bf062" />
<img width="80" alt="harbor-circle" src="https://github.com/user-attachments/assets/dcc946f9-3ada-4fa3-9d06-42a2835a5dd4" />
<img width="80" alt="docker" src="https://github.com/user-attachments/assets/42917b90-45fe-4226-bb4d-471e11a46306" />
<img width="80" alt="cloudflare-circle" src="https://github.com/user-attachments/assets/aeebe3c4-6577-4406-8fef-05a2e54f0ddf" />
<img width="80" alt="jenkins" src="https://github.com/user-attachments/assets/0019a936-a17f-4f5e-8fc0-0fc90d6b7a34" />
</div>

### Tools

<div>
<img width="80" height="80" alt="Chat_GPT" src="https://github.com/user-attachments/assets/37ad2922-9900-469c-a3f4-a79a582228fd" />
<img width="80" height="80" alt="Kakao_OAuth2" src="https://github.com/user-attachments/assets/5c1ffe71-b5fd-4621-8931-a94aae498654" />
<img width="80" height="80" alt="iamport" src="https://github.com/user-attachments/assets/35f32049-4572-4c14-8738-df0404405a8d" />
<img width="80" height="80" alt="FCM" src="https://github.com/user-attachments/assets/6a3ccb9f-9cd2-47de-94e1-4eca0c2c4b33" />
</div>

<br />

### 인프라 아키텍처
<img width="1024" alt="infra-architecture" src="https://github.com/user-attachments/assets/9be4d07c-bcdf-4943-a8d0-db6d8b2b0eb7" />

<br />

## 💁‍♂️ 프로젝트 팀원

| 이름 | GitHub | 역할 |
|------|--------|------|
| 한상준 | [sangzun-han](https://github.com/sangzun-han) | Fullstack |
| 박찬혁 | [pch8349](https://github.com/pch8349) | Fullstack |
| 정재영 | [JaeY0ung](https://github.com/JaeY0ung) | Fullstack · 인프라 |
| 이지연 | [ljy6712](https://github.com/ljy6712) | Fullstack |
| 원승현 | [hyeon8571](https://github.com/hyeon8571) | Fullstack |
