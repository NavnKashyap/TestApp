package com.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testapp.databinding.ActivityMainBinding;
import com.testapp.util.DownloadFileAsync;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.mahdi.mzip.zip.ZipArchive;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static String TAG = MainActivity.class.getSimpleName();
    String path="";
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil .setContentView(this,R.layout.activity_main);

      binding.tvDownload.setOnClickListener(this);
      binding.tvShowweb.setOnClickListener(this);

    }



    private void downloadAndUnzipContent(){

        String root = Environment.getExternalStorageDirectory().toString();

        final File mediaStorageDir = new File(root, Consts.APP_NAME);

        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        DownloadFileAsync download = new DownloadFileAsync(mediaStorageDir.getPath() + File.separator +
                Consts.APP_NAME + timeStamp + "zip", this, new DownloadFileAsync.PostDownload(){
            @Override
            public void downloadDone(File file) {
                Log.e(TAG, "file download completed");

                // check unzip file now
//                Decompress unzip = new Decompress(Home.this, file);
//                unzip.unzip();


                ZipArchive zipArchive = new ZipArchive();
                zipArchive.unzip(mediaStorageDir.getPath() + File.separator +
                        Consts.APP_NAME + timeStamp + "zip",mediaStorageDir.getPath() + File.separator +
                        Consts.APP_NAME + timeStamp,"");
                path=mediaStorageDir.getPath() + File.separator +
                        Consts.APP_NAME + timeStamp;

                        binding.tvShowweb.setVisibility(View.VISIBLE);
                Log.e(TAG, "file unzip completed");
            }
        });
        download.execute(Consts.URL);
    }

    private void showWebview(String path) {



        WebSettings ws = binding.webview.getSettings();
        ws.setAllowFileAccess(true);
        String dir = getFilesDir().getAbsolutePath();
        binding.webview.loadUrl("file://"+path+".html");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDownload:
                downloadAndUnzipContent();
                break;
            case R.id.tvShowweb:
                binding.llBTN.setVisibility(View.GONE);
                binding.llWebview.setVisibility(View.GONE);
                showWebview(path);
                break;
        }
    }
}
