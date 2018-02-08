package team_percussion.todosql;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class SetToDo extends Activity {

    private EditText mEditText01Name;               // やること名
    private ImageView mImageView01Picture;          // 画像
    private EditText mEditText01Primary;        // 優先度
    private TextView mText01Kome01;             // やること名の※印
    private TextView mText01Kome03;             // 優先度の※印
    int nomalpic = R.drawable.not_select;
    private Button mButton01Picture;
    private Button mButton01Regist;             // 登録ボタン
    private Button mButton01Show;           // 表示ボタン
    private int PicId;
    private Intent intent;                      // インテント
    private DBAdapter dbAdapter;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ListView mListView01;
    private Button mButton01AllDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_to_do);
        findViews();        // 各部品の結びつけ処理
        DBA();

        mButton01AllDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!items.isEmpty()) {
                    dbAdapter.openDB();
                    dbAdapter.allDelete();
                    dbAdapter.closeDB();
                    adapter.clear();
                    adapter.addAll(items);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SetToDo.this, "登録されているデータがありません", Toast.LENGTH_SHORT).show();
                }
            }
        });


        init();             //初期値設定

        //画像選択押したとき
        mButton01Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SetToDo.this, ResourceAllView.class);
                int requestCode = 1001;
                startActivityForResult(intent, requestCode);
            }
        });


        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // キーボードを非表示
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // DBに登録
                saveList();
                DBA();

            }
        });

        // 表示ボタン押下時処理
        mButton01Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.openDB();
                dbAdapter.saveDoTable();
                dbAdapter.closeDB();
                intent = new Intent(SetToDo.this, SelectSheetListView.class);
                startActivity(intent);      // 各画面へ遷移
            }
        });
    }

    private void DBA() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        items = new ArrayList<>();

        String[] columns = {DBAdapter.COL_NAME};
        Cursor c = dbAdapter.getDB(columns);

        if (c.moveToFirst()) {
            do {
                items.add(c.getString(0));
                Log.d("取得したCursor", c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();
        adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, items);
        mListView01.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        mEditText01Name = findViewById(R.id.editText01Name);   // やること名
        mImageView01Picture = findViewById(R.id.imageView01Picture);     // 画像
        mEditText01Primary = findViewById(R.id.editText01Primary);     // 優先度

        mText01Kome01 = findViewById(R.id.text01Kome01);             // やること名の※印
        mText01Kome03 = findViewById(R.id.text01Kome03);             // 優先度の※印

        mListView01 = findViewById(R.id.listView01);
        mButton01AllDelete = findViewById(R.id.button01AllDelete);

        mButton01Picture = findViewById(R.id.button01Picture);
        mButton01Regist = findViewById(R.id.button01Regist);           // 登録ボタン
        mButton01Show = findViewById(R.id.button01Show);               // 表示ボタン
    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Name.setText("");
        mImageView01Picture.setImageResource(nomalpic);
        mEditText01Primary.setText("");

        mText01Kome01.setText("");
        mText01Kome03.setText("");
        mEditText01Name.requestFocus();
    }


    /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        String strName = mEditText01Name.getText().toString();

        String strPrimary = mEditText01Primary.getText().toString();

        // EditTextが空白の場合
        if (strName.equals("") || strPrimary.equals("")) {

            if (strName.equals("")) {
                mText01Kome01.setText("※");     // 品名が空白の場合、※印を表示
            } else {
                mText01Kome01.setText("");      // 空白でない場合は※印を消す
            }


            if (strPrimary.equals("")) {
                mText01Kome03.setText("※");     // 個数が空白の場合、※印を表示
            } else {
                mText01Kome03.setText("");      // 空白でない場合は※印を消す
            }


            Toast.makeText(SetToDo.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int intPrimary = Integer.parseInt(strPrimary);

            Log.d("保存した優先度", String.valueOf(intPrimary));
            Log.d("保存したやること名", String.valueOf(strName));
            Log.d("保存した画像ID", String.valueOf(PicId));


            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.saveDB(intPrimary, strName, PicId);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        }
    }

    //画像選択から戻ってきたとき
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1001) {
            // 返却結果ステータスとの比較
            if (resultCode == Activity.RESULT_OK) {
                PicId = intent.getIntExtra("resultId", R.drawable.not_select);
                mImageView01Picture.setImageResource(PicId);
            }
        }
    }


}
