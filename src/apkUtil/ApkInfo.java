package apkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>ApkInfo</B>
 * <p>
 * 该类封装了一个Apk的信息。包括版本号，支持平台，图标，名称，权限，所需设备特性等。
 * </p>
 * <p>
 * Created by zhangyakun on 17/11/27.
 */
public class ApkInfo {
    public static final String APPLICATION_ICON_240 = "application-icon-240";//hdpi
    public static final String APPLICATION_ICON_320 = "application-icon-320";//xhdpi
    public static final String APPLICATION_ICON_480 = "application-icon-480";//xxhdpi
    public static final String APPLICATION_ICON_640 = "application-icon-640";//xxxhdpi
    /**
     * apk内部版本号
     */
    private String versionCode = null;
    /**
     * apk外部版本号
     */
    private String versionName = null;
    /**
     * apk的包名
     */
    private String packageName = null;
    /**
     * 支持的android平台最低版本号
     */
    private String minSdkVersion = null;
    /**
     * apk所需要的权限
     */
    private List<String> usesPermissions = null;

    /**
     * 支持的SDK版本。
     */
    private String sdkVersion;
    /**
     * 建议的SDK版本
     */
    private String targetSdkVersion;
    /**
     * 应用程序名
     */
    private String applicationLable;
    /**
     * 各个分辨率下的图标的路径。
     */
    private Map<String, String> applicationIcons;

    /**
     * 程序的图标。
     */
    private String applicationIcon;

    /**
     * 所需设备特性。
     */
    private List<String> features;
    /**
     * 启动界面
     */
    private String launchableActivity;

    public ApkInfo() {
        this.usesPermissions = new ArrayList<>();
        this.applicationIcons = new HashMap<>();
        this.features = new ArrayList<>();
    }

    /**
     * 返回版本代码。
     *
     * @return 版本代码。
     */
    public String getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode the versionCode to set
     */
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 返回版本名称。
     *
     * @return 版本名称。
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName the versionName to set
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 返回支持的最小sdk平台版本。
     *
     * @return the minSdkVersion
     */
    public String getMinSdkVersion() {
        return minSdkVersion;
    }

    /**
     * @param minSdkVersion the minSdkVersion to set
     */
    public void setMinSdkVersion(String minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    /**
     * 返回包名。
     *
     * @return 返回的包名。
     */
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 返回sdk平台版本。
     *
     * @return
     */
    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    /**
     * 返回所建议的SDK版本。
     *
     * @return
     */
    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    /**
     * 返回所需的用户权限。
     *
     * @return
     */
    public List<String> getUsesPermissions() {
        return usesPermissions;
    }

    public void setUsesPermissions(List<String> usesPermission) {
        this.usesPermissions = usesPermission;
    }

    public void addToUsesPermissions(String usesPermission) {
        this.usesPermissions.add(usesPermission);
    }

    /**
     * 返回程序的名称标签。
     *
     * @return
     */
    public String getApplicationLable() {
        return applicationLable;
    }

    public void setApplicationLable(String applicationLable) {
        this.applicationLable = applicationLable;
    }

    /**
     * 返回应用程序的图标。res/mipmap-mdpi-v4/ic_launcher.png(没有设置的话是小机器人)这个方法谨慎使用
     *
     * @return
     */
    public String getApplicationIcon() {
        return applicationIcon;
    }

    public void setApplicationIcon(String applicationIcon) {
        this.applicationIcon = applicationIcon;
    }

    /**
     * 返回应用程序各个分辨率下的图标。
     *
     * @return
     */
    public Map<String, String> getApplicationIcons() {
        return applicationIcons;
    }

    public void setApplicationIcons(Map<String, String> applicationIcons) {
        this.applicationIcons = applicationIcons;
    }

    public void addToApplicationIcons(String key, String value) {
        this.applicationIcons.put(key, value);
    }

    /**
     * 返回应用程序所需的特性。
     *
     * @return
     */
    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public void addToFeatures(String feature) {
        this.features.add(feature);
    }

    @Override
    public String toString() {
        return "ApkInfo [versionCode=" + versionCode + ",\n versionName="
                + versionName + ",\n packageName=" + packageName
                + ",\n minSdkVersion=" + minSdkVersion + ",\n usesPermissions="
                + usesPermissions + ",\n sdkVersion=" + sdkVersion
                + ",\n targetSdkVersion=" + targetSdkVersion
                + ",\n applicationLable=" + applicationLable
                + ",\n applicationIcons=" + applicationIcons
                + ",\n applicationIcon=" + applicationIcon
                + ",\n features=" + features
                + ",\n launchableActivity=" + launchableActivity + "\n]";
    }

    public String getLaunchableActivity() {
        return launchableActivity;
    }

    public void setLaunchableActivity(String launchableActivity) {
        this.launchableActivity = launchableActivity;
    }

}
