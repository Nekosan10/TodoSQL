/**
 * Created by m2270073 on 2018/01/18.
 */
package team_percussion.todosql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SelectSheetListView extends Activity {

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<MyListItem> items;
    private GridView mListView03;
    protected MyListItem myListItem;
    private Intent intent;
    private int listId;

    // 参照するDBのカラム：優先度,やること,リソースIDの全部なのでnullを指定
    private String[] columns = null;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sheet_listview);

        // DBAdapterのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);



        // itemsのArrayList生成
        items = new ArrayList<>();

        // MyBaseAdapterのコンストラクタ呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new MyBaseAdapter(this, items);

        // ListViewの結び付け
        mListView03 = findViewById(R.id.GridView03);

        loadMyList();   // DBを読み込む＆更新する処理


        mListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,final int position,long id) {

                intent = new Intent(SelectSheetListView.this,Confirmation.class);
                int requestCode = 1002;
                myListItem = items.get(position);
                listId = myListItem.getPrimary();
                intent.putExtra("ToDoPrimary",listId );
                startActivityForResult(intent, requestCode);
            }
        });

        // 行を長押しした時の処理
        mListView03.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                // アラートダイアログ表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectSheetListView.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                // OKの時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // IDを取得する
                        myListItem = items.get(position);
                        listId = myListItem.getPrimary();

                        dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                        dbAdapter.selectDeletedoTable(String.valueOf(listId));     // DBから取得したIDが入っているデータを削除する
                        Log.d("Long click : ", String.valueOf(listId));
                        dbAdapter.closeDB();    // DBを閉じる
                        loadMyList();
                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });
    }

    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */
    private void loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB();     // DBの読み込み(読み書きの方)


        // DBのデータを取得
        Cursor c = dbAdapter.GetDoTable(columns);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem = new MyListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2));


                Log.d("取得したCursor(優先度):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(やること名):", c.getString(1));
                Log.d("取得したCursor(画像ID):", c.getString(2));


                items.add(myListItem);          // 取得した要素をitemsに追加

            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();                    // DBを閉じる
        mListView03.setAdapter(myBaseAdapter);  // ListViewにmyBaseAdapterをセット
        myBaseAdapter.notifyDataSetChanged();   // Viewの更新

    }

    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */
    public class MyBaseAdapter extends BaseAdapter {

        private Context context;
        private List<MyListItem> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {
            //TextView text05Primary;
            TextView text05Name;
            ImageView text05Picture;
        }

        // コンストラクタの生成
        public MyBaseAdapter(Context context, List<MyListItem> items) {
            this.context = context;
            this.items = items;
        }

        // Listの要素数を返す
        @Override
        public int getCount() {
            return items.size();
        }

        // indexやオブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        // IDを他のindexに返す
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 新しいデータが表示されるタイミングで呼び出される
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;

            // データを取得
            myListItem = items.get(position);


            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_sheet_listviewlayout, parent, false);


                //TextView text05Primary = (TextView) view.findViewById(R.id.text05Primary);        // 優先度のTextView
                TextView text05Name = view.findViewById(R.id.text05Name);      // やること名のTextView
                ImageView image05Picture = view.findViewById(R.id.text05TitlePicture);        // 画像のTextView

                // holderにviewを持たせておく
                holder = new ViewHolder();
                //holder.text05Primary = text05Primary;
                holder.text05Name = text05Name;
                holder.text05Picture = image05Picture;


                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            //holder.text05Primary.setText(myListItem.getPrimary()+"");
            holder.text05Name.setText(myListItem.getName()+"");
            //TODO picname を　getPictureで使えるように
            holder.text05Picture.setImageResource(myListItem.getPictureid());
            return view;

        }
    }
    //画像選択から戻ってきたとき
    public void onActivityResult(int requestCode,int resultCode,Intent intent) {
        if (requestCode == 1002) {
            // 返却結果ステータスとの比較
            if (resultCode == Activity.RESULT_OK) {
                loadMyList();
            }
        }
    }
}
