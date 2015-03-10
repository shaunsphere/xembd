package com.xec.istory.presentation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xec.istory.R;
import com.xec.istory.cache.ImageLoader;

public class CustomListViewAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public CustomListViewAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    /////////////////////////////////////////////////////
    public String Pullname(String comfour) {
       return  comfour.substring(0, comfour.indexOf("\u2665"));
    }
    
    public String Pullconnectname(String comfour) {
    	String[] words = comfour.split("\u2665");  
       return  words[1];
    }
    
    public String Pulllink(String comfour) {
    	String[] words = comfour.split("\u2665");  
        return  words[2];
    }
    
    public String Pullmd5(String comfour) {
        return  comfour.substring(comfour.lastIndexOf("\u2665")+1);
     }
    public String Pullshortcontent(String comfour) {
        return  comfour.substring(comfour.lastIndexOf("\u2665")+1);
     }
    ////////////////////////////////////////////////////
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
        vi = inflater.inflate(R.layout.storyrow, null);
        ImageView image=(ImageView)vi.findViewById(R.id.icon);
        TextView showdisplayname=(TextView)vi.findViewById(R.id.displayname);
        TextView showshortcontent=(TextView)vi.findViewById(R.id.shortcontent);
        ProgressBar pb = (ProgressBar)vi.findViewById(R.id.progressspin);
        //pb.setVisibility(View.INVISIBLE);
//        Log.i("log_tag","numofapps = "  + data[position]);

        String itemcomfour = data[position];
        
        showdisplayname.setText(Pullname(itemcomfour));
      //  showshortcontent.setText(Pullshortcontent(itemcomfour));
        showshortcontent.setText(Pullconnectname(itemcomfour));
        
        
        //imageLoader.DisplayImage(Pulllink(itemcomfour),Pullmd5(itemcomfour), image);
        
        imageLoader.DisplayImage(Pulllink(itemcomfour),Pullmd5(itemcomfour), image,pb);

        return vi;
    }
}

