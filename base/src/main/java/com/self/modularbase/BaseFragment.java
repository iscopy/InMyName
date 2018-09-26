package com.self.modularbase;
/*
 * 作者：iscopy on 2018/9/25
 * 邮箱：iscopy@163.com
 * 版本：v1.0
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    private View mContentView;
    private Context context;

    /** View点击 **/
    public abstract void widgetClick(View v);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(bindLayout(),container,false);



        context = getContext();
        ButterKnife.bind(this,mContentView);
        initView(mContentView);
        setListener();

        return mContentView;
    }

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int bindLayout();

    /**
     * [初始化控件]
     * @param view
     */
    public abstract void initView(final View view);

    /**
     * [设置监听]
     */
    public abstract void setListener();


    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void init() {}

    public View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return context;
    }


    /**
     * [页面跳转]
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(),clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * [接收广播]
     * **/
    public abstract void receiveRadio(Intent intent);

    LocalBroadcastManager broadcastManager;
    BroadcastReceiver MyReceiver;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        MyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                receiveRadio(intent);
            }
        };
        broadcastManager.registerReceiver(MyReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(MyReceiver);
    }




    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    //点击界面其他位置，EditText失去焦点，fragment_gmi是整个布局
    public interface FocusEditView{
        //获取焦点
        void focusObtain(View view);

        //失去焦点
        void focusLose(View view);
    };
    public void focusEditView(EditText editText, final FocusEditView focusEditView){
        //点击界面其他位置，EditText失去焦点，fragment_gmi是整个布局
        /*linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });*/

        //EditText获取与失去焦点
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    //EditText获取焦点事件
                    focusEditView.focusObtain(view);
                }else{
                    //EditText失去焦点事件
                    focusEditView.focusLose(view);
                }
            }
        });
    }

}
