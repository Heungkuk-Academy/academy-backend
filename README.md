# academy-backend

흥국생명 연수원 예약 관리 시스템의 백엔드 API 서버.

| 항목 | 내용 |
|---|---|
| 언어 / 프레임워크 | Java 21, Spring Boot |
| 빌드 | Gradle (`./gradlew`) |
| DB | MySQL 8.4 |
| 인증 | JWT (쿠키) |
| 엑셀 | Apache POI (견적서 / 거래명세서 / 확인서 생성) |

## 관련 레포

| 레포 | 역할 |
|---|---|
| academy-backend |  API 서버 |
| academy-frontend | 웹 프론트엔드 |
| academy-infra | 운영 설정 |
| academy-docs | 문서 |

## 브랜치 전략

- `main`: 운영 배포 기준. 머지되면 배포된다. **직접 커밋 금지**
- `develop`: 개발 기준 브랜치. 작업 브랜치는 여기서 분기하고 PR 대상도 `develop`
- 작업 브랜치: `<type>/<설명>` 형식
  - 예: `feat/reservation-search`, `fix/reservation-date`, `docs/readme`, `chore/gitignore`

작업 흐름:

1. 이슈 생성
2. `develop`에서 작업 브랜치 분기
3. 커밋 후 push, `develop` 대상 PR 생성
4. 리뷰 후 머지
5. 릴리스 시 `develop` → `main` PR

### 머지 조건

- `develop`과 `main`은 **PR로만** 반영된다. 직접 푸시는 관리자도 불가능하다.
- CI의 **`Test and build`**(`./gradlew build`, 테스트 포함)가 통과해야 머지할 수 있다. 테스트가 하나라도 실패하면 머지 버튼이 막힌다.
- PR을 올리기 전에 로컬에서 `./gradlew build` 로 먼저 확인한다.
- `.github/workflows/ci.yml` 의 잡 이름(`Test and build`)을 바꾸거나 `paths` 필터를 넣으면 브랜치 보호의 필수 체크와 어긋나 머지가 막힐 수 있으므로, 바꿀 때는 보호 규칙도 같이 수정한다.

## 커밋 메시지

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

예: `feat: 예약 목록 검색 조건 추가`
