package com.mikkimesser;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;

public class SelenideFilesTest {

    @DisplayName("Проверка содержимого загруженного текстового файла")
    @Test
    public void selenideDownloadTest() throws Exception {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile))
        {
            byte[] fileContents = is.readAllBytes();
            assertThat(new String(fileContents, StandardCharsets.UTF_8)).contains("This");
        }

    }
}
