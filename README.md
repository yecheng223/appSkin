# App一键换肤框架

## 一、目录结构

此工程由三个module构成，分别是app（用于测试换肤效果）、mylibrary（换肤核心代码）、myskinplugin（用于打皮肤包的）。

## 二、使用

1、将mylibrary打成jar包引入工程。
2、在Application中调用```SkinManager.init(this);```完成初始化。
3、调用```SkinManager.getInstance().loadSkin(skinPath);```加载服务端下载的皮肤路径（皮肤是.apk格式的文件）。
4、如需还原皮肤则调用```SkinManager.getInstance().loadSkin("");```
5、制作皮肤包上传服务器，注意需要换肤的文件资源名称需一致。
6、说明：本项目支持activity和fragment的所有原生控件UI属性的修改，目前支持的属性如下，如需增加可到SkinAttribute类中自行增加。
``` static {
           mAttributes.add("background");
           mAttributes.add("src");
           mAttributes.add("textColor");
           mAttributes.add("drawableLeft");
           mAttributes.add("drawableTop");
           mAttributes.add("drawableRight");
           mAttributes.add("drawableBottom");
       }
```
7、系统状态栏和底部导航栏颜色修改是SkinThemeUtils，可修改类里面声明的颜色属性。

自定义View的自定义属性需实现SkinViewSupport接口，具体方法参考【app】-【CustomView2】。

## 三、实现原理
1、检测每个activity创建时间，将其所有xml中的需要换肤的控件存储。检测activity方式为继承Application.ActivityLifecycleCallbacks。对应实现类是ApplicationActivityLifecycle。
监测View的创建是设置Factory2，根据源码可知，一般Factory2为null，由系统自行创建View，当有Factory2时交由自定义的Factory2创建，对应实现类为SkinLayoutInflaterFactory。
反射将mFactorySet设为true的操作是因为系统只允许Factory设置一次，设置完成后将改为false，这样修改可以让app在启动时连续换肤。
```
//获得Activity的布局加载器
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //使用factory2 设置布局加载工程
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory
                (activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);

        mObserable.addObserver(skinLayoutInflaterFactory);
```

2、SkinLayoutInflaterFactory实现观察者，SKinManager实现被观察者，当activity创建时，通知观察者换肤。

## 四、重要知识点

1、获取插件apk中的res资源AssetManager。
```
//反射创建AssetManager 与 Resource
                AssetManager assetManager = AssetManager.class.newInstance();
                //资源路径设置 目录或压缩包
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                        String.class);
                addAssetPath.invoke(assetManager, skinPath);

                //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
                Resources skinResource = new Resources(assetManager, appResource.getDisplayMetrics
                        (), appResource.getConfiguration());
```
由源码可知App资源管理是由AssetManager实现的，外部包装了一个Resources，只要反射获取AssetManager的addAssetPath方法，并将皮肤包路径当做参数调用addAssetPath方法，再用
AssetManager构造一个皮肤包的Resources就可以用来获取皮肤包的res资源。
换肤原理是由主App的资源id获知资源名称，再用资源名称去找皮肤包的资源id，最终获取同名对应res资源。

2、获取每个界面需要更换皮肤的所有View的所有属性。
```
public final View tryCreateView(@Nullable View parent, @NonNull String name,
        @NonNull Context context,
        @NonNull AttributeSet attrs) {
        if (name.equals(TAG_1995)) {
            // Let's party like it's 1995!
            return new BlinkLayout(context, attrs);
        }

        View view;
        if (mFactory2 != null) {
            view = mFactory2.onCreateView(parent, name, context, attrs);
        } else if (mFactory != null) {
            view = mFactory.onCreateView(name, context, attrs);
        } else {
            view = null;
        }

        if (view == null && mPrivateFactory != null) {
            view = mPrivateFactory.onCreateView(parent, name, context, attrs);
        }

        return view;
    }
```
LayoutInflater源码可知，在创建View时，如果factory不存在就走最下面自己的mPrivateFactory创建View，这个factory就是系统预留的接口，方便开发者拦截View的创建。
实现Factory2接口，重写CreatView方法，并将View中需要换肤的属性记录。

**声明：本项目只经过简单测试，没有商用，可能有不完善的地方，欢迎指正。**