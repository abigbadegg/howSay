package com.howsay.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.darcytech.doris.alert.tool.Alerter;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("test")
public class Test {

    public static Integer FOREACH_TIMES = 10000;
    @GetMapping("test")
    public String test() {
        List<Integer> list = new ArrayList<>();

        log.warn("xxx");
        throw new UnsupportedOperationException("不支持的操作");
    }

    public static void main(String[] args) {
        txtFileExport();
        excelFileExport();

    }

    private static void txtFileExport() {
        StopWatch st = new StopWatch("txtFileExport");

        st.start("write txtFileExport");
        File chatpeerIdsFile = new File("D:\\data\\txtFileExport.xlsx");
        try (FileWriter writer = new FileWriter(chatpeerIdsFile, true); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (int i = 0; i < FOREACH_TIMES; i++) {
                String joinIds = resolveIds();
                bufferedWriter.write(joinIds);
                bufferedWriter.newLine();
            }
        } catch (Exception  e) {
            throw new RuntimeException("Failed to write IDs to the temporary file.", e);
        }
        st.stop();

        st.start("read txtFileExport");
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\data\\txtFileExport.xlsx"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Long> chatpeerIds = Splitter.on(",").splitToList(line).stream().map(Long::valueOf).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read IDs from the temporary file.", e);
        }
        st.stop();

        System.out.println(st.prettyPrint());
    }

    private static void excelFileExport() {
        StopWatch st = new StopWatch("excelFileExport");

        st.start("write excelFileExport");
        File chatpeerIdsFile = new File("D:\\data\\excelFileExport.xlsx");
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            SXSSFSheet sheet = workbook.createSheet("chatpeerIds");
            String joinIds =  resolveIds();
            for (int i = 0; i < FOREACH_TIMES; i++) {
                SXSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
                SXSSFCell cell = row.createCell(0);
                cell.setCellValue(joinIds);
            }
            workbook.write(Files.newOutputStream(chatpeerIdsFile.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        st.stop();

        st.start("read excelFileExport");
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(chatpeerIdsFile))) {
            SXSSFSheet sheet = workbook.getSheet("chatpeerIds");
            if (sheet == null) {
                return;
            }

            for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
                String line = sheet.getRow(i).getCell(0).toString();
                List<Long> chatpeerIds = Splitter.on(",").splitToList(line).stream().map(Long::valueOf).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read IDs from the temporary file.", e);
        }
        st.stop();

        System.out.println(st.prettyPrint());
    }

    private static String resolveIds() {
        List<Long> ids = new ArrayList<>();
        for(int i = 0; i < 256; i++) {
            ids.add(RandomUtils.nextLong());
        }
        return Joiner.on(',').join(ids);
    }
}
