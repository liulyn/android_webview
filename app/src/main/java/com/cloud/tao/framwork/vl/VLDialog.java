package com.cloud.tao.framwork.vl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class VLDialog
{

	public interface DialogCallBack<T> {

		void ok(T msg)  ;

		void cancel() ;

	}


	public static final void configSpinnerDialog(Context context, final Spinner spinner, String[] options)
	{
		configSpinnerDialog(context, spinner, options, -1, -1);
	}

	public static final void configSpinnerDialog(Context context, final Spinner spinner, String[] options, final int color, final int gravity)
	{
		int selected = 0;
		if(spinner.getTag() != null && (spinner.getTag() instanceof Integer))
			selected = (Integer) spinner.getTag();
		if(selected < 0 || selected >= options.length)
			selected = 0;
		spinner.setTag(selected);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(selected);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				spinner.setTag(position);
				TextView textView = (TextView) view;
				if(color != -1)
				{
					textView.setTextColor(color);
				}
				if(gravity != -1)
				{
					textView.setGravity(gravity);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	public static final void showOptionsSelectDialog(Context context, String title, String[] options, int checkIndex, boolean cancelable,
													 final DialogCallBack callBack)
	{
		if(checkIndex < 0 || checkIndex >= options.length)
			checkIndex = 0;
		final int[] selectedIndex = new int[] { checkIndex };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setSingleChoiceItems(options, checkIndex, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				selectedIndex[0] = which;
			}
		});
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() //TODO string
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				if(callBack != null) {
					callBack.ok(selectedIndex[0]);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() //TODO string
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				if(callBack != null){
					callBack.cancel();
				}

			}
		});

		AlertDialog dialog = builder.create();
		if(title != null && title.length() > 0)
			dialog.setTitle(title);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if(callBack != null){
					callBack.cancel();
				}
			}
		});
		dialog.show();
	}
	

	public static final void showSelectListialog(Context context, String title, String[] options, final DialogCallBack callBack)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(options, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				if(null != callBack) {
					callBack.ok(which);
				}
            }  
        });
		

		AlertDialog dialog = builder.create();
		if(title != null && title.length() > 0)
			dialog.setTitle(title);
		
		dialog.show();
	}

	public static final void showInputDialog(final Context context, String title, String text, int maxLength, boolean cancelable, final DialogCallBack callBack)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		if(title!=null && title.length()>0) dialog.setTitle(title);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if(callBack != null) {
					callBack.cancel();
				}
			}
		});

		final EditText editText = new EditText(context);
		if(text.length()>maxLength) text = text.substring(0, maxLength);
		editText.setText(text);
		InputFilter[] filters = { new LengthFilter(maxLength) };
		editText.setFilters(filters);
		dialog.setView(editText);
		
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() //TODO string
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(callBack != null)
				{
					callBack.ok(editText.getText().toString());
					InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive())
					{
						imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() //TODO string
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(null != callBack) {
					callBack.cancel();
				}
			}
		});
		dialog.show();
	}


	public static final AlertDialog showOkCancelDialog(Context context, String title, CharSequence message, String okLabel, String cancelLabel, final boolean cancelable,
												final DialogCallBack callBack)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		if(title != null && title.length() > 0)
			dialog.setTitle(title);
		if(message != null && message.length() > 0)
			dialog.setMessage(message);
		if(okLabel == null || okLabel.length() == 0)
			okLabel = context.getString(android.R.string.ok); //TODO string
		if(cancelLabel == null || cancelLabel.length() == 0)
			cancelLabel = context.getString(android.R.string.cancel); //TODO string
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if(callBack != null){
					callBack.cancel();
				}

			}
		});

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, okLabel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(null != callBack) {
					callBack.ok(null);
				}
			}
		});

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelLabel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(null != callBack) {
					callBack.cancel();
				}
			}
		});
		dialog.show();
		return dialog;
	}

	public static final void showAlertDialog(Context context, String title, CharSequence message, boolean cancelable, final DialogCallBack callBack)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		if(title != null && title.length() > 0)
			dialog.setTitle(title);
		if(message != null && message.length() > 0)
			dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if(null != callBack) {
					callBack.cancel();
				}
			}
		});

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() //TODO string
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if( null != callBack) {
					callBack.ok(null);
				}
			}
		});
		dialog.show();
	}

	public static final ProgressDialog showOkCancelProgressDialog(Context context, String title, String message, String okLabel, String cancelLabel, final boolean cancelable, final DialogCallBack callBack)
	{
		ProgressDialog progressDialog = new ProgressDialog(context);
		if(title!=null && title.length()>0) progressDialog.setTitle(title);
		if(message!=null && message.length()>0) progressDialog.setMessage(message);
		progressDialog.setCancelable(cancelable);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				if(null != callBack) {
					callBack.cancel();
				}
			}
		});
		if(okLabel!=null)
		{
			progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, okLabel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if(callBack!=null) {
						callBack.ok(null);
					}
				}
			});
		}
		if(cancelLabel!=null)
		{
			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelLabel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if(null != callBack) {
						callBack.cancel();
					}
				}
			});
		}
		//progressDialog.show();
		return progressDialog;
	}
	
	public static final ProgressDialog showProgressDialog(Context context, String title, String message, boolean cancelable)
	{
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(cancelable);
		progressDialog.setCanceledOnTouchOutside(false);
		if(title != null && title.length() > 0)
			progressDialog.setTitle(title);
		if(message != null && message.length() > 0)
			progressDialog.setMessage(message);
		progressDialog.show();
		return progressDialog;
	}

	public static final void updateProgressDialog(ProgressDialog progressDialog, String title, String message)
	{
		if(title!=null && title.length()>0) progressDialog.setTitle(title);
		if(message!=null && message.length()>0) progressDialog.setMessage(message);
	}
	
	public static final void hideProgressDialog(ProgressDialog progressDialog)
	{
		if(progressDialog != null)
		{
			progressDialog.hide();
			try
			{
				progressDialog.dismiss();
			}
			catch (Exception e)
			{
			}
		}
	}
}
