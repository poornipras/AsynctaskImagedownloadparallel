package com.pooja.asynctaskimagedownloadparallel;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
Button btn;
    TextView loading1,loading2,loading3,loading4;
    ProgressBar pb_image1,pb_image2,pb_image3,pb_image4;
    String[] url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button);
        loading1=(TextView)findViewById(R.id.textView_loading1);
        loading2=(TextView)findViewById(R.id.textView_loading2);
        loading3=(TextView)findViewById(R.id.textView_loading3);
        loading4=(TextView)findViewById(R.id.textView_loading4);

        pb_image1=(ProgressBar)findViewById(R.id.progressBar_image1);
        pb_image2=(ProgressBar)findViewById(R.id.progressBar_image2);
        pb_image3=(ProgressBar)findViewById(R.id.progressBar_image3);
        pb_image4=(ProgressBar)findViewById(R.id.progressBar_image4);
        url=getResources().getStringArray(R.array.url_array);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsync myAsync=new MyAsync(pb_image1);
                myAsync.execute(url[0]);
                MyAsync asyncparallel=new MyAsync(pb_image2);
                StartAsyncTaskInParalleltwo(asyncparallel);
                MyAsync asyncparalleltwo=new MyAsync(pb_image3);
                StartAsyncTaskInParallel(asyncparalleltwo);
                MyAsync asyncparallelthree=new MyAsync(pb_image4);
                StartAsyncTaskInParallel(asyncparallelthree);
            }
        });
    }

    private void StartAsyncTaskInParallel(MyAsync task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url[1]);
        else
            task.execute(url[1]);
    }

    private void StartAsyncTaskInParalleltwo(MyAsync task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url[2]);
        else
            task.execute(url[2]);
    }
    private void StartAsyncTaskInParallelthree(MyAsync task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url[3]);
        else
            task.execute(url[3]);
    }
    public class MyAsync extends AsyncTask<String,Integer,Boolean>
    {
        private int contentlength=-1;
        private int counter=0;
        public int calculatedprogress=0;
        ProgressBar myprogressbar;

        MyAsync(ProgressBar progressBar)
        {
           this.myprogressbar=progressBar;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loading1.setVisibility(View.VISIBLE);
            pb_image1.setVisibility(View.VISIBLE);
            loading2.setVisibility(View.VISIBLE);
            pb_image2.setVisibility(View.VISIBLE);
            loading3.setVisibility(View.VISIBLE);
            pb_image3.setVisibility(View.VISIBLE);
            loading4.setVisibility(View.VISIBLE);
            pb_image4.setVisibility(View.VISIBLE);*/
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean successful=false;
            URL imageurl=null;
            HttpURLConnection connection=null;

            InputStream inputStream=null;
            File file=null;
            FileOutputStream fileOutputStream=null;
            try {
                imageurl=new URL(strings[0]);
                connection= (HttpURLConnection) imageurl.openConnection();
                contentlength=connection.getContentLength();
                inputStream=connection.getInputStream();
                file=new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+
                        "/"+ Uri.parse(strings[0]).getLastPathSegment());
                fileOutputStream=new FileOutputStream(file);

                int read=-1;
                byte[]buffer=new byte[1024];

                while((read=inputStream.read(buffer))!=-1)
                {
                    fileOutputStream.write(buffer,0,read);
                    counter=counter+read;
                    publishProgress(counter);
                }
                successful=true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally
            {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                if(inputStream!=null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fileOutputStream!=null)
                {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return successful;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            calculatedprogress=(int)(((double)values[0]/contentlength)*100);
            myprogressbar.setProgress(calculatedprogress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            /*loading1.setVisibility(View.INVISIBLE);
            pb_image1.setVisibility(View.INVISIBLE);
            loading2.setVisibility(View.INVISIBLE);
            pb_image2.setVisibility(View.INVISIBLE);
            loading3.setVisibility(View.INVISIBLE);
            pb_image3.setVisibility(View.INVISIBLE);
            loading4.setVisibility(View.INVISIBLE);
            pb_image4.setVisibility(View.INVISIBLE);*/
        }
    }

}
