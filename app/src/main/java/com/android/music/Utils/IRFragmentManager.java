package com.android.music.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.music.R;

public class IRFragmentManager {
    private static FragmentManager mManager;
    private static FragmentTransaction mTransaction;
    private static FragmentManager getFragmentManager() {
        return mManager;
    }

    private static void setFragmentManager(FragmentManager manager) {
        IRFragmentManager.mManager = manager;
    }

    public static void setFragmentTransaction(FragmentTransaction transaction) {
        IRFragmentManager.mTransaction = transaction;
    }

    public static FragmentTransaction getFragmentTransaction() {
        return mTransaction;
    }

    private static void initFragmentTransaction() {
        mTransaction = mManager.beginTransaction();
    }

    /**
     * 此方法在onBackPressed被重写时使用
     * 回退到上一层fragment
     * 如果已经是最后一层，隐藏界面
     * @param activity 当前activity，仅支持AppCompatActivity
     *                 在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     */
    public static void back(AppCompatActivity activity) {
        if (getFragmentManager() == null ) {
            activity.moveTaskToBack(true);
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() < 1) {
            activity.moveTaskToBack(true);
        } else {
            mManager.popBackStack();
        }
    }

    /**
     * 切换Fragment为传入参数
     *
     * @param activity 当前activity，仅支持AppCompatActivity
     *                 在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     * @param fragment 目标fragment对象
     */
    public static void switchFragment(Fragment fragment, AppCompatActivity activity, String tag) {
        IRFragmentManager.setFragmentManager(activity.getSupportFragmentManager());
        IRFragmentManager.initFragmentTransaction();
        //frame容器id
        mManager.findFragmentById(R.id.frameLayout);
        mTransaction
                .replace(R.id.frameLayout, fragment, tag)
                .addToBackStack(null)
                .commit();
                //替换成下面那句可以在frameLayout容器被遮挡的情况下替换fragment
                //.commitAllowingStateLoss();
    }
}
