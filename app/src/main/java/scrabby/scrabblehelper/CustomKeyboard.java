package scrabby.scrabblehelper;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static scrabby.scrabblehelper.ScrabbyEditText.KeyCodeHide;

class CustomKeyboard
{
    private KeyboardView _keyboardView;
    private Activity _hostActivity;

    private KeyboardView.OnKeyboardActionListener _onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener()
    {
        public void onKey(int primaryCode, int[] keyCodes)
        {
            View focusCurrent = _hostActivity.getWindow().getCurrentFocus();
            if(focusCurrent == null)
            {
                return;
            }

            ScrabbyEditText edittext = (ScrabbyEditText) focusCurrent;

            if(primaryCode == KeyCodeHide)
            {
                hideCustomKeyboard();
            }
            else
            {
                // insert character
                edittext.injectKey(primaryCode);
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

    /**
     * Create a custom keyboard, that uses the KeyboardView (with resource id <var>viewid</var>) of the <var>host</var> activity,
     * and load the keyboard layout from xml file <var>layoutid</var> (see {@link Keyboard} for description).
     * Note that the <var>host</var> activity must have a <var>KeyboardView</var> in its layout (typically aligned with the bottom of the activity).
     * Note that the keyboard layout xml file may include key codes for navigation; see the constants in this class for their values.
     * Note that to enable EditText's to use this custom keyboard, call the {@link #registerEditText(ScrabbyEditText)}.
     *
     * @param host     The hosting activity.
     * @param viewid   The id of the KeyboardView.
     * @param layoutid The id of the xml file containing the keyboard layout.
     */
    public CustomKeyboard(Activity host, int viewid, int layoutid)
    {
        _hostActivity = host;
        _keyboardView = (KeyboardView) _hostActivity.findViewById(viewid);
        _keyboardView.setKeyboard(new Keyboard(_hostActivity, layoutid));
        _keyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        _keyboardView.setOnKeyboardActionListener(_onKeyboardActionListener);
        // Hide the standard keyboard initially
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
        // Make the custom keyboard appear
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

        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }
}
