package team_percussion.todosql;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m2270073 on 2018/02/01.
 */

public class Confirmation extends AppCompatActivity {
    private ImageView TodoImage;
    private ImageView Ok;
    private ImageView No;
    private DBAdapter dbAdapter;
    private SelectSheetListView.MyBaseAdapter myBaseAdapter;
    private List<MyListItem> DoList;
    protected MyListItem myListItem;
    // 参照するDBのカラム：優先度,やること,リソースIDの全部なのでnullを指定
    private String[] List = null;
    private Intent intent;
    private int listId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);
        dbAdapter = new DBAdapter(this);
        intent = getIntent();
        listId = intent.getIntExtra("ToDoPrimary",0);
        // itemsのArrayList生成
        DoList = new ArrayList<>();
        loadMyList();

        // ListViewの結び付け
        TodoImage = findViewById(R.id.imageVieTodo);
        Ok = findViewById(R.id.imageViewOk);
        No = findViewById(R.id.imageViewDoNo);

        myListItem = DoList.get(listId);
        int picId = myListItem.getPrimary();
        TodoImage.setImageResource(picId);

        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // クリック時の処理
                dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                dbAdapter.selectDeletedoTable(String.valueOf(listId));     // DBから取得したIDが入っているデータを削除する
                dbAdapter.closeDB();    // DBを閉じる
                loadMyList();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    private void SetImage(){

    }
    private void loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        DoList.clear();

        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // DBのデータを取得
        String[] name={Integer.toString(listId)};
        Cursor c = dbAdapter.selectgetDoTable(null,"_primary",name);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem = new MyListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2));


                Log.d("取得したCursor(優先度):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(やること名):", c.getString(1));
                Log.d("取得したCursor(画像ID):", String.valueOf(c.getInt(2)));


                DoList.add(myListItem);          // 取得した要素をDoListに追加

            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();             // DBを閉じる
    }



}
