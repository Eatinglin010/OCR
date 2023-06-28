package ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class TesseractOCR {
    public void preprocessImageForOCR() {
        // 指定圖片資料夾路徑和輸出文本檔資料夾路徑
        String tessDataPath = "resource/Tesseract";
        String imagePath = "images";
        String outputFolderPath = "processedTxt";

        // 指定待辨識圖片副檔名
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".bmp"};

        // 初始化 Tesseract 物件
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("eng");

        // 建立輸出文本檔資料夾
        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        Arrays.stream(Objects.requireNonNull(new File(imagePath).listFiles()))
                // 過濾只保留圖片檔案
                .filter(file -> file.isFile() && hasValidExtension(file.getName(), validExtensions))
                // 對每個圖片檔案進行處理
                .forEach(imageFile -> {
                    String imageFileName = imageFile.getName();
                    System.out.println("開始辨識檔案：" + imageFileName);

                    try {
                        // 使用 Tesseract 進行圖片辨識
                        String result = tesseract.doOCR(imageFile);

                        // 構建輸出文本檔的路徑
                        String outputFilePath = outputFolderPath + "/" + getBaseFileName(imageFileName) + ".txt";

                        // 將辨識結果輸出為文本檔
                        File outputFile = new File(outputFilePath);
                        FileWriter writer = new FileWriter(outputFile);
                        writer.write(result);
                        writer.close();

                        System.out.println("已生成檔案：" + outputFilePath);
                    } catch (TesseractException | IOException e) {
                        System.err.println("處理檔案時發生錯誤：" + imageFileName);
                        e.printStackTrace();
                    }
                });
    }


    private static String getBaseFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }

    private static boolean hasValidExtension(String fileName, String[] validExtensions) {
        for (String extension : validExtensions) {
            if (fileName.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
