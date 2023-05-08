package com.qat.helper.tools;

import java.util.List;

public class CheckerHelper {

    private CheckerHelper(){}

    public static boolean areAllTrue( List<Boolean> list)
    {
        boolean areAllTrue=true;
        for(boolean b : list ) {
            if (!b){
                areAllTrue=false;
                break;
            }
        }

        return areAllTrue;
    }
}
