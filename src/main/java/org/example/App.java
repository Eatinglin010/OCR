package org.example;

import ocr.TesseractOCR;
import ocr.TesseractOCRProcessor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        TesseractOCR tesseractOCR = new TesseractOCR();
        TesseractOCRProcessor processor = new TesseractOCRProcessor();
        processor.preprocessImageForOCR("images","processedTxt");
//        tesseractOCR.preprocessImageForOCR();
    }
}
