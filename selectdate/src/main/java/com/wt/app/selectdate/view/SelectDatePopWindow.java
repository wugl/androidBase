package com.wt.app.selectdate.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wt.app.selectdate.R;
import com.wt.app.selectdate.view.wheelview.OnWheelChangedListener;
import com.wt.app.selectdate.view.wheelview.OnWheelScrollListener;
import com.wt.app.selectdate.view.wheelview.WheelView;
import com.wt.app.selectdate.view.wheelview.adapter.AbstractWheelTextAdapter1;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wuguilin on 1/9/2017.
 */


public class SelectDatePopWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "SelectDatePopWindow";

    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;
    private WheelView wvHour;
    private WheelView wvMinute;
    private View lySelectDate;
    private View lySelectDateChild;
    private TextView btnSure;
    private TextView btnCancel;

    private LinearLayout hourContainer, minuteContainer;


    private String[] mYearDatas, mMonthDatas, mDayDatas, mHourDatas, mMinuteDatas;


    private DateTextAdapter yearAdapter;
    private DateTextAdapter monthAdapter;
    private DateTextAdapter dayAdapter;
    private DateTextAdapter hourAdapter;
    private DateTextAdapter minuteAdapter;

    private String strYear;
    private String strMonth;
    private String strDay;
    private String strHour;
    private String strMinute;
    private OnDateCListener onDateCListener;

    private int maxsize = 14;
    private int minsize = 12;
    private Calendar calendar = Calendar.getInstance();

    public SelectDatePopWindow(final Context context) {
        this(context, true,null,null,null,null,null);
    }
    public SelectDatePopWindow(final Context context, boolean showTime) {
        this(context, showTime,null,null,null,null,null);
    }
    public SelectDatePopWindow(final Context context,String year,String month,String day, String hour,String minute) {
        this(context, true, year, month, day,  hour, minute);
    }
    public SelectDatePopWindow(final Context context, boolean showTime,String year,String month,String day, String hour,String minute) {
        super(context);

        View view = View.inflate(context, R.layout.select_date_pop_layout, null);

        wvYear = (WheelView) view.findViewById(R.id.wv_date_year);
        wvMonth = (WheelView) view.findViewById(R.id.wv_date_month);
        wvDay = (WheelView) view.findViewById(R.id.wv_date_day);
        wvHour = (WheelView) view.findViewById(R.id.wv_date_hour);
        wvMinute = (WheelView) view.findViewById(R.id.wv_date_minute);
        lySelectDate = view.findViewById(R.id.select_date);
        lySelectDateChild = view.findViewById(R.id.select_date_child);
        btnSure = (TextView) view.findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) view.findViewById(R.id.btn_myinfo_cancel);

        hourContainer = (LinearLayout) view.findViewById(R.id.hour_container);
        minuteContainer = (LinearLayout) view.findViewById(R.id.minute_container);
        if (showTime) {
            hourContainer.setVisibility(View.VISIBLE);
            minuteContainer.setVisibility(View.VISIBLE);
        } else {
            hourContainer.setVisibility(View.GONE);
            minuteContainer.setVisibility(View.GONE);
        }


        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        lySelectDateChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initDatas( year, month, day,  hour, minute);

        yearAdapter = new DateTextAdapter(context, mYearDatas, Integer.parseInt(strYear) - calendar.get(Calendar.YEAR) + 100, maxsize, minsize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(yearAdapter);
        wvYear.setCurrentItem(Integer.parseInt(strYear) - calendar.get(Calendar.YEAR) + 100);


        monthAdapter = new DateTextAdapter(context, mMonthDatas, Integer.parseInt(strMonth) - 1, maxsize, minsize);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(monthAdapter);
        wvMonth.setCurrentItem(Integer.parseInt(strMonth) - 1);

        //initDays(mAreaDatasMap.get(strMonth));
        dayAdapter = new DateTextAdapter(context, mDayDatas, Integer.parseInt(strDay) - 1, maxsize, minsize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(dayAdapter);
        wvDay.setCurrentItem(Integer.parseInt(strDay) - 1);

        hourAdapter = new DateTextAdapter(context, mHourDatas, Integer.parseInt(strHour), maxsize, minsize);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(hourAdapter);
        wvHour.setCurrentItem(Integer.parseInt(strHour));

        minuteAdapter = new DateTextAdapter(context, mMinuteDatas, Integer.parseInt(strMinute), maxsize, minsize);
        wvMinute.setVisibleItems(5);
        wvMinute.setViewAdapter(minuteAdapter);
        wvMinute.setCurrentItem(Integer.parseInt(strMinute));

        wvYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                strYear = currentText;
                setTextviewSize(currentText, yearAdapter);


                mDayDatas = getDays(Integer.parseInt(strYear), Integer.parseInt(strMonth));
                dayAdapter = new DateTextAdapter(context, mDayDatas, 0, maxsize, minsize);
                wvDay.setVisibleItems(5);
                wvDay.setViewAdapter(dayAdapter);
                wvDay.setCurrentItem(0);
                setTextviewSize("0", dayAdapter);
            }
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, yearAdapter);
            }
        });

        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                strMonth = currentText;
                Log.e(TAG, "onChanged:month: " + strMonth);
                setTextviewSize(currentText, monthAdapter);


                mDayDatas = getDays(Integer.parseInt(strYear), Integer.parseInt(strMonth));
                dayAdapter = new DateTextAdapter(context, mDayDatas, 0, maxsize, minsize);
                wvDay.setVisibleItems(5);
                wvDay.setViewAdapter(dayAdapter);
                wvDay.setCurrentItem(0);
                setTextviewSize("0", dayAdapter);


            }
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, monthAdapter);
            }
        });

        wvDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                strDay = currentText;
                setTextviewSize(currentText, dayAdapter);
            }
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
            }
        });


        wvHour.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                strHour = currentText;
                setTextviewSize(currentText, hourAdapter);
            }
        });

        wvHour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub

                String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, hourAdapter);
            }
        });
        wvMinute.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                strMinute = currentText;
                setTextviewSize(currentText, minuteAdapter);
            }
        });

        wvMinute.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, minuteAdapter);
            }
        });

    }


    private class DateTextAdapter extends AbstractWheelTextAdapter1 {
        String[] list;

        protected DateTextAdapter(Context context, String[] list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_select, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            // if (getItemsCount() == 0) return "";
            return list[index];
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, DateTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(14);
            } else {
                textvew.setTextSize(12);
            }
        }
    }

    public void setDatekListener(OnDateCListener onDateCListener) {
        this.onDateCListener = onDateCListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSure) {
            if (onDateCListener != null) {
                Log.e(TAG, "onClick: " + strMonth);

                onDateCListener.onClick(strYear, strMonth, strDay, strHour, strMinute);
            }
        } else if (v == btnCancel) {

        } else if (v == lySelectDateChild) {
            return;
        } else {
//			dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnDateCListener {
        void onClick(String year, String month, String day, String hour, String minute);
    }

    /**
     * 从文件中读取地址数据
     */
