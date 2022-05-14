package com.mikkimesser;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

import static com.codeborne.selenide.Selenide.$;

public class ParsingFilesTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @DisplayName("Парсинг PDF файла")
    @Test
    public void parsePDFTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedFile = $(By.linkText("PDF download")).download();
        PDF pdf = new PDF(downloadedFile);

        assertThat(pdf.author).contains("Matthias Merdes");
    }

    @DisplayName("Парсинг XLS файла")
    @Test
    public void parseXLSFile() throws Exception{
        Selenide.open("https://ckmt.ru/price-download.html");
        File downloadedFile = $("a[href*='Tehresurs']").download();

        XLS xls = new XLS(downloadedFile);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(8)
                .getCell(5)
                .getStringCellValue())
                .isEqualTo("от 150 000р");
    }

    @DisplayName("Парсинг CSV файла")
    @Test
    public void parseCSVFile() throws Exception{
        try (InputStream is = classLoader.getResourceAsStream("csv/contacts_example_test.csv");
             CSVReader csvReader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> csvContent = csvReader.readAll();
            assertThat(csvContent.get(1)).contains("Kibbutz");
        }
    }
    @Test
    public void parseZipSimpleTest() throws Exception{
        try (InputStream is = classLoader.getResourceAsStream("zip/archive.zip");
             ZipInputStream zis = new ZipInputStream(is))
        {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null){
                //simpleChecks
            }
        }
    }
}
