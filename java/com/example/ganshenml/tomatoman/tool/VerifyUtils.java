package com.example.ganshenml.tomatoman.tool;

/**
 * Created by ganshenml on 2016-04-30.
 * 专用于校验合法性，并输出检验提示
 */
public class VerifyUtils {
    //检验用户名：1.长度校验：4-14；2.类型校验：中文7个，英文和数值组成；3.不含空字符串校验；4.用户名是否存在
    public static String verifyName(String nameStr){

        //长度判断
        int length = nameStr.length();
        if(length>14){//疑问：此时的中文会自动占用两个字符吗？
            return "长度超过了"+(length-14)+"个字符";
        }

        //类型校验
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        if (!nameStr.matches(regex)){
            return "不能包含数字、字母和汉字以外的字符";
        }

        //不含空字符串校验
        if(nameStr.contains(" ")){
            return "不能有空格";
        }

        //用户名是否存在

        return "";
    }

    //检验密码：1.长度校验：4-16；2.类型校验：英文（含大小写），数字；3.不含空字符串校验
    public static String verifyPassword(String passwordStr){

        //长度判断
        int length = passwordStr.length();
        if(length>16){//疑问：此时的中文会自动占用两个字符吗？
            return "长度超过了"+(length-16)+"个字符";
        }

        //类型校验
        String regex = "^[a-z0-9A-Z]+$";
        if (!passwordStr.matches(regex)){
            return "不能包含数字、字母和汉字以外的字符";
        }

        //不含空字符串校验
        if(passwordStr.contains(" ")){
            return "不能有空格";
        }

        return "";
    }


}
