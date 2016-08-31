package com.example.rd_e_z240.a160829_test_listview;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private ListView mlistView;
    private CustomAdapter mCustomAdapater;
    private CustomCanvas mCustomCanvas;

    private View mView; //カスタムCanvasView用
    private ViewTreeObserver.OnGlobalLayoutListener mGLL;   //View Size取得用
    private int mView_w,mView_h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初期パラメータ読み込み＆ListViewへセット
        initParameterSet();
        mlistView = (ListView) findViewById(R.id.listview);
        mlistView.setAdapter(mCustomAdapater);

        //波形表示用カスタムビュー初期画像表示
        mCustomCanvas = (CustomCanvas) this.findViewById(R.id.customview);
        mView = (View) findViewById(R.id.customview);
        mGLL = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mView_w=mView.getWidth();   mView_h=mView.getHeight();
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(mGLL);// removeOnGlobalLayoutListener()の削除
            }
        };
        mView.getViewTreeObserver().addOnGlobalLayoutListener(mGLL);
    }
    private void initParameterSet(){
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        BufferedReader br = null;
        try{
            try {
                is = am.open("init_parameter.txt");
                br = new BufferedReader(new InputStreamReader(is));
                String str;
                List<ParamDetails> pd_List = new ArrayList<>();
                while((str = br.readLine()) != null){
                    ParamDetails pd = new ParamDetails();
                    String[] st = str.split(",",5);
                    //init_parameter.txt データ配置：no,id,name,value,unit
                    pd.setParam_no(st[0]);
                    pd.setParam_id(st[1]);
                    pd.setParam_name(st[2]);
                    pd.setParam_value(parseInt(st[3],0));    // valueのみintで取り込み
                    pd.setParam_unit(st[4]);
                    pd_List.add(pd);
                }
                mCustomAdapater = new CustomAdapter(this, 0, pd_List);
                Log.d("init_parameter", pd_List.toString());
            } finally {
                if (br != null) br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "初期パラメータの読み込みに失敗しました。", Toast.LENGTH_LONG).show();
        }
    }

    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch ( NumberFormatException e ) {
            return defaultValue;
        }
    }
    public static int parseInt(String value) {
        return parseInt(value, 0);
    }
}
