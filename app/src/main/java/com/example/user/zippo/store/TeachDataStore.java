package com.example.user.zippo.store;

import android.content.Context;

import com.example.greendao.ZI_TeachData;
import com.example.greendao.ZI_TeachDataDao;
import com.example.user.zippo.custom.DaoManager;

import java.util.List;

/**
 * 教育背景
 */

public class TeachDataStore
{
    private DaoManager manager;

    public TeachDataStore(Context context)
    {
        manager = DaoManager.getInstance();
        manager.init(context);
    }

    public List<ZI_TeachData> getList()
    {
        ZI_TeachDataDao daoList = manager.getDaoSession().getZI_TeachDataDao();
        List<ZI_TeachData> teachData = daoList.queryBuilder()
                .orderAsc(ZI_TeachDataDao.Properties.Id)
                .list();
        return teachData;
    }

    public void deleteAll()
    {
        ZI_TeachDataDao daoList = manager.getDaoSession().getZI_TeachDataDao();
        daoList.deleteAll();
    }

    /**
     * 新增資料，會先清除舊資料
     */
    public void storeTeachList(List<ZI_TeachData> aList)
    {
        ZI_TeachDataDao daoList = manager.getDaoSession().getZI_TeachDataDao();

        ZI_TeachDataDao.dropTable(daoList.getDatabase(), true);
        ZI_TeachDataDao.createTable(daoList.getDatabase(), true);

        for (ZI_TeachData data : aList)
        {
            // 新增紀錄
            ZI_TeachData insertItem = new ZI_TeachData(null, data.getSchoolName(), data.getDegree(), data.getManager(),
                    data.getStartTime(), data.getEndTime(), data.getGrade());
            daoList.insert(insertItem);
        }
    }
}
