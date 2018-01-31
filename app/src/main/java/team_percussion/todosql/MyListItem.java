package team_percussion.todosql;
/**
 * Created by m2270073 on 2018/01/18.
 */


import android.util.Log;


public class MyListItem {
    protected int primary;
    protected String name;
    protected int picture;

    public MyListItem(int primary,String name,int picture){
        this.primary=primary;
        this.name=name;
        this.picture=picture;
    }
    public int getPrimary(){
        Log.d("取得した優先度",String.valueOf(primary));
        return primary;
    }
    public String getName(){
        Log.d("取得したやること",String.valueOf(name));
        return name;
    }
    public int getPictureid(){
        Log.d("取得した画像ID",String.valueOf(picture));
        return picture;
    }

}

