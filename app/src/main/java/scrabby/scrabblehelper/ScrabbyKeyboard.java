package scrabby.scrabblehelper;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static scrabby.scrabblehelper.ScrabbyEditText.KeyCodeHide;

class ScrabbyKeyboard
{
    private KeyboardView _keyboardView;
    private Activity _hostActivity;

    private KeyboardView.OnKeyboardActionListener _onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener()
    {
        public void onKey(int primaryCode, int[] keyCodes)
        {
            View focusView = _hostActivity.getWindow().getCurrentFocus();
            if(focusView == null)
            {
                return;
            }

            ScrabbyEditText editText = (ScrabbyEditText) focusView;

            if(primaryCode == KeyCodeHide)
            {
                hideCustomKeyboard();
            }
            else
            {
                editText.injectKey(primaryCode);
            }
        }

        public void onPress(int arg0) { }
        public void onRelease(int primaryCode) { }
        public void onText(CharSequence text) { }
        public void swipeDown() { }
        public void swipeLeft() { }
        public void swipeRight() { }
        public void swipeUp(){ }
    };

    public ScrabbyKeyboard(Activity host, int viewid, int layoutid)
    {
        _hostActivity = host;
        _keyboardView = (KeyboardView)host.findViewById(viewid);
        _keyboardView.setKeyboard(new Keyboard(_hostActivity, layoutid));
        _keyboardView.setPreviewEnabled(false);
        _keyboardView.setOnKeyboardActionListener(_onKeyboardActionListener);

        _hostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public boolean isCustomKeyboardVisible()
    {
        return _keyboardView.getVisibility() == View.VISIBLE;
    }

    public void showCustomKeyboard(View v)
    {
        _keyboardView.setVisibility(View.VISIBLE);
        _keyboardView.setEnabled(true);
        if(v != null)
        {
            ((InputMethodManager) _hostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void hideCustomKeyboard()
    {
        _keyboardView.setVisibility(View.GONE);
        _keyboardView.setEnabled(false);
    }

    public void registerEditText(ScrabbyEditText edittext)
    {
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus) { showCustomKeyboard(v); }
                else { hideCustomKeyboard(); }
            }
        });

        edittext.setOnClickListener(new View.OnClickListener()
        {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v)
            {
                showCustomKeyboard(v);
            }
        });

        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(!isCustomKeyboardVisible())
                    showCustomKeyboard(view);

                return false;
            }
        });
    }
}
