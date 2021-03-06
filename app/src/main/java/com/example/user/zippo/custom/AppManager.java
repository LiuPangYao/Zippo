package com.example.user.zippo.custom;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 應用程序 Activity 管理類
 * @author  liux
 */
public class AppManager
{
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager()
    {

    }

    /**
     * 單一實例
     */
    public static AppManager getAppManager()
    {
        if(instance == null)
        {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆棧
     */
    public void addActivity(Activity activity)
    {
        if(activityStack == null)
        {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 獲取當前Activity（堆棧中最後一個壓入的）
     */
    public Activity currentActivity()
    {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 結束當前Activity（堆棧中最後一個壓入的）
     */
    public void finishActivity()
    {
        Activity activity = activityStack.lastElement();
        if(activity != null)
        {
            activity.finish();
            activity=null;
        }
    }

    /**
     * 結束指定的Activity
     */
    public void finishActivity(Activity activity)
    {
        if(activity != null)
        {
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 結束指定類名的Activity
     */
    public void finishActivity(Class<?> cls)
    {
        for (Activity activity : activityStack)
        {
            if(activity.getClass().equals(cls) )
            {
                finishActivity(activity);
            }
        }
    }

    /**
     * 結束所有Activity
     */
    public void finishAllActivity()
    {
        for (int i = 0, size = activityStack.size(); i < size; i++)
        {
            if (null != activityStack.get(i))
            {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出應用程序
     */
    public void AppExit(Context context)
    {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }
}
