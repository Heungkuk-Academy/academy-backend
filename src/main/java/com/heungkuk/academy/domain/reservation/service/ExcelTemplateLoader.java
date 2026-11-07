package com.heungkuk.academy.domain.reservation.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.heungkuk.academy.global.entity.LogNames;

import lombok.extern.slf4j.Slf4j;

/**
 * 엑셀 양식을 {@code app.template.path} 디렉터리에서 읽어 온다.
 *
 * <p>실제 양식에는 연락처와 직인 등이 들어 있어 Git/jar 에 포함하지 않고 서버에 따로 둔다.
 * 경로가 없거나 파일이 없으면 어디를 찾았는지 담은 예외를 던진다.
 */
@Slf4j(topic = LogNames.RESERVATION)
@Component
public class ExcelTemplateLoader {

    private final Path externalDir;

    public ExcelTemplateLoader(@Value("${app.template.path:}") String externalPath) {
        this.externalDir = StringUtils.hasText(externalPath) ? Path.of(externalPath.trim()) : null;
    }

    /** 호출한 쪽이 스트림을 닫아야 한다. */
    public InputStream open(ExcelTemplate template) {
        if (externalDir == null) {
            throw new IllegalStateException(
                    "엑셀 양식 경로(app.template.path)가 설정되지 않았습니다. template=" + template);
        }
        Path file = externalDir.resolve(template.fileName());
        if (!Files.isRegularFile(file)) {
            throw new IllegalStateException("엑셀 양식 파일이 없습니다. template=" + template + " path=" + file);
        }
        try {
            return Files.newInputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException("엑셀 양식을 읽을 수 없습니다: " + file, e);
        }
    }

    /** 서버 기동 시 양식 3개를 찾을 수 있는지 한 번 남긴다 (마운트 누락을 배포 직후 바로 알아채기 위함). */
    @EventListener(ApplicationReadyEvent.class)
    void logTemplateSources() {
        for (ExcelTemplate template : ExcelTemplate.values()) {
            if (externalDir != null && Files.isRegularFile(externalDir.resolve(template.fileName()))) {
                log.info("엑셀 양식 로드: 외부 경로 사용 template={}", template);
            } else {
                log.warn("엑셀 양식 로드: 파일 없음 → 해당 엑셀 생성이 실패합니다 template={} externalPath={}", template,
                        externalDir == null ? "(미설정)" : externalDir);
            }
        }
    }
}
