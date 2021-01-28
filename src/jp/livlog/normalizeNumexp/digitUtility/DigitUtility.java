package jp.livlog.normalizeNumexp.digitUtility;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import jp.livlog.normalizeNumexp.dictionaryDirpath.DictionaryDirpath;

public class DigitUtility extends AbsDigitUtility {

    public TreeMap <String, ENotationType> stringToNotationType     = new TreeMap <>();

    public TreeMap <String, Integer>       kansuji09ToValue         = new TreeMap <>();

    public TreeMap <String, Integer>       kansujiKuraiToPowerValue = new TreeMap <>();

    class ChineseCharacter {

        public ChineseCharacter(String character, int value, String notationType) {

            this.character = character;
            this.value = value;
            this.notationType = notationType;
        }

        @SerializedName ("character")
        @Expose
        public final String  character;

        @SerializedName ("value")
        @Expose
        public final Integer value;

        @SerializedName ("NotationType")
        @Expose
        public final String  notationType;
    }

    void loadJsonFromFile(final String filepath, ArrayList <ChineseCharacter> list) {

        final Reader reader = new InputStreamReader(
                DigitUtility.class.getResourceAsStream(filepath));

        final var gson = new Gson();
        final var collectionType = new TypeToken <Collection <ChineseCharacter>>() {
        }.getType();
        list = gson.fromJson(reader, collectionType);
    }


    void loadFromDictionary(final String dictionaryPath, ArrayList <ChineseCharacter> loadTarget) {

        loadTarget.clear();

        this.loadJsonFromFile(dictionaryPath, loadTarget);
    }


    @Override
    void initKansuji(String language) {

        final var chineseCharacters = new ArrayList <ChineseCharacter>();
        final var dictionaryPath = new StringBuilder(DictionaryDirpath.DICTIONARY_DIRPATH);
        if (language.equals("ja")) {
            dictionaryPath.append("ja/chinese_character.txt");
        } else if (language.equals("zh")) {
            dictionaryPath.append("zh/chinese_character.txt");
        } else {
            return;
        }

        this.loadFromDictionary(dictionaryPath.toString(), chineseCharacters);
        for (var i = 0; i < chineseCharacters.size(); i++) {
            var notationType = ENotationType.NOT_NUMBER;
            if (chineseCharacters.get(i).notationType.equals("09")) {
                notationType = ENotationType.KANSUJI_09;
            } else if (chineseCharacters.get(i).notationType.equals("sen")) {
                notationType = ENotationType.KANSUJI_KURAI_SEN;
            } else if (chineseCharacters.get(i).notationType.equals("man")) {
                notationType = ENotationType.KANSUJI_KURAI_MAN;
            }

            this.stringToNotationType.put(chineseCharacters.get(i).character, notationType);
            switch (notationType) {
                case KANSUJI_09:
                    this.kansuji09ToValue.put(chineseCharacters.get(i).character, chineseCharacters.get(i).value);
                    break;
                case KANSUJI_KURAI_MAN:
                case KANSUJI_KURAI_SEN:
                    this.kansujiKuraiToPowerValue.put(chineseCharacters.get(i).character, chineseCharacters.get(i).value);
                    break;
                default:
                    break;
            }

        }
        this.kansujiKuraiToPowerValue.put("　", 0);
    }


    @Override
    boolean isHankakusuji(String str) {

        var isResult = false;

        // チェック対象文字列がnullの場合はfalseを返す
        if (str == null) {
            return isResult;
        }

        if (str.matches("[0-9]+")) {
            isResult = true;
        }

        return isResult;
    }


    @Override
    boolean isZenkakusuji(String str) {

        var isResult = false;

        // チェック対象文字列がnullの場合はfalseを返す
        if (str == null) {
            return isResult;
        }

        if (str.matches("[０-９]+")) {
            isResult = true;
        }

        return isResult;
    }


    @Override
    boolean isArabic(String str) {

        return (this.isHankakusuji(str) || this.isZenkakusuji(str));
    }


    boolean isNotationType(final String str, ENotationType notationType) {

        final var check = this.stringToNotationType.get(str);
        if (check == null) {
            return false;
        }

        return check.getValue() == notationType.getValue();
    }


    @Override
    boolean isKansuji(String str) {

        return this.isNotationType(str, ENotationType.KANSUJI);
    }


    @Override
    boolean isKansuji09(String str) {

        return this.isNotationType(str, ENotationType.KANSUJI_09);
    }


    @Override
    boolean isKansujiKuraiSen(String str) {

        return this.isNotationType(str, ENotationType.KANSUJI_KURAI_SEN);
    }


    @Override
    boolean isKansujiKuraiMan(String str) {

        return this.isNotationType(str, ENotationType.KANSUJI_KURAI_MAN);
    }


    @Override
    boolean isKansujiKurai(String str) {

        return this.isNotationType(str, ENotationType.KANSUJI_KURAI);
    }


    @Override
    boolean isNumber(String str) {

        return (this.isHankakusuji(str) || this.isZenkakusuji(str) || this.isKansuji(str));
    }


    @Override
    boolean isComma(String str) {

        return (",".equals(str) || "、".equals(str) || "，".equals(str));
    }


    @Override
    boolean isDecimalPoint(String str) {

        return (".".equals(str) || "・".equals(str) || "．".equals(str));
    }


    @Override
    boolean isRangeExpression(String str) {

        return ("~".equals(str) || "〜".equals(str) || "～".equals(str) || "-".equals(str) || "−".equals(str) || "ー".equals(str) || "―".equals(str)
                || "から".equals(str));
    }


    @Override
    int convertKansuji09ToValue(String str) {

        final var value = this.kansuji09ToValue.get(str);
        if (value == null) {
            // 例外処理
            throw new NullPointerException("Exception : is not kansuji09");
        }

        return value;
    }


    @Override
    int convertKansujiKuraiToPowerValue(String str) {

        final var powerValue = this.kansujiKuraiToPowerValue.get(str);
        if (powerValue == null) {
            // 例外処理
            throw new NullPointerException("Exception : is not kansuji_kurai");
        }

        return powerValue;
    }

}
