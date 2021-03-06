package app.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @ClassName : app.utils.MathUtils
 * @Description :
 * @Date 2021-09-24 16:11:23
 * @Author ZhangHL
 */
public class MathUtils {

    /**
     * 中缀表达式转换后缀表达式
     * @param infix 中缀
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-24 16:12
     * @version V1.0
     */
    public static List<String> ExpressionInfix2Suffix(String infix){
        List<String> lexp = parseExpression(infix);
        List<String> suffix = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (String c : lexp) {
            //如果是左括号，直接入栈
            if("(".equals(c)){
                stack.add(c);
                continue;
            }
            //如果是右括号，则不断出栈直到遇到左括号
            if(")".equals(c)){
                String temp;
                while (!stack.empty() && !"(".equals((temp = stack.pop()))){
                    suffix.add(temp);
                }
                continue;
            }
            //处理加减号
            if("+".equals(c) || "-".equals(c)){
                if(stack.empty()){
                    stack.add(c);
                }else {
                    if("(".equals(stack.peek())){
                        stack.add(c);
                    }else {
                        //否则需要不断出栈直到栈顶为左括号或为空
                        while (!stack.empty() && !"(".equals(stack.peek())){
                            suffix.add(stack.pop());
                        }
                        stack.add(c);
                    }
                }
                continue;
            }
            //处理乘除号
            if("*".equals(c) || "/".equals(c)){
                if(stack.empty()){
                    stack.add(c);
                }else {
                    if("+".equals(stack.peek()) || "-".equals(stack.peek()) || "(".equals(stack.peek())){
                        stack.add(c);
                    }else {
                        while (!"+".equals(stack.peek()) || !"-".equals(stack.peek()) || !"(".equals(stack.peek())){
                            suffix.add(stack.pop());
                        }
                        stack.add(c);
                    }
                }
                continue;
            }
            suffix.add(c);
        }
        while (!stack.empty()){
            suffix.add(stack.pop());
        }
        return suffix;
    }

    public static List<String> parseExpression(String exp){
        char[] cexp = exp.toCharArray();
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : cexp){
            if(c == ' '){
                continue;
            }
            if(c == '('){
                res.add(String.valueOf(c));
                continue;
            }
            if(c == '+' || c == '-' || c == '*' || c== '/' || c == ')'){
                if(sb.length() > 0){
                    res.add(sb.toString());
                }
                sb = new StringBuilder();
                res.add(String.valueOf(c));
                continue;
            }else {
                sb.append(c);
            }
        }
        res.add(sb.toString());
        return res;
    }


    public static Float calculate(String exp){
        List<String> lexp = ExpressionInfix2Suffix(exp);
        Stack<String> stack = new Stack<>();
        for (String s : lexp){
            if(!"+".equals(s) && !"-".equals(s) && !"*".equals(s) && !"/".equals(s)){
                stack.add(s);
                continue;
            }
            float a = Float.parseFloat(stack.pop());
            float b = Float.parseFloat(stack.pop());
            if("+".equals(s)){
                stack.add(String.valueOf(a+b));
            }
            if("-".equals(s)){
                stack.add(String.valueOf(b-a));
            }
            if("*".equals(s)){
                stack.add(String.valueOf(b*a));
            }
            if("/".equals(s)){
                stack.add(String.valueOf(b/a));
            }
        }
        return Float.parseFloat(stack.pop());
    }


}
