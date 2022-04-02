package com.youwu.shouyin.jianpan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.util.KeyboardUtils;
import com.xuexiang.xui.adapter.recyclerview.GridDividerItemDecoration;
import com.xuexiang.xui.adapter.recyclerview.XGridLayoutManager;
import com.youwu.shouyin.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.utils.KLog;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;


public class MyCustKeybords extends FrameLayout {


    RecyclerView recyView;

    View layoutDel;//取消
    RelativeLayout layoutSpot;//点
    RelativeLayout layoutZero;//0

    FrameLayout layoutSystem;

    FrameLayout btnConfirm;
    private BaseAdapter<String> adapter;
    private EditText editText;
    private OnKeyBoradConfirm listener;
    private boolean needSystem;
    List<String> list = new ArrayList<>();

    private Timer _timer;

    public void setListener(OnKeyBoradConfirm listener) {
        this.listener = listener;
    }

    public MyCustKeybords(Context context) {
        this(context, null);
    }

    public MyCustKeybords(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustKeybords(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustKeybords);
        needSystem = typedArray.getBoolean(R.styleable.MyCustKeybords_needSystem, true);
        typedArray.recycle();
        init();
    }

    public void init() {
        View keyBords = LayoutInflater.from(getContext()).inflate(R.layout.view_keybords, this);
        recyView=findViewById(R.id.recy_view);
        layoutDel=findViewById(R.id.layout_del);
        layoutSpot=findViewById(R.id.layout_spot);
        layoutZero=findViewById(R.id.layout_zero);
        layoutSystem=findViewById(R.id.layout_system);
        btnConfirm=findViewById(R.id.btn_confirm);
        layoutSystem.setVisibility(needSystem ? VISIBLE : GONE);
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isFastClick()) {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                }
            }
        });

        layoutSystem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    KeyboardUtils.showSoftInput(editText);
                }
            }
        });
        //删除的点击事件
        layoutDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null)
                    delete();
            }
        });
        //点的点击事件
        layoutSpot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("sssssssss:"+(editText != null));
                if (editText != null) {
                    inset(".");
                }
            }
        });


        //0的点击事件
        layoutZero.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("sssssssss:"+(editText != null));
                if (editText != null) {
                    inset("0");
                }
            }
        });



    }

    public void bindEditText(EditText editText) {
        this.editText = editText;
        if (editText != null) {
            editText.setOnEditorActionListener(editorActionListener);
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (listener != null) {
                    listener.onConfirm();
                }
            }
            return false;
        }
    };


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int itemHeight = (getMeasuredHeight() - 2) / 4;
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        adapter = new BaseAdapter<String>(R.layout.item_keybords) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                View itemView = helper.itemView;
                itemView.setLayoutParams(params);
                helper.setText(R.id.tv_name, item);

                if(item.contains("x")){
                    helper.setGone(R.id.delete_image,true);
                    helper.setGone(R.id.tv_name, false);
                }else {
                    helper.setGone(R.id.delete_image,false);
                }
            }

        };
        recyView.setLayoutManager(new XGridLayoutManager(getContext(), 3));
        recyView.addItemDecoration(new GridDividerItemDecoration(getContext(), 3, 0, Color.parseColor("#D8D8D8")));
        adapter.bindToRecyclerView(recyView);
        initData(1);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter ad, View view, int position) {
                if (editText != null) {
                    if (position==list.size()-1){
                            delete();
                    }else {
                        inset(adapter.getItem(position));
                    }
                }
            }
        });
        //长按删除
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (editText != null) {
                    if (position==list.size()-1){
                        deleteALL(view);
                    }
                }
                return false;
            }
        });
    }


    public void inset(String text) {
        int index = editText.getSelectionStart();//获取光标所在位置
        int selectionEnd = editText.getSelectionEnd();
        Editable edit = editText.getEditableText();//获取EditText的文字
        if (selectionEnd > index) {
            edit.replace(index, selectionEnd, "");
            KLog.d("11111111111");
            inset(text);
            return;
        }

        if (index < 0 || index >= edit.length()) {
            KLog.d("2222222222222222");
            edit.append(text);
        } else {
            KLog.d("3333333333333333");
            edit.insert(index, text);

        }
    }

    /**
     * 删除光标之前的字符串
     */
    public void delete() {
        int index = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        Editable editable = editText.getText();
        if (selectionEnd > index) {
            editable.delete(index, selectionEnd);
        } else {
            if (index <= 0) {
                return;
            }
            editable.delete(index - 1, index);
        }
    }

    /**
     * 长按删除字符串
     */
    public void deleteALL(final View view) {
        _timer = new Timer();
        TimerTask _task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Editable _editable = editText.getText();
                        int length = _editable.length();
                        editText.setSelection(length);
                        if (view.isPressed() && length > 0) {//按下删除
                            _editable.delete(length - 1, length);
                        }else {//取消
                            _timer.cancel();
                        }
                    }
                });
            }
        };
        _timer.schedule(_task, 0, 60);
    }

    private void initData(int type) {

        if (type == 1) {
            for (int i = 1; i < 10; i++) {
                //添加1-9
                list.add(String.valueOf(i));
            }
            list.add("0");
            list.add(".");
            list.add("x");
        }
        adapter.setNewData(list);
    }



    public interface OnKeyBoradConfirm {
        void onConfirm();

    }

}
