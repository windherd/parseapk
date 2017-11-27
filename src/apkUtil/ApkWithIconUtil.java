package apkUtil;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 通过ApkInfo 里的applicationIcon从APK里解压出icon图片并存放到磁盘上
 * <p>
 * Created by zhangyakun on 17/11/27.
 */
public class ApkWithIconUtil {

    /**
     * 从指定的apk文件里获取指定file的流
     *
     * @param apkpath
     * @param fileName
     * @return
     */
    private static InputStream extractFileFromApk(String apkpath, String fileName) {
        try {
            ZipFile zFile = new ZipFile(apkpath);
            ZipEntry entry = zFile.getEntry(fileName);
            entry.getComment();
            entry.getCompressedSize();
            entry.getCrc();
            entry.isDirectory();
            entry.getSize();
            entry.getMethod();
            InputStream stream = zFile.getInputStream(entry);
            return stream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void parseApk(String apkpath, String outputPath) {
        String icon320 = "";
        InputStream is = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            ApkInfo apkInfo = ApkUtil.getInstance().getApkInfo(apkpath);
            System.out.println(apkInfo);
            Map<String, String> applicationIcons = apkInfo.getApplicationIcons();
            if (applicationIcons != null) {
                icon320 = applicationIcons.get(ApkInfo.APPLICATION_ICON_320);
            }
            is = extractFileFromApk(apkpath, icon320);
            File file = new File(outputPath);
            bos = new BufferedOutputStream(new FileOutputStream(file), 1024);
            byte[] b = new byte[1024];
            bis = new BufferedInputStream(is, 1024);
            while (bis.read(b) != -1) {
                bos.write(b);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * demo 获取apk文件的icon并写入磁盘指定位置
     *
     * @param args
     */
    public static void main(String[] args) {
        String apkpath = "/Users/zhangyakun/Desktop/iBiliPlayer-bili.apk";
        String outputpath = "/Users/zhangyakun/Desktop/icon.png";
        parseApk(apkpath, outputpath);
    }

}
