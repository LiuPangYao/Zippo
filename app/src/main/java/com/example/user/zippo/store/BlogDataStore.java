package com.example.user.zippo.store;

import android.content.Context;

import com.example.greendao.ZI_BlogData;
import com.example.greendao.ZI_BlogDataDao;
import com.example.user.zippo.custom.DaoManager;

import java.util.List;

public class BlogDataStore
{
    private DaoManager manager;

    public BlogDataStore(Context context)
    {
        manager = DaoManager.getInstance();
        manager.init(context);
    }

    public List<ZI_BlogData> getList()
    {
        ZI_BlogDataDao daoList = manager.getDaoSession().getZI_BlogDataDao();
        List<ZI_BlogData> noteData = daoList.queryBuilder()
                .orderAsc(ZI_BlogDataDao.Properties.Sort)
                .list();
        return noteData;
    }

    public void deleteAll()
    {
        ZI_BlogDataDao daoList = manager.getDaoSession().getZI_BlogDataDao();
        daoList.deleteAll();
    }

    /**
     * 新增資料，會先清除舊資料
     */
    public void storeBlogList(List<ZI_BlogData> aList)
    {
        ZI_BlogDataDao daoList = manager.getDaoSession().getZI_BlogDataDao();

        ZI_BlogDataDao.dropTable(daoList.getDatabase(), true);
        ZI_BlogDataDao.createTable(daoList.getDatabase(), true);

        for (ZI_BlogData data : aList)
        {
            // 新增紀錄
            ZI_BlogData insertItem = new ZI_BlogData(null, data.getSort(), data.getBlog());
            daoList.insert(insertItem);
        }
    }
}
