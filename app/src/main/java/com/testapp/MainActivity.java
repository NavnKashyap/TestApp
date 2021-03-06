package com.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=MainActivity.this;
      binding= DataBindingUtil .setContentView(this,R.layout.activity_main);

      binding.tvDownload.setOnClickListener(this);
      binding.tvShowweb.setOnClickListener(this);

    }



    private void downloadAndUnzipContent(){

        String root = Environment.getExternalStorageDirectory().toString();

        final File mediaStorageDir = new File(root, Consts.APP_PATH);

        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        path=mediaStorageDir.getPath() + File.separator +
                Consts.APP_NAME + timeStamp;

        DownloadFileAsync download = new DownloadFileAsync(path+ ".zip", this, new DownloadFileAsync.PostDownload(){
            @Override
            public void downloadDone(File file) {
                Log.e(TAG, "file download completed");
                Toast.makeText(context, "file download completed", Toast.LENGTH_SHORT).show();

                // check unzip file now
//                Decompress unzip = new Decompress(Home.this, file);
//                unzip.unzip();


                ZipArchive zipArchive = new ZipArchive();
                zipArchive.unzip(path + ".zip",path,"");


                        binding.tvShowweb.setVisibility(View.VISIBLE);
                Log.e(TAG, "file unzip completed");
            }
        });
        download.execute(Consts.URL);
    }

    private void showWebview(String path) {


        WebSettings ws = binding.webview.getSettings();
        ws.setAllowFileAccess(true);

        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setBuiltInZoomControls(true);
        String dir = getFilesDir().getAbsolutePath();
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.loadUrl("file:/"+path/*+"/examples"+"/user.html"*/);

/*
        File file = new File("file://"+path+"/examples"+"/user.html");

        WebSettings settings = binding.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        binding.webview.setWebChromeClient(new WebChromeClient());
        binding.webview.loadUrl( file.getAbsolutePath() );*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDownload:
                downloadAndUnzipContent();
                break;
            case R.id.tvShowweb:
                binding.llBTN.setVisibility(View.GONE);
                binding.llWebview.setVisibility(View.VISIBLE);
                showWebview(path);
                break;
        }
    }
}
