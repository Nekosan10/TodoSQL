package team_percussion.todosql;

public class IconList{
    private Integer[] IconId = {
            R.drawable.change,
            R.drawable.cleansing,
            R.drawable.dosing,
            R.drawable.key,
            R.drawable.not_select,
            R.drawable.toilet,
            R.drawable.travel_bag,
            R.drawable.turn_off
            //R.drawable.<!--picname -->
            //TODO(以下繰り返し...)
    };
    private String[] IconName = {
            "change",
            "cleansing",
            "dosing",
            "key",
            "not_select",
            "toilet",
            "travel_bag",
            "turn_off",
            //" picname "
            //TODO(以下繰り返し...)
    };
    public IconList(){

    }
    public final int getCount() {
        // List大きさを返す
        return IconName.length;
    }
    public final long getItemId(int position) {
        return position;
    }
    public String getName(int number){
        return IconName[number];
    }
    public int getIcon(int number){
        return IconId[number];
    }

}
