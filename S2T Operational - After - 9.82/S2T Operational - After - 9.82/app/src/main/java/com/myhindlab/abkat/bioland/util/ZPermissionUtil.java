package com.myhindlab.abkat.bioland.util;

import android.app.Activity;

import com.myhindlab.abkat.bioland.Interface.IPermissionsListener;
//import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者: zhudewei
 * 时间: 2017/12/15
 * code is never no bug
 * 简单的包装下RxPermissions便于后期替换
 */

public class ZPermissionUtil {

    private ZPermissionUtil() {
    }

    private static class RxPermissionUtilHolder
    {
        private static final ZPermissionUtil mInstance = new ZPermissionUtil();
    }

    public static ZPermissionUtil getInstance()
    {
        return RxPermissionUtilHolder.mInstance;
    }

    public void requestPermissions(final Activity activity, final IPermissionsListener listener, final String... permissions)
    {
        if (null == listener )
        {
            throw new RuntimeException("IPermissionsListener not null");
        }

        //
//        Flowable.just(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        getPermissions(activity).request(permissions)
//                                .subscribe(new Consumer<Boolean>() {
//
//                                    @Override
//                                    public void accept(Boolean aBoolean) throws Exception
//                                    {
//                                        if (aBoolean)
//                                        {
//                                            listener.onPermissionsSuccess();
//                                        }
//                                        else
//                                        {
//                                            listener.onPermissionsFail();
//                                        }
//                                    }
//                                });
//                    }
//                });

    }

    public RxPermissions getPermissions(Activity activity)
    {
        return new RxPermissions(activity);
    }

    public boolean isPermissions(Activity activity, String permission)
    {
        return getPermissions(activity).isGranted(permission);
    }

}
