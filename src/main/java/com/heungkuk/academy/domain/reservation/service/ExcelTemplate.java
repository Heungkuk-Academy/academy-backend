package com.heungkuk.academy.domain.reservation.service;

/** 엑셀 양식 종류. 파일 이름은 {@code app.template.path} 디렉터리 안의 이름이다. */
public enum ExcelTemplate {

    ESTIMATE("estimate_template.xlsx"),
    TRADE("trade_template.xlsx"),
    CONFIRMATION("confirmation_template.xlsx");

    private final String fileName;

    ExcelTemplate(String fileName) {
        this.fileName = fileName;
    }

    public String fileName() {
        return fileName;
    }
}
