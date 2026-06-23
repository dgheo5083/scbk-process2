package com.scbank.process.api.svc.shared.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ZipUtils {
    /**
     * 파일 압축
     * 
     * @param dirPath
     * @param zipFileName
     * @param targetFileList
     * @throws IOException
     */
    public static void zip(String dirPath, String zipFileName, List<String> targetFileList) throws IOException {

        byte[] buffer = new byte[1024];

        String filePath = SecurityFilterUtils.getPMFilter(dirPath + zipFileName);

        try (FileOutputStream out = new FileOutputStream(filePath);
                ZipOutputStream zipOut = new ZipOutputStream(out);) {

            for (String filename : targetFileList) {
                File inFile = new File(dirPath + filename);

                try (FileInputStream in = new FileInputStream(inFile);) {

                    ZipEntry zipEntry = new ZipEntry(inFile.getName());
                    zipOut.putNextEntry(zipEntry);

                    int len = 0;
                    while ((len = in.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, len);
                    }
                }

            }
        }
    }

    /**
     * 파일 압축 해제
     * 
     * @param dirPath
     * @param zipFileName
     * @return
     * @throws IOException
     */
    public static List<String> unzip(String dirPath, String zipFileName) throws IOException {
        List<String> unzipFileList = new ArrayList<String>();

        byte[] buf = new byte[1024];

        try (FileInputStream in = new FileInputStream(dirPath + zipFileName);
                ZipInputStream zipIn = new ZipInputStream(in);) {
            ZipEntry zipEntry = null;

            while ((zipEntry = zipIn.getNextEntry()) != null) {
                String filePath = SecurityFilterUtils.getPMFilter(dirPath + zipEntry.getName());
                try (OutputStream out = new FileOutputStream(filePath);) {
                    int len = 0;
                    while ((len = zipIn.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    unzipFileList.add(zipEntry.getName());
                }
            }
        }
        return unzipFileList;
    }
}
