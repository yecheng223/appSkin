package com.cy.mylibrary.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by chuyang on 2021/3/2.
 */
public class SkinResources {
    /**
     * 皮肤包名
     */
    private String mSkinPkgName;
    /**
     * 是否是默认的皮肤
     */
    private boolean isDefaultSkin = true;
    /**
     * 宿主app的Resources
     */
    private Resources mAppResources;
    /**
     * 皮肤包的Resources
     */
    private Resources mSkinResources;

    private volatile static SkinResources instance;

    private SkinResources(Context context) {

    }


    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
    }

    public static SkinResources getInstance() {
        return instance;
    }

    public void reset() {
        mSkinPkgName = "";
        mSkinResources = null;
        isDefaultSkin = true;
    }

    public void applySkin(Resources resources, String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 通过主app资源id获取对应的插件资源id
     *
     * @param resId
     * @return
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    /**
     * 获取颜色，如果皮肤包没有资源就返回主app的资源，有资源返回皮肤包的资源
     *
     * @param resId
     * @return
     */
    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    /**
     * 获取颜色状态列表
     *
     * @param resId
     * @return
     */
    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    /**
     * 获取Drawable
     *
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }

    /**
     * 获取Background
     *
     * @param resId
     * @return
     */
    public Object getBackground(int resId) {
        String resourcesTypeName = mAppResources.getResourceTypeName(resId);

        if ("color".equals(resourcesTypeName)) {
            return getColor(resId);
        } else {
            return getDrawable(resId);
        }
    }
}
