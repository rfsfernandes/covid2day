package pt.covidtwoday.custom.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UtilsClass {

  private static UtilsClass instance;

  public static UtilsClass getInstance() {
    if (instance != null) {
      return instance;
    } else {
      return instance = new UtilsClass();
    }
  }

  public void setStatusBarDark(Activity activity, boolean isWhite) {
    setWindowFlag(activity, true);
    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    if (isWhite) {
      activity.getWindow().getDecorView().setSystemUiVisibility(0x00002000
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    } else {
      activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    setWindowFlag(activity, false);
  }

  private void setWindowFlag(Activity activity, boolean on) {
    Window win = activity.getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    if (on) {
      winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    } else {
      winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    }
    win.setAttributes(winParams);
  }

  public HashMap sortByValues(HashMap map) {
    List list = new LinkedList(map.entrySet());
    // Defined Custom Comparator here
    Collections.sort(list, new Comparator() {
      public int compare(Object o1, Object o2) {
//        return ((Comparable) ((Map.Entry) (o1)).getKey())
//            .compareTo(((Map.Entry) (o2)).getKey());


        Object ob1 = ((Map.Entry) (o1)).getKey();
        Object ob2 = ((Map.Entry) (o2)).getKey();

        String[] stringArray1 = ob1.toString().split("/");
        String[] stringArray2 = ob2.toString().split("/");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MONTH,Integer.valueOf(stringArray1[0])-1);
        calendar1.set(Calendar.DAY_OF_MONTH,Integer.valueOf(stringArray1[1]));
        calendar1.set(Calendar.YEAR,Integer.valueOf(stringArray1[2]) + 2000);

        Calendar calendar2 = Calendar.getInstance();
//        Calendar calendar =
//            new Calendar.Builder().setDate(Integer.valueOf(stringArray1[2]) + 2000,
//                Integer.valueOf(stringArray1[0])-1,Integer.valueOf(s));
        calendar2.set(Calendar.MONTH,Integer.valueOf(stringArray2[0])-1);
        calendar2.set(Calendar.DAY_OF_MONTH,Integer.valueOf(stringArray2[1]));
        calendar2.set(Calendar.YEAR,Integer.valueOf(stringArray2[2]) + 2000);

        return calendar1.compareTo(calendar2);

      }
    });

    // Here I am copying the sorted list in HashMap
    // using LinkedHashMap to preserve the insertion order
    HashMap sortedHashMap = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      sortedHashMap.put(entry.getKey(), entry.getValue());
    }
    return sortedHashMap;
  }

}
