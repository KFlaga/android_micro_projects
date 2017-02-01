package scrabby.scrabblehelper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    WordValidityChecker _wordChecker;
    PointCounter _pointsCounter;

    boolean _dictionaryLoaded = false;
    boolean _helpVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkWordButton = (Button) findViewById(R.id._buttonCheckWord);
        checkWordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(_dictionaryLoaded)
                {
                    checkWordValidityButtonClicked();
                }
            }
        });

//        FloatingActionButton helpButton = (FloatingActionButton) findViewById(R.id._floatButtonHelp);
//        helpButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                helpButtonClicked();
//            }
//        });

        TextView labelResult = (TextView) findViewById(R.id._labelResults);
        labelResult.setText(getResources().getText(R.string.dict_loading));

        // Ladujemy slownik asynchronicznie
        LoadDictionaryAsync loadDictionaryAsync = new LoadDictionaryAsync();
        loadDictionaryAsync.execute(this);
    }

    void checkWordValidityButtonClicked()
    {
        EditText editWord = (EditText) findViewById(R.id._editWord);
        TextView labelResult = (TextView) findViewById(R.id._labelResults);
        String word = editWord.getText().toString();
        WordValidityChecker.ValidationResult result = _wordChecker.checkWordIsValid(word);;
        if(result.IsValid)
        {
            StringBuilder msg = new StringBuilder();
            msg.append("Słowo: [");
            msg.append(word);
            msg.append("] jest dopuszczalne. Warte: [");
            msg.append(_pointsCounter.getPointsForWord(word));
            msg.append("] punktów");
            labelResult.setText(msg);
        }
        else
        {
            StringBuilder msg = new StringBuilder();
            msg.append("Słowo: [");
            msg.append(word);
            msg.append("] jest NIE dopuszczalne. ");
            msg.append(result.InvalidReson);
            labelResult.setText(msg);
        }
    }

    void dictionaryLoaded(Dictionary dict)
    {
        _pointsCounter = new PointCounter();
        _wordChecker = new WordValidityChecker(dict);
        _dictionaryLoaded = true;

        TextView labelResult = (TextView) findViewById(R.id._labelResults);
        labelResult.setText(getResources().getText(R.string.dict_loaded));
    }

//    void helpButtonClicked()
//    {
//        if(_helpVisible)
//        {
//
//        }
//        else
//        {
//
//        }
//        _helpVisible = !_helpVisible;
//    }

    class LoadDictionaryAsync extends AsyncTask<Context, Void, Void>
    {
        private Dictionary _dictionary;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Context... params)
        {
            Context context = params[0];
            Dictionary.IDictionaryLoader dictionaryLoader = new TxtDictionaryLoader(context);
            _dictionary = new Dictionary(dictionaryLoader);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            dictionaryLoaded(_dictionary);
        }
    }
}
