package test;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class name_limit extends PlainDocument{
    private int limit;
    public name_limit(int limit){
         super();
         this.limit=limit;
    } 
    public name_limit(int limit,String str){
        super();
        this.limit=limit;
        try{
           super.insertString(0, str, null);
           }catch(BadLocationException e){
               System.out.println("bad loaction execption");
        }
     } 
    public void insertString(int offset,String str,AttributeSet attr) throws BadLocationException{
         String only_alphabet_and_korean="[a-zA-Zㄱ-ㅎ가-힣]";
         boolean isNotSpecial=Pattern.matches(only_alphabet_and_korean,str);
         if(str==null || !isNotSpecial){
             return;
         }
         if(getLength()+str.length()<=limit){
             super.insertString(offset,str,attr);
         }
    }
}
