# academy-backend

흥국생명 연수원 예약 관리 시스템의 백엔드 API 서버.

| 항목 | 내용 |
|---|---|
| 언어 / 프레임워크 | Java 21, Spring Boot |
| 빌드 | Gradle (`./gradlew`) |
| DB | MySQL 8.4 |
| 인증 | JWT (쿠키) |
| 기본 포트 | 8888 |
| 엑셀 | Apache POI (견적서 / 거래명세서 / 확인서 생성) |

## 관련 레포

| 레포 | 역할 |
|---|---|
| academy-backend |  API 서버 |
| academy-frontend | 웹 프론트엔드 |
| academy-infra | 운영 docker-compose, Nginx 등 배포 설정 |
| academy-docs | 문서 |


## 실행

프로파일은 두 가지다.

| 프로파일 | 용도 | 설정 파일 |
|---|---|---|
| `dev` | 로컬 개발 | `application-dev.properties` |
| `docker` | 운영 컨테이너 (Dockerfile 기본값) | `application-docker.properties` (환경변수로 주입) |

### 로컬 (dev)

Swagger UI는 로컬에서만 켜져 있다 (`/swagger-ui/index.html`). 운영(`docker`)에서는 비활성화된다.


## 환경변수

`docker` 프로파일은 아래 환경변수가 모두 필요하다. **값은 절대 커밋하지 않는다.**

| 변수 | 설명 |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB 사용자 |
| `SPRING_DATASOURCE_PASSWORD` | DB 비밀번호 |
| `JWT_SECRET` | JWT 서명 키 |
| `LOG_PATH` | 로그 파일 디렉터리 (app / error / auth 로그가 여기에 쌓임) |

## 커밋 금지 항목

아래는 `.gitignore`로 막혀 있다. 강제로 add 하지 않는다.

- `.env`, `.env.*` (`.env.example`은 제외)
- `src/main/resources/application-*.properties` (`application-docker.properties`는 플레이스홀더만 있어 예외)
- `docker-compose.dev.yml`
- `*.xlsx` (연락처가 포함된 엑셀 템플릿. `sample_*.xlsx`만 허용)
- `db/`, `logs/`, `*.pem`, `*.key`

## 브랜치 전략

- `main`: 운영 배포 기준. 머지되면 배포된다. **직접 커밋 금지**
- `develop`: 개발 기준 브랜치. 작업 브랜치는 여기서 분기하고 PR 대상도 `develop`
- 작업 브랜치: `<type>/<설명>` 형식
  - 예: `feat/excel-template-path`, `fix/reservation-date`, `docs/readme`, `chore/gitignore`

작업 흐름:

1. 이슈 생성
2. `develop`에서 작업 브랜치 분기
3. 커밋 후 push, `develop` 대상 PR 생성
4. 리뷰 후 머지
5. 릴리스 시 `develop` → `main` PR

## 커밋 메시지

[Conventional Commits](https://www.conventionalcommits.org/ko/) 형식을 따른다.

```
<type>: <제목>
```

| type | 용도 |
|---|---|
| `feat` | 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 |
| `chore` | 빌드, 설정, 기타 잡무 |
| `refactor` | 동작 변경 없는 구조 개선 |
| `test` | 테스트 추가 / 수정 |

예: `feat: 엑셀 템플릿을 외부 경로에서 읽도록 변경`
