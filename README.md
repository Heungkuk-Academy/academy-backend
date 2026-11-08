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
