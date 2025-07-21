package com.netra.commons.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BasicUtil {
    public Boolean validString(String string){
       return  string != null && !string.isBlank();
    }
}
