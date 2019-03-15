package info.pratham.asersample.utility;

import java.util.List;

public class ListConstant {
    public static int FIVE = 5;
    public static int TWO = 2;
    public static int ONE = 1;

    //native words
    public static List Words;
    public static List Letters;
    public static List Para;
    public static List Story;

    //English
    public static List Capital;
    public static List Small;
    public static List engWord;
    public static List Sentence;

    //mathematics
    public static List Single;
    public static List Double;
    public static List Subtraction;
    public static List Division;

    //English

    public static int Words_cnt = 0;
    public static int Letters_cnt = 0;
    public static int Para_cnt = 0;
    public static int Story_cnt = 0;


    public static int Capital_cnt = 0;
    public static int Small_cnt;
    public static int engWord_cnt;
    public static int Sentence_cnt;


    public static int Single_cnt = 0;
    public static int Double_cnt;
    public static int Subtraction_cnt;
    public static int Division_cnt;

    //English


    public static void clearFields() {
        Words = null;
        Letters = null;
        Para = null;
        Story = null;

        Capital = null;
        Small = null;
        Sentence = null;
        engWord = null;

        Single = null;
        Double = null;
        Subtraction = null;
        Division = null;


        Words_cnt = 0;
        Letters_cnt = 0;
        Para_cnt = 0;
        Story_cnt = 0;

        Capital_cnt = 0;
        Small_cnt = 0;
        engWord_cnt = 0;
        Sentence_cnt = 0;

        Single_cnt = 0;
        Double_cnt = 0;
        Subtraction_cnt = 0;
        Division_cnt = 0;
    }
}