/*    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/" + "city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    private void initDatas(String ye,String mo,String da, String ho,String mi) {
        int year, month, day, hour, minute;

        if(ye!=null && !ye.equals("")){
            year = Integer.parseInt(ye);
        }else {

            year = calendar.get(Calendar.YEAR);
        }

        if(mo!=null && !mo.equals("")){
            month = Integer.parseInt(mo);
        }else {
            month = calendar.get(Calendar.MONTH) + 1;
        }

        if(da!=null && !da.equals("")){
            day = Integer.parseInt(da);
        }else {
            day = calendar.get(Calendar.DATE);
        }

        if(ho!=null && !ho.equals("")){
            hour = Integer.parseInt(ho);
        }else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }

        if(mi!=null && !mi.equals("")){
            minute = Integer.parseInt(mi);
        } else {
            minute = calendar.get(Calendar.MINUTE);
        }
        strYear = String.valueOf(year);
        strMonth = (month < 10 ? "0" : "") + String.valueOf(month);
        strDay = (day < 10 ? "0" : "") + String.valueOf(day);
        strHour = (hour < 10 ? "0" : "") + String.valueOf(hour);
        strMinute = (minute < 10 ? "0" : "") + String.valueOf(minute);

        mYearDatas = new String[150];
        //int year = calendar.get(Calendar.YEAR);
        for (int i = year - 100, j = 0; i < year + 50; i++, j++) {
            mYearDatas[j] = i + "";
        }
        mMonthDatas = new String[12];
        for (int i = 0; i < 12; i++) {
            if (i < 9)
                mMonthDatas[i] = "0" + (i + 1);
            else
                mMonthDatas[i] = i + 1 + "";

        }
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Log.e(TAG, "initDatas: "+ strYear+":"+strMonth+":"+strDay+":"+count);


        mDayDatas = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < 9)
                mDayDatas[i] = "0" + (i + 1);
            else
                mDayDatas[i] = (i + 1) + "";

        }
        mHourDatas = new String[24];
        for (int i = 0; i < 24; i++) {
            if (i < 10)
                mHourDatas[i] = "0" + i;
            else
                mHourDatas[i] = i + "";

            //  Log.e(TAG, "initDatas: "+mHourDatas[i] );

        }
        mMinuteDatas = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10)
                mMinuteDatas[i] = "0" + i;
            else
                mMinuteDatas[i] = i + "";

        }
    }

    private String[] getDays(int year, int month) {
        String[] datas;
        Calendar newCal = Calendar.getInstance();
        // newCal.s
        newCal.set(Calendar.YEAR, year);
        newCal.set(Calendar.MONTH, month - 1);
        int count = newCal.getActualMaximum(Calendar.DAY_OF_MONTH);//new Date(year,month,0).getDate();

        Log.e(TAG, "getDays: " + year + ":" + month + ":" + count);
        datas = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < 9)
                datas[i] = "0" + (i + 1);
            else
                datas[i] = (i + 1) + "";

        }
        return datas;


    }


}
