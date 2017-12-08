package tech.jcjc.rxjavademo;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import tech.jcjc.http_rx.FileUtil;
import tech.jcjc.http_rx.RestClient;
import tech.jcjc.http_rx.RestClientBuilder;
import tech.jcjc.http_rx.RestCreator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.tv);

        RestClient.builder()
                  .url("http://lsandroid.b0.upaiyun.com/updata/9709870c092839ea5c28d989765939ac.ts")
                  .build()
                  .download()
                  .map(new Function<ResponseBody, File>() {
                      @Override
                      public File apply(ResponseBody responseBody) throws Exception {
                          final InputStream is = responseBody.byteStream();
                          String downloadDir = "RxJavaDemo";
                          String extension = "chenqiao";
                          String name = "test.chenqiao";

                          if (downloadDir == null || downloadDir.equals("")) {
                              downloadDir = "down_loads";
                          }
                          if (extension == null || extension.equals("")) {
                              extension = "";
                          }
                          if (name == null) {
                              return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
                          } else {
                              return FileUtil.writeToDisk(is, downloadDir, name);
                          }
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<File>() {
                      @Override
                      public void accept(File s) throws Exception {
                          textView.setText(s.getAbsolutePath());
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          textView.setText("错误");
                      }
                  });


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "RxJava", Toast.LENGTH_LONG).show();
            }
        });

    }
}
