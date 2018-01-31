package team_percussion.todosql;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;


public class ResourceAllView extends Activity implements AdapterView.OnItemClickListener {
    private Intent intent;
    private IconList PL;
    private List<Integer> iconList = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_all_view);

        PL = new IconList();
        for (int i = 0; i < PL.getCount() ; i++){
            iconList.add(PL.getIcon(i));
        }

        // GridViewのインスタンスを生成
        GridView gridview = (GridView) findViewById(R.id.gridview);

        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_main.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, iconList);

        // gridViewにadapterをセット
        gridview.setAdapter(adapter);

        // item clickのListnerをセット
        gridview.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            int picid = PL.getIcon(position);
            intent = new Intent(ResourceAllView.this, SetToDo.class);
            intent.putExtra( "resultId", picid );
            setResult(SetToDo.RESULT_OK,intent);
            finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class ViewHolder {
        ImageView imageView;
    }
    // BaseAdapter を継承した GridAdapter クラスのインスタンス生成
    class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int layoutId;
        private List<Integer> icList = new ArrayList<Integer>();

        public GridAdapter(Context context, int layoutId, List<Integer>iconList) {
            super();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
            icList = iconList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
                convertView = inflater.inflate(layoutId, parent,false);
                // ViewHolder を生成
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setImageResource(icList.get(position));

            return convertView;
        }

        @Override
        public int getCount() {
            // 全要素数を返す
            return iconList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


}


