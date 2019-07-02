package com.qiao_techcomponent.pluginload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/****
 * 获取插件的加载类等信息
 */
public class PluginLoadManager {

    private static final PluginLoadManager ourInstance = new PluginLoadManager();


    //Dex包加载
    private DexClassLoader mDexClassLoader;
    //资源加载
    private Resources mResources;
    private Context mContext;
    private String enterActivityName;


    public String getEnterActivityName() {
        return enterActivityName;
    }

    public void setEnterActivityName(String enterActivityName) {
        this.enterActivityName = enterActivityName;
    }



    public Resources getmResources() {
        return mResources;
    }

    public void setmResources(Resources mResources) {
        this.mResources = mResources;
    }


    public DexClassLoader getmDexClassLoader() {
        return mDexClassLoader;
    }

    public void setmDexClassLoader(DexClassLoader mDexClassLoader) {
        this.mDexClassLoader = mDexClassLoader;
    }


    public static PluginLoadManager getInstance() {
        return ourInstance;
    }

    private PluginLoadManager() {

    }


    /***
     * @Deprecated
      public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
     this(null);
     mResourcesImpl = new ResourcesImpl(assets, metrics, config, new DisplayAdjustments());
    }
     * @param apkPath
     */
    /***
     * 根据apk的路径获取 mDexClassLoader assetManager Resources
     * @param apkPath
     */
    public void loadPath (String apkPath) {

        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
        enterActivityName = packageInfo.activities[0].name;

        File outCachePath = mContext.getDir("dex",Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(apkPath,outCachePath.getAbsolutePath(),null,mContext.getClassLoader());
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mResources = new Resources(assetManager,mContext.getResources().getDisplayMetrics()
                ,mContext.getResources().getConfiguration());
        parseReceivers(apkPath,mContext);
    }


    /***
     * 解决插件广播的静态注册问题
     * @param apkPath
     * @param context
     *
     * Package 里有List<Activity>  receiver 里有 Activity里有 List<xx extends IntentInfo> intents
     * 请查看PMS 和 PackageParser源码
     *
     * public final ArrayList<Activity> receivers = new ArrayList<Activity>(0);
     * Activity extends Component<ActivityIntentInfo>
     *     public static class Component<II extends IntentInfo> {
           public final Package owner;
           public final ArrayList<II> intents;}

           IntentInfo extends IntentFilter


           UserHandle getCallingUserId () 方法获取userId
     */
    private void parseReceivers (String apkPath,Context context) {

        try {
            Class packageParseClazz = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParseClazz.getDeclaredMethod("parsePackage",File.class,int.class);
            Object packageParseInstance = packageParseClazz.newInstance();
            Object packageObj = parsePackageMethod.invoke(packageParseInstance,new File(apkPath),PackageManager.GET_ACTIVITIES);

            //注册的广播集合
            Field receiverField = packageObj.getClass().getField("receivers");
            Class<?> componentClazz = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClazz.getDeclaredField("intents");

            /**
             * public static final ActivityInfo generateActivityInfo(Activity a, int flags,
             PackageUserState state, int userId)
             */
            List receivers = (List) receiverField.get(packageObj);


            /***
             * 生成ActivityInfo对象，ActivityInfo对象里有name字段，这个字段表示从androidmanifest里这册的广播name
             */

            Class<?> packageParserActivityClazz = Class.forName("android.content.pm.PackageParser$Activity");
            Class<?> packageUserStateClazz = Class.forName("android.content.pm.PackageUserState");
            Object packageUserStateInstance = packageUserStateClazz.newInstance();
            Method generateActivityInfo = packageParseClazz.getDeclaredMethod("generateActivityInfo"
                    ,packageParserActivityClazz,int.class,packageUserStateClazz,int.class);
            //请查看UserHandle源码，getCallingUserId方法获取宿主进程id，是个静态方法
            Class<?> userHandle = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandle.getDeclaredMethod("getCallingUserId");
            //静态方法，对象可传入空
            int userId = (int)getCallingUserIdMethod.invoke(null);




            for (Object activity:receivers) {
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfo.invoke(packageParseInstance,activity,
                        0,packageUserStateInstance,userId);
                /***
                 * 将插件的BroadCastReceiver加载进来
                 */
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) mDexClassLoader.loadClass(activityInfo.name).newInstance();
                List<? extends IntentFilter> intentFilters = (List<? extends IntentFilter>)intentsField.get(activity);
                for (IntentFilter intentFilter:intentFilters) {
                    /***
                     * 再动态注册一下，相当于给插件静态注册的广播解析出来，然后运行时动态注册进去
                     */
                    context.registerReceiver(broadcastReceiver,intentFilter);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setContext (Context context) {
        mContext = context.getApplicationContext();
    }
}
