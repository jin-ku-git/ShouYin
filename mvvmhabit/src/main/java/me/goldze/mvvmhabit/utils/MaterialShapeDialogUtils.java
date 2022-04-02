package me.goldze.mvvmhabit.utils;

import android.content.Context;

import com.mingle.widget.ShapeLoadingDialog;


/**
 * Created by goldze on 2017/5/10.
 */

public class MaterialShapeDialogUtils {

    public void showThemed(Context context, String
            title, String content) {
        new ShapeLoadingDialog.Builder(context)
                .loadText(title)
                .show();

        //获取按钮并监听
//        MDButton btn = ShapeLoadingDialog.getActionButton(DialogAction.NEGATIVE);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    /***
     * 获取一个耗时等待对话框
     *
     * @return ShapeLoadingDialog.Builder
     */
    public static ShapeLoadingDialog.Builder showIndeterminateProgressDialog(Context context, String content) {
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(context)
                .loadText(content)
                .canceledOnTouchOutside(false);
        return builder;
    }



}
