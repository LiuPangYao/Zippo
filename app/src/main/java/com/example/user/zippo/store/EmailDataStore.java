package com.example.user.zippo.store;

import android.content.Context;

import com.example.greendao.ZI_EmailData;
import com.example.greendao.ZI_EmailDataDao;
import com.example.user.zippo.custom.DaoManager;

import java.util.List;

public class EmailDataStore
{
    private DaoManager manager;

    public EmailDataStore(Context context)
    {
        manager = DaoManager.getInstance();
        manager.init(context);
    }

    public List<ZI_EmailData> getList()
    {
        ZI_EmailDataDao daoList = manager.getDaoSession().getZI_EmailDataDao();
        List<ZI_EmailData> emailData = daoList.queryBuilder()
                .orderAsc(ZI_EmailDataDao.Properties.Sort)
                .list();
        return emailData;
    }

    public void deleteAll()
    {
        ZI_EmailDataDao daoList = manager.getDaoSession().getZI_EmailDataDao();
        daoList.deleteAll();
    }

    /**
     * 新增資料，會先清除舊資料
     */
    public void storeEmailList(List<ZI_EmailData> aList)
    {
        ZI_EmailDataDao daoList = manager.getDaoSession().getZI_EmailDataDao();

        ZI_EmailDataDao.dropTable(daoList.getDatabase(), true);
        ZI_EmailDataDao.createTable(daoList.getDatabase(), true);

        for (ZI_EmailData data : aList)
        {
            // 新增紀錄
            ZI_EmailData insertItem = new ZI_EmailData(null, data.getSort(), data.getEmail());
            daoList.insert(insertItem);
        }
    }
}
