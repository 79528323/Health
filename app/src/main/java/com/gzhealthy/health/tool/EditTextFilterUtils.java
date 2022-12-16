package com.gzhealthy.health.tool;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextFilterUtils {

    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     */
    private static class SingletonHolder {
        private static EditTextFilterUtils instance = new EditTextFilterUtils();
    }

    /**
     * 私有的构造函数
     */
    private EditTextFilterUtils() {
    }

    public static EditTextFilterUtils getInstance() {
        return SingletonHolder.instance;
    }

    public static void setEmojiFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{EMOJI_FILTER});
    }


    //过滤表情字符
    private static InputFilter EMOJI_FILTER = new InputFilter() {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^` ，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]");

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = pattern.matcher(source);
            if (!matcher.find()) {
                return null;
            } else {
                return "";
            }

//            for (int index = start; index < end; index++) {
//
//                int type = Character.getType(source.charAt(index));
//
//                if (type == Character.SURROGATE) {
//                    return "";
//                }
//            }
//            return null;
        }
    };

    public static InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                ToastUtils.showShort("不能包含表情字符");
                return "";
            }

        }
    };

}
