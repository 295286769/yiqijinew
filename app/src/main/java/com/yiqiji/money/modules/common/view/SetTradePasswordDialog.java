package com.yiqiji.money.modules.common.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.DisplayUtil;

public class SetTradePasswordDialog extends Dialog implements View.OnKeyListener {
	// private Context mContext;
	private View mRootLayout;
	private EditText password1, password2, password3, password4, password5, password6;
	private int mViewId = 0;
	private OnFinishListener onFinishListener;
	private ImageButton mImageButton;
	private int[] mId = { R.id.password1, R.id.password2, R.id.password3, R.id.password4, R.id.password5,
			R.id.password6 };
	private View btn_confirm;

	@SuppressLint("InflateParams")
	public SetTradePasswordDialog(Context context) {
		this(context, R.style.My_CustomDialog);
		// TODO Auto-generated constructor stub
		// mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootLayout = (View) inflater.inflate(R.layout.dialog_input_trade_password_layout, null);
		setContentView(mRootLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		mImageButton = (ImageButton) mRootLayout.findViewById(R.id.dismiss);
		btn_confirm = mRootLayout.findViewById(R.id.btn_comfirm_dialog_set_trade_password);
		initView(mRootLayout);
		// 接收键盘事件
		password1.setFocusable(true);
		// 接收触摸事件,两个事件连着用
		password1.setFocusableInTouchMode(true);
		Window window = getWindow();
		window.getAttributes().width = DisplayUtil.getWidth();
		window.setGravity(Gravity.BOTTOM);
		// 获得焦点
		password1.requestFocus();
		mImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	public SetTradePasswordDialog(Context context, int theme) {
		super(context, theme);
	}

	void initView(View view) {
		password1 = (EditText) view.findViewById(R.id.password1);
		password2 = (EditText) view.findViewById(R.id.password2);
		password3 = (EditText) view.findViewById(R.id.password3);
		password4 = (EditText) view.findViewById(R.id.password4);
		password5 = (EditText) view.findViewById(R.id.password5);
		password6 = (EditText) view.findViewById(R.id.password6);
		// 设置password控件的改变事件
		password1.addTextChangedListener(textWatcher1);
		password2.addTextChangedListener(textWatcher2);
		password3.addTextChangedListener(textWatcher3);
		password4.addTextChangedListener(textWatcher4);
		password5.addTextChangedListener(textWatcher5);
		password6.addTextChangedListener(textWatcher6);
		// 设置password控件删除键的监听
		password1.setOnKeyListener(this);
		password2.setOnKeyListener(this);
		password3.setOnKeyListener(this);
		password4.setOnKeyListener(this);
		password5.setOnKeyListener(this);
		password6.setOnKeyListener(this);

		password2.setFocusable(false);
		password3.setFocusable(false);
		password4.setFocusable(false);
		password5.setFocusable(false);
		password6.setFocusable(false);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

	}

	/**
	 * 监听删除键 将当前控件a前面的控件b的值置空，让控件b获得焦点并可操作 然后将a控件设置不可操作 然后返回false
	 * 再会调用当前控件的textWatcher方法
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
			EditText editText = (EditText) findViewById(mViewId);
			editText.setText("");
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			int Id = mRootLayout.findFocus().getId();
			setFocus(Id);
			return false;
		}
		return false;
	}

	public void setOnFinishListener(OnFinishListener onFinishLesterner) {
		this.onFinishListener = onFinishLesterner;
	}

	public interface OnFinishListener {
		void onfinish(String password);
	}

	public void setOnComfirmClickLisstener(View.OnClickListener onClickListener) {
		btn_confirm.setOnClickListener(onClickListener);
	}

	/**
	 * 刚进入时都不可点击 输完值后将当前控件a设为不可操作，将下面的控件b设置获得焦点并可操作
	 */
	private TextWatcher textWatcher1 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (password1.length() != 0) {
				password1.setFocusable(false);
				password2.setFocusable(true);
				password2.setFocusableInTouchMode(true);
				password2.requestFocus();
				mViewId = R.id.password1;
			}
		}
	};

	private TextWatcher textWatcher2 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (password2.length() != 0) {
				password2.setFocusable(false);
				password3.setFocusable(true);
				password3.setFocusableInTouchMode(true);
				password3.requestFocus();
				mViewId = R.id.password2;
			} else {
				mViewId = R.id.password1;
			}

		}
	};
	private TextWatcher textWatcher3 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {

			if (password3.length() != 0) {
				password3.setFocusable(false);
				password4.setFocusable(true);
				password4.setFocusableInTouchMode(true);
				password4.requestFocus();
				mViewId = R.id.password3;
			} else {
				mViewId = R.id.password2;
			}
		}
	};
	private TextWatcher textWatcher4 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (password4.length() != 0) {
				password4.setFocusable(false);
				password5.setFocusable(true);
				password5.setFocusableInTouchMode(true);
				password5.requestFocus();
				mViewId = R.id.password4;
			} else {
				mViewId = R.id.password3;
			}
		}
	};
	private TextWatcher textWatcher5 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (password5.length() != 0) {
				password5.setFocusable(false);
				password6.setFocusable(true);
				password6.setFocusableInTouchMode(true);
				password6.requestFocus();
				mViewId = R.id.password5;
			} else {
				mViewId = R.id.password4;
			}
		}
	};
	private TextWatcher textWatcher6 = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String password = password1.getText().toString() + password2.getText().toString()
					+ password3.getText().toString() + password4.getText().toString() + password5.getText().toString()
					+ password6.getText().toString();
			onFinishListener.onfinish(password);
		}
	};

	private void setFocus(int viewId) {
		for (int i = 0; i < mId.length; i++) {
			if (mId[i] == viewId) {
				EditText editText = (EditText) findViewById(mId[i + 1]);
				editText.setFocusable(false);
			}
		}
	}
}
