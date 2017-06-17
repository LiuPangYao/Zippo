package com.example.user.zippo.store;

import android.content.Context;

import com.example.greendao.ZI_NoteList;
import com.example.greendao.ZI_NoteListDao;
import com.example.user.zippo.custom.DaoManager;

import java.util.List;

public class NoteListStore
{
    private DaoManager manager;

    public NoteListStore(Context context)
    {
        manager = DaoManager.getInstance();
        manager.init(context);
    }

    public List<ZI_NoteList> getList()
    {
        ZI_NoteListDao daoList = manager.getDaoSession().getZI_NoteListDao();
        List<ZI_NoteList> noteData = daoList.queryBuilder()
                .orderAsc(ZI_NoteListDao.Properties.Date)
                .list();
        return noteData;
    }

    public void deleteAll()
    {
        ZI_NoteListDao daoList = manager.getDaoSession().getZI_NoteListDao();
        daoList.deleteAll();
    }

    /**
     * 新增資料，會先清除舊資料
     */
    public void storeNoteList(List<ZI_NoteList> aList)
    {
        ZI_NoteListDao daoList = manager.getDaoSession().getZI_NoteListDao();

        ZI_NoteListDao.dropTable(daoList.getDatabase(), true);
        ZI_NoteListDao.createTable(daoList.getDatabase(), true);

        for (ZI_NoteList data : aList)
        {
            // 新增紀錄
            ZI_NoteList insertItem = new ZI_NoteList(null, data.getTitle(), data.getDate(), data.getUrl());
            daoList.insert(insertItem);
        }
    }
}
