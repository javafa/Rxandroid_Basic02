package com.kodonho.android.rxandroid_basic02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Rxandroid Basic2";
    Button btnJust,btnFrom,btnDefer;
    TextView tv;
    ListView listView;
    ArrayAdapter<String> adapter;

    ArrayList<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJust = (Button) findViewById(R.id.btnJust);
        btnFrom = (Button) findViewById(R.id.btnFrom);
        btnDefer = (Button) findViewById(R.id.btnDefer);
        btnJust.setOnClickListener(this);
        btnFrom.setOnClickListener(this);
        btnDefer.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datas);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnJust:
                doJust();
                break;
            case R.id.btnFrom:
                doFrom();
                break;
            case R.id.btnDefer:
                doDefer();
                break;
        }
    }

    // Java 데이터를 바로 Observable 객체로 변환할 수 있다
    public void doJust(){
        Observable<String> observable = Observable.just("dog");
        observable.subscribe(new Action1<String>(){
            @Override
            public void call(String s) {
                tv.setText(s);
            }
        });
    }

    // 컬렉션 형태의 자바 객체로부터 옵저버블을 생성한다
    public void doFrom(){
        Observable<String> observable = Observable.from(new String[]{"dog","bird","chicken","horse","turtle","rabbit","tiger"});
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                datas.add(s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }, new Action0() {
            @Override
            public void call() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    // 지연처리 함수를 제공하고
    // 호출할때 마다 옵저버블 객체를 매번 생성한다
    public void doDefer(){
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("bird");
            }
        });

        observable
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"Completed");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        tv.setText(s);
                    }
                });
    }
}
