package com.heungkuk.academy.domain.reservation.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** 엑셀 내용을 셀 단위 텍스트로 풀어서 두 결과를 비교할 수 있게 한다 (값, 수식, 병합 영역). */
final class XlsxDump {

    private XlsxDump() {}

    static String dump(byte[] xlsx) {
        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(xlsx))) {
            List<String> lines = new ArrayList<>();
            for (int s = 0; s < wb.getNumberOfSheets(); s++) {
                Sheet sheet = wb.getSheetAt(s);
                String sn = "[" + s + ":" + sheet.getSheetName() + "]";
                lines.add(sn + " merged=" + sheet.getNumMergedRegions());
                for (int m = 0; m < sheet.getNumMergedRegions(); m++) {
                    lines.add(sn + " merge " + sheet.getMergedRegion(m).formatAsString());
                }
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        String v = value(cell);
                        if (v != null) {
                            lines.add(sn + " r" + row.getRowNum() + "c" + cell.getColumnIndex() + " = " + v);
                        }
                    }
                }
            }
            return String.join("\n", lines) + "\n";
        } catch (IOException e) {
            throw new IllegalStateException("xlsx 를 읽을 수 없습니다", e);
        }
    }

    private static String value(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().isEmpty() ? null : "S:" + cell.getStringCellValue();
            case NUMERIC -> "N:" + cell.getNumericCellValue();
            case BOOLEAN -> "B:" + cell.getBooleanCellValue();
            case FORMULA -> "F:" + cell.getCellFormula();
            default -> null;
        };
    }
}
