package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGen
{
    public static void main(String args[]) throws Exception
    {
        Schema schema = new Schema(3, "com.example.greendao");

        addNoteList(schema);
        addEmailData(schema);
        addBlogData(schema);
        addWorkHistoryData(schema);
        addTeachData(schema);

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addNoteList(Schema schema)
    {
        Entity noteList = schema.addEntity("ZI_NoteList");

        noteList.addIdProperty();
        noteList.addStringProperty("title").notNull();
        noteList.addStringProperty("date").notNull();
        noteList.addStringProperty("url").notNull();
    }

    private static void addEmailData(Schema schema)
    {
        Entity blogData = schema.addEntity("ZI_EmailData");

        blogData.addIdProperty();
        blogData.addStringProperty("sort").notNull();
        blogData.addStringProperty("email").notNull();
    }

    private static void addBlogData(Schema schema)
    {
        Entity emailData = schema.addEntity("ZI_BlogData");

        emailData.addIdProperty();
        emailData.addStringProperty("sort").notNull();
        emailData.addStringProperty("blog").notNull();
    }

    private static void addWorkHistoryData(Schema schema)
    {
        Entity workData = schema.addEntity("ZI_WorkHistoryData");

        workData.addIdProperty();
        workData.addStringProperty("workTitle").notNull();
        workData.addStringProperty("compayName").notNull();
        workData.addStringProperty("location").notNull();
        workData.addStringProperty("startTime").notNull();
        workData.addStringProperty("endTime").notNull();
        workData.addStringProperty("scribe").notNull();
    }

    private static void addTeachData(Schema schema)
    {
        Entity teachData = schema.addEntity("ZI_TeachData");

        teachData.addIdProperty();
        teachData.addStringProperty("schoolName").notNull();
        teachData.addStringProperty("degree").notNull();
        teachData.addStringProperty("manager").notNull();
        teachData.addStringProperty("startTime").notNull();
        teachData.addStringProperty("endTime").notNull();
        teachData.addStringProperty("grade").notNull();
    }
}
