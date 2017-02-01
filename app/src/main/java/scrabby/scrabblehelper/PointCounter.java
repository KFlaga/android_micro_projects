package scrabby.scrabblehelper;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

// Liczy punktacje za podane slowo
public class PointCounter
{
    public static final char BLANK_CODE = '?';

    private Map<Character, Integer> _pointsForLetterMap; // Mapa z punktami dla kazdego mozliwego kodu znaku (mapa kod->punkty)

    public PointCounter()
    {
        _pointsForLetterMap = new TreeMap<>();

        // Dodajemy wszystkie obslugiwane kody znakow
        setPointsForLetter(BLANK_CODE, 0);
        setPointsForLetters("aeinorswz", 1);
        setPointsForLetters("cdklmpty", 2);
        setPointsForLetters("bghjłu", 3);
        setPointsForLetters("ąęfóśż", 5);
        setPointsForLetters("ć", 6);
        setPointsForLetters("ń", 7);
        setPointsForLetters("ż", 9);
    }

    // Ustawia wartosc punktowa danego znaku
    void setPointsForLetter(char code, int points)
    {
        _pointsForLetterMap.put(code, points);
    }

    // Ustawia wartosc punktowa dla wszystkich znakow w zadanym stringu
    void setPointsForLetters(String codes, int points)
    {
        for(int i = 0; i < codes.length(); ++i)
        {
            setPointsForLetter(codes.charAt(i), points);
        }
    }

    // Zwraca wartosc punktowa znaku
    // Jesli znak nie zostal dodany zwraca poprostu zero
    public int getPointsForLetter(char letter)
    {
        Integer points = _pointsForLetterMap.get(letter);
        if(points != null)
            return points;
        return 0;
    }

    // Zwraca wartosc punktowa znaku
    // Jesli slowo zawiera nie dodany znak, to nie ma za niego punktow
    // Konwertuje slowo na male litery
    public int getPointsForWord(String word)
    {
        int points = 0;
        word = word.toLowerCase(new Locale("pl"));

        // Dla kazdego znaku w slowie dodaj jego wartosc punktowa
        for(int i = 0; i < word.length(); ++i)
        {
            char letter = word.charAt(i);
            points += getPointsForLetter(letter);
        }

        return points;
    }
}
