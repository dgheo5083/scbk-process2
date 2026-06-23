package com.scbank.process.api.svc.shared.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class FileUtils {

    /**
     * 디렉토리 생성
     * 
     * @param dirPath
     * @return boolean true or false
     */
    public boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            return true;
        } else {
            return file.mkdirs();
        }
    }

    /**
     * 파일여부 체크
     * 
     * @param filePath
     * @return boolean true or false
     */
    public boolean checkFile(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    /**
     * 파일 삭제
     * 
     * @param filePath
     * @return boolean true or false
     */
    public boolean fileDelete(String filePath) {
        filePath = SecurityFilterUtils.getPMFilter(filePath);
        File f = new File(filePath);
        return f.delete();
    }

    /**
     * 파일 저장
     * 
     * @param path : 파일저장경로
     * @param data : 파일데이터
     * @return void
     */
    public void fileSave(String path, String data) {

        BufferedWriter bw = null;
        OutputStreamWriter osw = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path);
            osw = new OutputStreamWriter(fos, "utf-8");
            bw = new BufferedWriter(osw);
            bw.write(data);

        } catch (IOException e) {
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (Exception e) {
                }
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (Exception e) {
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 파일명 변경
     * 
     * @param filePath    : 원파일명
     * @param newFilePath : 변경할 파일명
     * @param overWrite   : 덮어쓰기 여부
     * @return boolean true or false
     */
    public boolean rename(String filePath, String newFilePath, boolean overWrite) {
        boolean result = false;
        filePath = SecurityFilterUtils.getPMFilter(filePath);
        newFilePath = SecurityFilterUtils.getPMFilter(newFilePath);
        if (overWrite) {
            // 새로운 파일명으로 이미 있는 파일이 있으면 지운다.
            File f = new File(newFilePath);
            f.delete();
            // 파일명 변경
            File f2 = new File(filePath);
            result = f2.renameTo(new File(newFilePath));
        } else {
            File f = new File(filePath);
            result = f.renameTo(new File(newFilePath));
        }
        return result;
    }

    /**
     * 파일명 변경 : 파일이 둘다 존재할 경우 적용
     * 
     * @param orgPath : 원본파일
     * @param newPath : 변경파일
     * @return boolean true or false
     */
    public boolean renameWithExistFile(String orgPath, String newPath) {
        File orgFile = new File(orgPath);
        File newFile = new File(newPath);
        // 파일이 둘다 존재할 경우만
        if (!orgFile.exists() || !newFile.exists())
            return false;

        // 새로운 파일명으로 이미 있는 파일이 있으면 지운다.
        newFile.delete();

        return orgFile.renameTo(newFile);
    }

    /**
     * 파일 복사
     * 
     * @param orgFileName
     * @param newPath
     * @param newFileName
     * @return
     * @throws IOException
     */
    public boolean copyFile(String orgFileName, String newPath, String newFileName) {
        FileUtils.makeDir(newPath);
        File orgFile = new File(orgFileName);
        if (!orgFile.exists()) {
            return false;
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fcIn = null;
        FileChannel fcOut = null;

        try {
            fis = new FileInputStream(orgFile);
            fos = new FileOutputStream(new File(newPath + newFileName));

            fcIn = fis.getChannel();
            fcOut = fos.getChannel();
            long size = fcIn.size();
            fcIn.transferTo(0, size, fcOut);

        } catch (Exception e) {
            return false;
        } finally {
            if (null != fcOut) {
                try {
                    fcOut.close();
                } catch (Exception e) {
                }
            }
            if (null != fcIn) {
                try {
                    fcIn.close();
                } catch (Exception e) {
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    public static void fldrDelete(String folderPath) {
        File deleteFolder = new File(folderPath);

        try {
            if (deleteFolder.exists()) {
                File[] folder_list = deleteFolder.listFiles(); // 파일리스트 얻어오기
                for (int j = 0; j < folder_list.length; j++) {
                    log.info("folder_list ::: {}", folder_list[j].getPath());
                    if (folder_list[j].isFile()) {
                        folder_list[j].delete();
                    } else {
                        fldrDelete(folder_list[j].getPath());
                    }
                    folder_list[j].delete(); // 파일 삭제
                }
                deleteFolder.delete(); // 파일 삭제
            }
        } catch (Exception e) {
        }
    }
}
