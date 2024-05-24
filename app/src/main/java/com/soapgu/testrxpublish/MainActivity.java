package com.soapgu.testrxpublish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.functions.Functions;

public class MainActivity extends AppCompatActivity {
    private Observable<String> numberObservable = Observable.interval(1, TimeUnit.SECONDS)
            //.take(10)
            .map( t-> String.format("current value:%d",t) )
            .publish()
            .refCount();
    private TextView msg,tv_sub_first,tv_sub_sec;

    private Disposable mainSub,firstSub,secSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg = findViewById(R.id.msg);
        tv_sub_first = findViewById(R.id.tv_sub_first);
        tv_sub_sec = findViewById(R.id.tv_sub_sec);
        mainSub = numberObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe( t-> msg.setText( t ), Functions.ON_ERROR_MISSING,()-> msg.setText( "完成" ) );

        findViewById(R.id.button_sub_first).setOnClickListener( v->{
            if( firstSub == null ){
                firstSub = numberObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe( t-> tv_sub_first.setText( t ), Functions.ON_ERROR_MISSING,()-> tv_sub_first.setText( "完成" ) );
            } else {
                firstSub.dispose();
                firstSub = null;
            }
        } );

        findViewById(R.id.button_sub_sec).setOnClickListener( v->{
            if(secSub == null){
                secSub = numberObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe( t-> tv_sub_sec.setText( t ), Functions.ON_ERROR_MISSING,()-> tv_sub_sec.setText( "完成" ) );
            } else {
                secSub.dispose();
                secSub = null;
            }
        } );

    }
}