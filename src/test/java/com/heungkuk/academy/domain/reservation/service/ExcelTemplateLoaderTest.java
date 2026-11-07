package com.heungkuk.academy.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** 실제 양식 없이 임시 폴더에 만든 가짜 워크북으로 검증한다 (CI 에서 실행). */
class ExcelTemplateLoaderTest {

    @TempDir
    Path dir;

    @Test
    void 외부_경로의_파일을_읽는다() throws IOException {
        byte[] external = workbookWithMarker("외부양식");
        Files.write(dir.resolve("estimate_template.xlsx"), external);

        try (InputStream is = new ExcelTemplateLoader(dir.toString()).open(ExcelTemplate.ESTIMATE)) {
            assertThat(is.readAllBytes()).isEqualTo(external);
        }
    }

    @Test
    void 세_양식은_각자_정해진_파일_이름으로_읽는다() throws IOException {
        for (ExcelTemplate t : ExcelTemplate.values()) {
            Files.write(dir.resolve(t.fileName()), workbookWithMarker(t.name()));
        }
        ExcelTemplateLoader loader = new ExcelTemplateLoader(dir.toString());

        assertThat(ExcelTemplate.ESTIMATE.fileName()).isEqualTo("estimate_template.xlsx");
        assertThat(ExcelTemplate.TRADE.fileName()).isEqualTo("trade_template.xlsx");
        assertThat(ExcelTemplate.CONFIRMATION.fileName()).isEqualTo("confirmation_template.xlsx");
        for (ExcelTemplate t : ExcelTemplate.values()) {
            try (InputStream is = loader.open(t)) {
                assertThat(is.readAllBytes()).isEqualTo(workbookWithMarker(t.name()));
            }
        }
    }

    @Test
    void 파일이_없으면_찾은_경로를_담은_예외를_던진다() {
        ExcelTemplateLoader loader = new ExcelTemplateLoader(dir.toString());

        assertThatThrownBy(() -> loader.open(ExcelTemplate.TRADE)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("TRADE").hasMessageContaining(dir.resolve("trade_template.xlsx").toString());
    }

    @Test
    void 일부만_있으면_있는_것만_읽고_나머지는_예외() throws IOException {
        byte[] external = workbookWithMarker("외부견적");
        Files.write(dir.resolve("estimate_template.xlsx"), external);
        ExcelTemplateLoader loader = new ExcelTemplateLoader(dir.toString());

        try (InputStream is = loader.open(ExcelTemplate.ESTIMATE)) {
            assertThat(is.readAllBytes()).isEqualTo(external);
        }
        assertThatThrownBy(() -> loader.open(ExcelTemplate.CONFIRMATION))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 경로가_설정되지_않으면_예외를_던진다() {
        for (String unset : new String[] {"", "  ", null}) {
            assertThatThrownBy(() -> new ExcelTemplateLoader(unset).open(ExcelTemplate.ESTIMATE))
                    .isInstanceOf(IllegalStateException.class).hasMessageContaining("app.template.path");
        }
    }

    @Test
    void 실제_양식은_빌드_리소스에_포함되지_않는다() {
        for (ExcelTemplate t : ExcelTemplate.values()) {
            assertThat(getClass().getResource("/templates/" + t.fileName()))
                    .as("실제 양식이 classpath 에 들어 있으면 안 된다: %s", t.fileName()).isNull();
        }
    }

    private static byte[] workbookWithMarker(String marker) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            wb.createSheet("s").createRow(0).createCell(0).setCellValue(marker);
            wb.write(out);
            return out.toByteArray();
        }
    }
}
