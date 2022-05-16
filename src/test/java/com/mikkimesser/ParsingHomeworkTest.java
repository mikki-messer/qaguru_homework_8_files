package com.mikkimesser;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;


public class ParsingHomeworkTest {
    @DisplayName("Парсинг zip-архива и проверка содержимого pdf, csv и xls файлов")
    @Test
    public void parseZipComplexTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/zip/archive.zip"));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            try (InputStream entryInputStream = zipFile.getInputStream(entry)) {
                String name = entry.getName();
                if (name.startsWith("__MACOSX"))
                    continue;
                String extension = FilenameUtils.getExtension(name);
                switch (extension) {
                    case "xls":
                        XLS xls = new XLS(entryInputStream);
                        assertThat(xls.excel
                                .getSheetAt(0)
                                .getRow(9)
                                .getCell(0)
                                .getStringCellValue())
                                .isEqualTo("Электроды сварочные \"Орловские\" (Lincoln Eleсtric, Межгосметиз-Мценск)");
                        break;
                    case "csv":
                        try (CSVReader csvReader = new CSVReader(new InputStreamReader(entryInputStream))) {
                            List<String[]> csvContent = csvReader.readAll();
                            assertThat(csvContent.get(1)).contains("Kibbutz");
                        }
                        break;
                    case "pdf":
                        PDF pdf = new PDF(entryInputStream);
                        assertThat(pdf.text).contains("Setting the Default Display Name Generator");
                        break;
                    default:
                        System.out.println("Unsupported file format");
                        break;
                }
            }
        }
    }
}
