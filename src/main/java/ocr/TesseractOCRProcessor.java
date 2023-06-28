package ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class TesseractOCRProcessor {
    public void preprocessImageForOCR(String inputFolderPath, String outputFolderPath) {
        String tessDataPath = "resource/Tesseract";
        // 初始化 Tesseract 物件
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("eng");
        tesseract.setVariable("user_defined_dpi", "600");

        // 获取输入文件夹下的所有文件和子文件夹
        File inputFolder = new File(inputFolderPath);
        processFiles(inputFolder, outputFolderPath, tesseract);
    }

    private void processFiles(File folder, String outputFolderPath, ITesseract tesseract) {
        // 遍历文件夹下的所有文件和子文件夹
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                // 如果是子文件夹，递归处理
                String subFolderName = file.getName();
                String subOutputFolderPath = outputFolderPath + "/" + subFolderName;
                File subOutputFolder = new File(subOutputFolderPath);
                subOutputFolder.mkdirs(); // 创建对应的输出子文件夹
                processFiles(file, subOutputFolderPath, tesseract);
            } else {
                // 如果是文件，进行处理
                String imageFileName = file.getName();
                System.out.println("开始辨识文件：" + imageFileName);
                try {
                    // 使用 Tesseract 进行图像识别
                    String result = tesseract.doOCR(file);

                    // 构建输出文本文件的路径
                    String outputFilePath = outputFolderPath + "/" + getBaseFileName(imageFileName) + ".txt";

                    // 将识别结果输出为文本文件
                    File outputFile = new File(outputFilePath);
                    FileWriter writer = new FileWriter(outputFile);
                    writer.write(result);
                    writer.close();

                    System.out.println("已生成文件：" + outputFilePath);
                } catch (TesseractException | IOException e) {
                    System.err.println("处理文件时发生错误：" + imageFileName);
                    e.printStackTrace();
                }
            }
        }
    }

    private String getBaseFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }
}
