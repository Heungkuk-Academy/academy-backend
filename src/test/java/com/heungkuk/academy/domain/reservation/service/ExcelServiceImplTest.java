package com.heungkuk.academy.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/**
 * 로컬 전용: 실제 양식으로 견적서/거래명세서/확인서가 정상 생성되는지 확인한다.
 *
 * <p>실제 양식은 Git 에 없으므로 CI 에서는 실행되지 않는다. 결과 파일(실제 양식 기반이라 연락처 포함)은
 * build/test-outputs/ 에 남으며 Git 에 올라가지 않는다.
 * <pre>
 *   TEMPLATE_REAL_DIR=&lt;실제 양식 디렉터리&gt; ./gradlew test --tests '*ExcelServiceImplTest'
 * </pre>
 */
@EnabledIfEnvironmentVariable(named = "TEMPLATE_REAL_DIR", matches = ".+")
class ExcelServiceImplTest {

    static final Path OUT = Path.of("build", "test-outputs");

    static ExcelServiceImpl service(ExcelFixtures f, ExcelTemplateLoader loader) {
        return new ExcelServiceImpl(f.reservationRepository, f.roomReservationRepository,
                f.classroomReservationRepository, f.mealReservationRepository, f.appSettingRepository, loader);
    }

    @Test
    void 실제_양식으로_견적서_거래명세서_확인서가_생성된다() throws IOException {
        ExcelServiceImpl service =
                service(new ExcelFixtures(), new ExcelTemplateLoader(System.getenv("TEMPLATE_REAL_DIR")));
        Files.createDirectories(OUT);

        byte[] estimate = service.generateEstimate(ExcelFixtures.RESERVATION_ID, "tester");
        byte[] trade = service.generateTrade(ExcelFixtures.RESERVATION_ID, "tester");
        byte[] confirmation = service.generateConfirmation(ExcelFixtures.RESERVATION_ID, "tester");

        Files.write(OUT.resolve("local-estimate.xlsx"), estimate);
        Files.write(OUT.resolve("local-trade.xlsx"), trade);
        Files.write(OUT.resolve("local-confirmation.xlsx"), confirmation);

        for (byte[] doc : new byte[][] {estimate, trade, confirmation}) {
            assertThat(XlsxDump.dump(doc)).contains(ExcelFixtures.ORGANIZATION);
        }
    }
}
