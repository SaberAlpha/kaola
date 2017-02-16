package qianfeng.com.kaola1613.offline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by liujianping on 2016/10/20.
 */
public class DownloadHelper extends SQLiteOpenHelper{
    /**
     * 数据库文件的名称
     */
    public static final String DB_NAME = "download.db";

    /**
     * 离线下载的表名
     */
    public static final String TABLE_NAME = "table_download";

    public static final int version = 1;

    /**
     * 创建数据库文件
     *
     * @param context
     */
    public DownloadHelper(Context context)
    {
        super(context, DB_NAME, null, version);
    }

    /**
     * 创建数据库的表
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table " + TABLE_NAME + " (");
        buffer.append(DownloadTable._ID + " integer primary key autoincrement,");
        buffer.append(DownloadTable.COLUMNS_IMAGE + " text,");
        buffer.append(DownloadTable.COLUMNS_ALBUMNAME + " text,");
        buffer.append(DownloadTable.COLUMNS_COUNT + " integer,");
        buffer.append(DownloadTable.COLUMNS_SIZE + " text)");

        String sql = buffer.toString();
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 表的字段就是要显示的数据类型
     */
    public class DownloadTable implements BaseColumns
    {
        /**
         * 图片的url
         */
        public static final String COLUMNS_IMAGE = "image";

        /**
         * 专辑名称
         */
        public static final String COLUMNS_ALBUMNAME = "albumName";

        /**
         * 总共多少期
         */
        public static final String COLUMNS_COUNT = "count";

        /**
         * 专辑里的所有mp3总共的大小
         */
        public static final String COLUMNS_SIZE = "size";
    }

}
