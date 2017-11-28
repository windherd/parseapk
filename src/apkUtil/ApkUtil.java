package apkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * apk工具类。封装了获取Apk信息的方法。
 * <p>
 * Created by zhangyakun on 17/11/27.
 */
public class ApkUtil {
    private static class ApkUtilHolder {
        private static final ApkUtil INSTANCE = new ApkUtil();
    }

    public static final String VERSION_CODE = "versionCode";
    public static final String VERSION_NAME = "versionName";
    public static final String SDK_VERSION = "sdkVersion";
    public static final String TARGET_SDK_VERSION = "targetSdkVersion";
    public static final String USES_PERMISSION = "uses-permission";
    public static final String APPLICATION_LABEL = "application-label:";
    public static final String APPLICATION_ICON = "application-icon";
    public static final String USES_FEATURE = "uses-feature";
    public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
    public static final String SUPPORTS_SCREENS = "supports-screens";
    public static final String SUPPORTS_ANY_DENSITY = "supports-any-density";
    public static final String DENSITIES = "densities";
    public static final String PACKAGE = "package";
    public static final String APPLICATION = "application:";
    public static final String LAUNCHABLE_ACTIVITY = "launchable-activity";

    private ProcessBuilder mBuilder;
    private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
    private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";
    /**
     * aapt所在的目录。
     */
    //    private String mAaptPath = "lib/aapt";
    private String mAaptPath = "/usr/local/python/img/aapt";

    private ApkUtil() {
        mBuilder = new ProcessBuilder();
        mBuilder.redirectErrorStream(true);
    }

    public static ApkUtil getInstance() {
        return ApkUtilHolder.INSTANCE;
    }

    /**
     * 返回一个apk程序的信息。
     *
     * @param apkPath apk的路径。
     * @return apkInfo 一个Apk的信息。
     */
    public ApkInfo getApkInfo(String apkPath) throws Exception {
        if (mBuilder == null) {
            throw new Exception("please instance ApkUtil use getInstance() method");
        }
        Process process = mBuilder.command("aapt", "d", "badging", apkPath)
                .start();
        InputStream is = null;
        is = process.getInputStream();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, "utf8"));
        String tmp = br.readLine();
        try {
            if (tmp == null || !tmp.startsWith("package")) {
                throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
            }
            ApkInfo apkInfo = new ApkInfo();
            do {
                setApkInfoProperty(apkInfo, tmp);
            } while ((tmp = br.readLine()) != null);
            return apkInfo;
        } catch (Exception e) {
            throw e;
        } finally {
            process.destroy();
            closeIO(is);
            closeIO(br);
        }
    }

    /**
     * 设置APK的属性信息。
     *
     * @param apkInfo
     * @param source
     */
    private void setApkInfoProperty(ApkInfo apkInfo, String source) {
        if (source.startsWith(PACKAGE)) {
            splitPackageInfo(apkInfo, source);
        } else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
            apkInfo.setLaunchableActivity(getPropertyInQuote(source));
        } else if (source.startsWith(SDK_VERSION)) {
            apkInfo.setSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(TARGET_SDK_VERSION)) {
            apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(USES_PERMISSION)) {
            apkInfo.addToUsesPermissions(getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION_LABEL)) {
            apkInfo.setApplicationLable(getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION_ICON)) {
            apkInfo.addToApplicationIcons(getKeyBeforeColon(source),
                    getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION)) {
            String[] rs = source.split("( icon=')|'");
            apkInfo.setApplicationIcon(rs[rs.length - 1]);
        } else if (source.startsWith(USES_FEATURE)) {
            apkInfo.addToFeatures(getPropertyInQuote(source));
        } else {
            //			System.out.println(source);
        }
    }

    /**
     * 返回出格式为name: 'value'中的value内容。
     *
     * @param source
     * @return
     */
    private String getPropertyInQuote(String source) {
        int index = source.indexOf("'") + 1;
        return source.substring(index, source.indexOf('\'', index));
    }

    /**
     * 返回冒号前的属性名称
     *
     * @param source
     * @return
     */
    private String getKeyBeforeColon(String source) {
        return source.substring(0, source.indexOf(':'));
    }

    /**
     * 分离出包名、版本等信息。
     *
     * @param apkInfo
     * @param packageSource
     */
    private void splitPackageInfo(ApkInfo apkInfo, String packageSource) {
        String[] packageInfo = packageSource.split(SPLIT_REGEX);
        apkInfo.setPackageName(packageInfo[2]);
        apkInfo.setVersionCode(packageInfo[4]);
        apkInfo.setVersionName(packageInfo[6]);
    }

    /**
     * 释放资源。
     *
     * @param c 将关闭的资源
     */
    private final void closeIO(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getmAaptPath() {
        return mAaptPath;
    }

    public void setmAaptPath(String mAaptPath) {
        this.mAaptPath = mAaptPath;
    }

    //********************************* iOS **************************************

    /**
     * @param url 应用itunes地址
     * @return
     */
    public ApkInfo getIOSInfo(String url) throws Exception {
        String id = "";
        String regex = "\\d+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            id = matcher.group();
        } else {
            return null;
        }
        String s = HttpHelper.sendGet("https://itunes.apple.com/lookup", "id=" + id);
        JSONObject jsonObject = null;
        ApkInfo apkInfo = new ApkInfo();
        try {
            jsonObject = new JSONObject(s);
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = results.getJSONObject(0);
            String bundleId = jsonObject1.getString("bundleId");
            String trackName = jsonObject1.getString("trackName");
            String version = jsonObject1.getString("version");
            String icon = jsonObject1.getString("artworkUrl512");
            apkInfo.setPackageName(bundleId);
            apkInfo.setApplicationLable(trackName);
            apkInfo.setVersionName(version);
            apkInfo.addToApplicationIcons(ApkInfo.APPLICATION_ICON_320, icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkInfo;
    }

    public static void main(String[] args) {
        try {
            String demo = "/Users/zhangyakun/Desktop/iBiliPlayer-bili.apk";
            ApkInfo apkInfo = ApkUtil.getInstance().getApkInfo(demo);
            System.out.println(apkInfo);
            String url = "https://itunes.apple.com/lookup?id=414478124";
            ApkInfo iOSInfo = ApkUtil.getInstance().getIOSInfo(url);
            System.out.println(iOSInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
