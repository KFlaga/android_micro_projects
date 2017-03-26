package scrabby.scrabblehelper;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.ArrayList;

class Letter
{
    char Char;
    BonusType Bonus;

    public Letter(char c)
    {
        Char = c;
        Bonus = BonusType.Bonus_None;
    }

    enum BonusType
    {
        Bonus_None,
        Bonus_Letter_x2,
        Bonus_Letter_x3,
        Bonus_Word_x2
    }
}

class LetterSequence extends ArrayList<Letter>
{
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(size());

        for(Letter letter : this)
        {
            stringBuilder.append(letter.Char);
        }

        return stringBuilder.toString();
    }

    public String toHtml()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(Letter letter : this)
        {
            if(letter.Bonus == Letter.BonusType.Bonus_None)
            { stringBuilder.append(letter.Char); }
            else if(letter.Bonus == Letter.BonusType.Bonus_Letter_x2)
            { stringBuilder.append("<font color='yellow'>" + letter.Char + "</font>"); }
            else if(letter.Bonus == Letter.BonusType.Bonus_Letter_x3)
            { stringBuilder.append("<font color='blue'>" + letter.Char + "</font>"); }
            else if(letter.Bonus == Letter.BonusType.Bonus_Word_x2)
            { stringBuilder.append("<font color='red'>" + letter.Char + "</font>"); }
        }

        return stringBuilder.toString();
    }
}


// Specjalny EditText zapisujacy znaki w LetterSequence, co pozwala na kolorowanie
// bonusowych liter. Input powinien byc obslugiwany wylacznie poprzez 'injectKey'
// TODO: rozwiazac problem nieklikalnego tekstu by zmienic polozenie kursora
public class ScrabbyEditText extends EditText
{
    static final int KeyCodeDelete = 10000;
    static final int KeyCodeHide = 10001;
    static final int KeyCodeBlank = 10002;
    static final int KeyCodeBonus_L2 = 10003;
    static final int KeyCodeBonus_L3 = 10004;
    static final int KeyCodeBonus_W2 = 10005;
    static final int KeyCodeBonus_None = 10006;
    static final int KeyCodeLeft = 10007;
    static final int KeyCodeRight = 10008;

    private LetterSequence _letters;

    public ScrabbyEditText(Context context)
    {
        super(context);
        init(context, null, 0);
    }

    public ScrabbyEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScrabbyEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle)
    {
        _letters = new LetterSequence();
    }

    public LetterSequence getLetters()
    {
        return _letters;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int inType = getInputType();       // Backup the input type
        setInputType(InputType.TYPE_NULL); // Disable standard keyboard
        super.onTouchEvent(event);         // Call native handler
        setInputType(inType);              // Restore input type
        return false;
    }

    // Prcesses input from ScrabbyKeyboard
    public void injectKey(int keyCode)
    {
        Editable editable = getText();
        int start = getSelectionStart();
        int finalSelectionIdx = start;

        if(start > 0)
        {
            if(keyCode == KeyCodeDelete)
            {
                _letters.remove(start - 1);
                finalSelectionIdx = start - 1;
            }
            else if(keyCode == KeyCodeBonus_None)
            {
                _letters.get(start - 1).Bonus = Letter.BonusType.Bonus_None;
            }
            else if(keyCode == KeyCodeBonus_L2)
            {
                _letters.get(start - 1).Bonus = Letter.BonusType.Bonus_Letter_x2;
            }
            else if(keyCode == KeyCodeBonus_L3)
            {
                _letters.get(start - 1).Bonus = Letter.BonusType.Bonus_Letter_x3;
            }
            else if(keyCode == KeyCodeBonus_W2)
            {
                _letters.get(start - 1).Bonus = Letter.BonusType.Bonus_Word_x2;
            }
        }

        if(keyCode == KeyCodeBlank)
        {
            keyCode = '?';
        }

        if(keyCode < 10000)
        {
            if(start == editable.length())
            {
                _letters.add(new Letter((char) keyCode));
            }
            else
            {
                _letters.add(start, new Letter((char) keyCode));
            }
            finalSelectionIdx = start + 1;
        }
        else if(keyCode == KeyCodeLeft && start > 0)
        {
            finalSelectionIdx = start - 1;
        }
        else if(keyCode == KeyCodeRight && start < editable.length())
        {
            finalSelectionIdx = start + 1;
        }

        updateText();
        this.setSelection(finalSelectionIdx);
    }

    void updateText()
    {
        String finalHtml = _letters.toHtml();
        this.setText(Html.fromHtml(finalHtml), BufferType.SPANNABLE);
    }
}
