package com.nicknorman.com;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        App myApp = new App();
        String inputStr = "1,1,3,3,5,5,45,45";
        myApp.fooBarRsm(inputStr);
    }
    String fooBarRsm(String str)
    {
        List<String> used = new ArrayList<String>();
        String[] myList = str.split(",");
        int len = myList.length;
        StringBuilder builder = new StringBuilder();

        for(int i=0;i<len;i++)
        {
            String s = getFooBar(Integer.parseInt(myList[i]));
            String result = "";

            String copyLabel = used.contains(myList[i]) ? "-copy" : "";
            used.add(myList[i]);
            if(i==len-1){
                builder.append(s+copyLabel);
            }
            else
            {
                builder.append(s+copyLabel+",");
            }

        }
        System.out.println(builder.toString());
        return builder.toString();
    }
    String getFooBar(int number) {
        if (number % 15 == 0)
        {
            return "foobar";
        } else if (number % 5 == 0)
        {
            return "bar";
        }
        else if (number % 3 == 0)
        {
            return "foo";
        }
        return String.valueOf(number);
    }
}
