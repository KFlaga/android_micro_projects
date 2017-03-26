package scrabby.scrabblehelper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

// Laduje slownik z pliku "raw\dict.txt"
// Oczekiwany format : jedna linia - jeden wyraz
// Zapisuje slowa majace od 2 do 15 liter, powinnu byc zapisane malymi literami
class TxtDictionaryLoader implements Dictionary.IDictionaryLoader
{
    private Context _context;

    TxtDictionaryLoader(Context context)
    {
        _context = context;
    }

    public ArrayList<HashSet<String>> loadDictionary()
    {
        ArrayList<HashSet<String>> dictionary = new ArrayList<>(16);

        for(int i = 0; i <= 15; ++i)
        {
            dictionary.add(new HashSet<String>());
        }

        final InputStream file = _context.getResources().openRawResource(R.raw.dict);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        try
        {
            String line = reader.readLine();
            while(line != null)
            {
                int len = line.length();
                if(len >= 2 && len <= 15)
                {
                    HashSet<String> set = dictionary.get(len);
                    set.add(line);
                }

                line = reader.readLine();
            }
        }
        catch(IOException e)
        {
            Log.d("FromTxtFileLoader", "loadDictionary: failed to read dict file");
        }

        return dictionary;
    }
}
