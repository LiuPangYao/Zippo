package com.example.user.zippo.store;

import android.content.Context;

import com.example.greendao.ZI_WorkHistoryData;
import com.example.greendao.ZI_WorkHistoryDataDao;
import com.example.user.zippo.custom.DaoManager;

import java.util.List;

/**
 * 工作經歷
 */

public class WorkHistoryStore
{
    private DaoManager manager;

    public WorkHistoryStore(Context context)
    {
        manager = DaoManager.getInstance();
        manager.init(context);
    }

    public List<ZI_WorkHistoryData> getList()
    {
        ZI_WorkHistoryDataDao daoList = manager.getDaoSession().getZI_WorkHistoryDataDao();
        List<ZI_WorkHistoryData> teachData = daoList.queryBuilder()
                .orderAsc(ZI_WorkHistoryDataDao.Properties.Id)
                .list();
        return teachData;
    }

    public void deleteAll()
    {
        ZI_WorkHistoryDataDao daoList = manager.getDaoSession().getZI_WorkHistoryDataDao();
        daoList.deleteAll();
    }

    /**
     * 新增資料，會先清除舊資料
     */
    public void storeTeachList(List<ZI_WorkHistoryData> aList)
    {
        ZI_WorkHistoryDataDao daoList = manager.getDaoSession().getZI_WorkHistoryDataDao();

        ZI_WorkHistoryDataDao.dropTable(daoList.getDatabase(), true);
        ZI_WorkHistoryDataDao.createTable(daoList.getDatabase(), true);

        for (ZI_WorkHistoryData data : aList)
        {
            // 新增紀錄
            ZI_WorkHistoryData insertItem = new ZI_WorkHistoryData(null, data.getWorkTitle(), data.getCompayName(), data.getLocation(),
                    data.getStartTime(), data.getEndTime(), data.getScribe());
            daoList.insert(insertItem);
        }
    }
}
