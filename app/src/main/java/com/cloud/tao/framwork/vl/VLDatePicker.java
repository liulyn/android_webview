package com.cloud.tao.framwork.vl;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;

import java.util.Calendar;

import com.cloud.tao.framwork.Logger;

public class VLDatePicker extends DialogFragment
{
	private int				mYear;
	private int				mMonth;
	private int				mDay;
	private boolean			mLimitMaxDate;

	private DatePickerCallBack dateCallback;

	private boolean isCancle = false  ;

	public interface DatePickerCallBack {

		void ok(int year, int month, int day) ;

		void cancel() ;
	}

	public static void show(FragmentActivity activity, DatePickerCallBack callBack)
	{
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		show(activity, year, month, day, callBack);
	}

	public static void show(FragmentActivity activity, int year, int month, int day, DatePickerCallBack callBack)
	{
		show(activity, false, year, month, day, callBack);
	}

	public static void show(FragmentActivity activity, boolean limitMaxDate, int year, int month, int day, DatePickerCallBack callBack)
	{
		VLDatePicker datePicker = new VLDatePicker();
		if(month < 0)
			month = 0;
		if(month > 11)
			month = 11;
		if(day < 1)
			day = 1;
		if(day > 31)
			day = 31;
		datePicker.mYear = year;
		datePicker.mMonth = month;
		datePicker.mDay = day;
		datePicker.dateCallback = callBack;
		datePicker.mLimitMaxDate = limitMaxDate;
		datePicker.show(activity.getSupportFragmentManager(), VLDatePicker.class.getName());
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day)
			{
				Logger.msg("onDateSet");
				if(null != dateCallback && ! isCancle){
					dateCallback.ok(year,month,day);
				}
			}

		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay)
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				Logger.msg("which : " + which +"   "  + BUTTON_NEGATIVE +"  " + BUTTON_POSITIVE +"  " + BUTTON_NEUTRAL);
				if(which == BUTTON_NEGATIVE) {
					onCancel(dialog);
				} else if(which == BUTTON_POSITIVE){
					super.onClick(dialog, which);
				}
			}

			@Override
			protected Object clone() throws CloneNotSupportedException {
				return super.clone();
			}

			@Override
			protected void onStop() {
			}
		};
		datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), datePickerDialog);
		datePickerDialog.setCanceledOnTouchOutside(false);
		datePickerDialog.setCancelable(true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			DatePicker datePicker = datePickerDialog.getDatePicker();
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(1920, Calendar.JANUARY, 1);
			datePicker.setMinDate(calendar.getTimeInMillis());
			if(mLimitMaxDate)
			{
				datePicker.setMaxDate(System.currentTimeMillis());
			}
			else
			{
				calendar.set(2037, Calendar.DECEMBER, 31);
				datePicker.setMaxDate(calendar.getTimeInMillis());
			}
		}
		return datePickerDialog;
	}


	@Override
	public void onCancel(DialogInterface dialog)
	{
		isCancle = true ;
		Logger.msg("onCancel ");
		super.onCancel(dialog);
		if(null != dateCallback) {
			dateCallback.cancel();
		}
	}


}
