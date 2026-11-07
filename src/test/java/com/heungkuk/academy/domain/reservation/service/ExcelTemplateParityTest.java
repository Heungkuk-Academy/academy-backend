package com.heungkuk.academy.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/**
 * 로컬 전용: 실제 양식을 외부 경로로 읽었을 때 결과가 기존(classpath 로 읽던) 결과와 셀 단위로 같은지 비교한다.
 *
 * <p>CI 에는 실제 양식이 없으므로 환경변수가 있을 때만 실행된다.
 * <pre>
 *   TEMPLATE_REAL_DIR=&lt;실제 양식 디렉터리&gt; TEMPLATE_BASELINE_DIR=&lt;기준 결과 디렉터리&gt; ./gradlew test --tests '*Parity*'
 * </pre>
 * 기준 결과는 변경 전 코드에서 같은 fixture 로 만든 {estimate,trade,confirmation}.txt 이다.
 */
@EnabledIfEnvironmentVariable(named = "TEMPLATE_REAL_DIR", matches = ".+")
@EnabledIfEnvironmentVariable(named = "TEMPLATE_BASELINE_DIR", matches = ".+")
class ExcelTemplateParityTest {

    @Test
    void 외부_경로로_읽은_결과가_기존_결과와_같다() throws IOException {
        Path baseline = Path.of(System.getenv("TEMPLATE_BASELINE_DIR"));
        ExcelServiceImpl service = ExcelServiceImplTest.service(new ExcelFixtures(),
                new ExcelTemplateLoader(System.getenv("TEMPLATE_REAL_DIR")));

        assertThat(XlsxDump.dump(service.generateEstimate(ExcelFixtures.RESERVATION_ID, "t")))
                .isEqualTo(Files.readString(baseline.resolve("estimate.txt")));
        assertThat(XlsxDump.dump(service.generateTrade(ExcelFixtures.RESERVATION_ID, "t")))
                .isEqualTo(Files.readString(baseline.resolve("trade.txt")));
        assertThat(XlsxDump.dump(service.generateConfirmation(ExcelFixtures.RESERVATION_ID, "t")))
                .isEqualTo(Files.readString(baseline.resolve("confirmation.txt")));
    }
}
