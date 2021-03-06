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
    //DB情報
    private final static String DB_NAME = "todo.db";
    private final static String DB_TABLE = "todoSheet";
    private final static String NEWDB_TABLE ="doSheet";
    private final static int DB_VERSION = 2;


    //todo やること項目名
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
    public DBAdapter readDB(){
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

    //テーブル取得
    public Cursor GetDoTable(String[] columns){
        return db.query(NEWDB_TABLE,columns,null,null,null,null,null);
    }
    //DB検索取得
    public Cursor selectgetDoTable(String[] columns){
        return db.query(NEWDB_TABLE,new String[]{COL_PRIMARY,COL_NAME,COL_PICTURE},COL_PRIMARY+"=?",columns,null,null,null);
    }

    //DB全削除
    public void allDeletedoTable(){
        db.beginTransaction();
        try{
            db.delete(NEWDB_TABLE,null,null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    //DB単一削除
    public void selectDeletedoTable(String position){
        db.beginTransaction();
        try{
            db.delete(NEWDB_TABLE,COL_PRIMARY+"=?",new String[]{position});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void saveDoTable(){
        //"INSERT INTO " + NEWDB_TABLE + " ("+ COL_PRIMARY+" , "+COL_NAME+" , "+COL_PICTURE+")
        db.execSQL("INSERT OR IGNORE INTO "+NEWDB_TABLE + " SELECT * FROM "
                +DB_TABLE+";");
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
            String create = "CREATE TABLE " + NEWDB_TABLE +  " ("
                    + COL_PRIMARY + " INTEGER UNIQUE,"
                    + COL_NAME + " TEXT NOT NULL,"
                    + COL_PICTURE + " INTEGER NOT NULL );";
            db.execSQL(create);

        }


        //DB更新処理
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + NEWDB_TABLE);
             onCreate(db);
        }

    }
}