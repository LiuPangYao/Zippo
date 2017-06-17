package com.example.user.zippo.custom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendao.DaoMaster;
import com.example.greendao.DaoSession;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 封裝
 */
public class DaoManager
{
    private static final String TAG = DaoManager.class.getSimpleName();
    private static final String DB_NAME = "zippo.db"; //數據庫名稱
    private volatile static DaoManager mDaoManager; //多線程訪問
    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static SQLiteDatabase db;
    private Context context;

    /**
     * 使用單例模式獲得操作數據庫的對象
     * @return
     */
    public static DaoManager getInstance()
    {
        DaoManager instance = null;
        if (mDaoManager == null)
        {
            synchronized (DaoManager.class)
            {
                if (instance == null)
                {
                    instance = new DaoManager();
                    mDaoManager = instance;
                }
            }
        }
        return mDaoManager;
    }

    /**
     * 初始化Context對象
     * @param context
     */
    public void init(Context context)
    {
        this.context = context;
    }

    /**
     * 判斷數據庫是否存在，如果不存在則創建
     * @return
     */
    public DaoMaster getDaoMaster()
    {
        if (null == mDaoMaster)
        {
            mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成對數據庫的增刪查找
     * @return
     */
    public DaoSession getDaoSession()
    {
        if (null == mDaoSession)
        {
            if (null == mDaoMaster)
            {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 設置debug模式開啟或關閉，默認關閉
     * @param flag
     */
    public void setDebug(boolean flag)
    {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 關閉數據庫
     */
    public void closeDataBase()
    {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession()
    {
        if (null != mDaoSession)
        {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper()
    {
        if (mHelper != null)
        {
            mHelper.close();
            mHelper = null;
        }
    }
}

