package team_percussion.todosql;

/**
 * Created by m2270073 on 2018/01/15.
 * Databace
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBAdapter{
    private final static String DB_NAME = "todo.db";
    private final static String DB_TABLE = "todoSheet";
    private final static int DB_VERSION = 1;

    //todo やること
    public final static String COL_NAME ="name"; //やること名
    public final static String COL_PRIMARY = "_primary"; //優先度
    public final static String COL_PICTURE = "picture"; //画像

    private SQLiteDatabase db = null;
    private DBHelper dbHelper = null;
    protected Context context;

    //コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }


    //DB_Read&Write
    public DBAdapter openDB(){
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public DBAdapter readCB(){
        db =dbHelper.getReadableDatabase();
        return this;
    }
    public void closeDB(){
        db.close();
        db = null;
    }

    //DB登録
    public  void saveDB(int primary,String name,int picture){
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(COL_PRIMARY,primary);
            values.put(COL_NAME,name);
            values.put(COL_PICTURE,picture);
            db.replace(DB_TABLE,null,values);
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    //DB取得
    public Cursor getDB(String[] columns){
        return db.query(DB_TABLE,columns,null,null,null,null,null);
    }

    //DB検索取得
    public Cursor searchDB(String[] columns,String column,String[] name){
        return db.query(DB_TABLE,columns,column+" like ?",name,null,null,null);
    }

    //DB全削除
    public void allDelete(){
        db.beginTransaction();
        try{
            db.delete(DB_TABLE,null,null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    //DB単一削除
    public void selectDelete(String position){
        db.beginTransaction();
        try{
            db.delete(DB_TABLE,COL_PRIMARY+"=?",new String[]{position});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    //DBHelper
    private static class DBHelper extends SQLiteOpenHelper {
        //コンストラクタ
        public DBHelper(Context context) {
            super(context,DB_NAME,null,DB_VERSION);
        }
        //DB生成時処理
        @Override
        public void onCreate(SQLiteDatabase db){
            //テーブル生成定義
           String createTbl = "CREATE TABLE " + DB_TABLE + " ("
                   + COL_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + COL_NAME + " TEXT NOT NULL,"
                   + COL_PICTURE + " INTEGER NOT NULL"
                   + ");";
            db.execSQL(createTbl);
        }
        //DB更新処理
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
             onCreate(db);
        }
    }
}