package qianfeng.com.kaola1613.offline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import qianfeng.com.kaola1613.offline.entity.DownloadEntity;
import qianfeng.com.kaola1613.other.utils.LogUtil;

/**
 * Created by liujianping on 2016/10/20.
 */
public class DownloadManager {

    private DownloadHelper helper;

    private SQLiteDatabase db;

    private static DownloadManager manager;

    private static Context mContext;

    public static DownloadManager getInstance()
    {
        if (manager == null)
        {
            synchronized (DownloadManager.class)
            {
                if (manager == null)
                {
                    manager = new DownloadManager();
                }
            }
        }

        return manager;
    }

    public static void init(Context context)
    {
        mContext = context;
    }

    private DownloadManager()
    {
        helper = new DownloadHelper(mContext);
    }

    public void insert(DownloadEntity entity)
    {
        db = helper.getWritableDatabase();

        long insert = db.insert(DownloadHelper.TABLE_NAME, null, getContentValues(entity));

        if (insert != 0)
        {
            LogUtil.w("添加成功");
        }

        db.close();
    }

    public void delete(DownloadEntity entity)
    {
        db = helper.getWritableDatabase();

        db.delete(DownloadHelper.TABLE_NAME, "where " + DownloadHelper.DownloadTable.COLUMNS_ALBUMNAME + " = ?",
                new String[]{entity.getAlbumName()});

        db.close();
    }

    public List<DownloadEntity> getList()
    {
        db = helper.getReadableDatabase();

        String sql = "select * from " + DownloadHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        List<DownloadEntity> list = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String image = cursor.getString(cursor.getColumnIndex(DownloadHelper.DownloadTable.COLUMNS_IMAGE));
            String albumname = cursor.getString(cursor.getColumnIndex(DownloadHelper.DownloadTable.COLUMNS_ALBUMNAME));
            int count = cursor.getInt(cursor.getColumnIndex(DownloadHelper.DownloadTable.COLUMNS_COUNT));
            String size = cursor.getString(cursor.getColumnIndex(DownloadHelper.DownloadTable.COLUMNS_SIZE));

            DownloadEntity entity = new DownloadEntity(image, albumname, count, size);
            list.add(entity);
        }

        cursor.close();
        db.close();

        return list;
    }



    public ContentValues getContentValues(DownloadEntity entity)
    {
        ContentValues values = new ContentValues();
        values.put(DownloadHelper.DownloadTable.COLUMNS_IMAGE, entity.getImage());
        values.put(DownloadHelper.DownloadTable.COLUMNS_ALBUMNAME, entity.getAlbumName());
        values.put(DownloadHelper.DownloadTable.COLUMNS_COUNT, entity.getCount());
        values.put(DownloadHelper.DownloadTable.COLUMNS_SIZE, entity.getSize());
        return values;
    }
}
