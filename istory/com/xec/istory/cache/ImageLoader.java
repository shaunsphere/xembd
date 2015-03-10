package com.xec.istory.cache;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xec.istory.R;

import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
    
     int stub_id=R.drawable.stub;
     int PROGRESS_OFF=View.INVISIBLE;
     int PROGRESS_ON=View.VISIBLE;
    
    public void DisplayImage(String url,String md5, ImageView imageView, ProgressBar pb )
    {
        imageViews.put(imageView, url); // hardcode in Map
        Bitmap bitmap=memoryCache.get(url); // to check bitmap in the cache or not???
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap); //bitmap in cache-->setImageBitmap to display
            pb.setVisibility(PROGRESS_OFF);
          
        }
        else //bitmap not in cache-->check in disc cache and decode/load
        {
            queuePhoto(url,md5, imageView, pb); // race bw Imgdisplay and Imgview
            imageView.setImageResource(stub_id); // set dummy first then wait for change after
            pb.setVisibility(PROGRESS_ON);
        }
    }
        
    private void queuePhoto(String url, String md5, ImageView imageView,ProgressBar pb)
    {
        PhotoToLoad p=new PhotoToLoad(url,md5, imageView, pb);
        executorService.submit(new PhotosLoader(p)); // executor terminate and produce a future for tracking progress of photoloader
    }
    
    private Bitmap getBitmap(String url,String md5) 
    {
//        File f=fileCache.getFile(url);
    	File f=fileCache.getFile(url, md5);
    //	  Log.i("log_tag","f  to string = "  +md5 +"DD"+ f.getName());
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //TaskLoadIS for the queue
    private class PhotoToLoad
    {
        public String url;
        public String md5;
        public ImageView imageView;
        public ProgressBar pb;
        public PhotoToLoad(String u,String m, ImageView i,ProgressBar p){
            url=u; 
            md5 = m;
            imageView=i;
            pb = p;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
             //Code you want to run on the thread goes here/below
            try{
                if(imageViewReused(photoToLoad))
                    return;
               // Bitmap bmp=getBitmap(photoToLoad.url);
                Bitmap bmp=getBitmap(photoToLoad.url,photoToLoad.md5); // load url into bitmap and save in md5 filename
                memoryCache.put(photoToLoad.url, bmp); // save image from url to memory cache
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd); // hardcode display img
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
       // if(tag==null || !tag.equals(photoToLoad.md5q))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null){
                photoToLoad.imageView.setImageBitmap(bitmap);
            photoToLoad.pb.setVisibility(PROGRESS_OFF);
            }
            else{
                photoToLoad.imageView.setImageResource(stub_id);
            photoToLoad.pb.setVisibility(PROGRESS_ON);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
