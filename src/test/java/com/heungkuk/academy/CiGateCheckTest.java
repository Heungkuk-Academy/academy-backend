package com.heungkuk.academy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** 일회용: CI 게이트 검증용 의도적 실패 테스트 (확인 후 삭제되는 브랜치에만 존재) */
class CiGateCheckTest {

    @Test
    void 통과한다() {
        assertTrue(true);
    }
}
