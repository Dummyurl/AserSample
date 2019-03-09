package info.pratham.asersample.utility;

import java.util.List;

public class ListConstant {
    public static int FIVE = 5;
    public static int TWO = 2;
    public static int ONE = 1;

    //native words
    public static List Words;
    public static List Letters;
    public static List Capital;
    public static List Small;


    public static List Single;
    public static List Double;
    public static List Subtraction;
    public static List Division;

    //English
    public static List engWord;

    public static int Words_cnt = 0;
    public static int Letters_cnt = 0;
    public static int Capital_cnt = 0;
    public static int Small_cnt;


    public static int Single_cnt = 0;
    public static int Double_cnt;
    public static int Subtraction_cnt;
    public static int Division_cnt;

    //English
    public static int engWord_cnt;


    public static void clearFields() {
        Words = null;
        Letters = null;
        Capital = null;
        Small = null;
        Single = null;
        Double = null;
        Subtraction = null;
        Division = null;
        engWord = null;

        Words_cnt = 0;
        Letters_cnt = 0;
        Capital_cnt = 0;
        Small_cnt = 0;
        Single_cnt = 0;
        Double_cnt = 0;
        Subtraction_cnt = 0;
        Division_cnt = 0;
        engWord_cnt = 0;
    }
}
