package ru.trilla.masking;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MaskingUtils {

    public String maskCommonText(String text) {
        if (text.length() < 2) {
            return "****";
        }
        return text.charAt(0) + "****" + text.charAt(text.length() - 1);
    }
}
