package test;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class number_limit extends PlainDocument{
   private int limit;
   public number_limit(int limit){
        super();
        this.limit=limit;
   } 
   public number_limit(int limit,String str){
        super();
        this.limit=limit;
        try{
            super.insertString(0, str, null);
        }catch(BadLocationException e){
            System.out.println("bad location exception");
        }
   }
   public void insertString(int offset,String str,AttributeSet attr) throws BadLocationException{
        int str_to_int;
        try{
            str_to_int=Integer.valueOf(str);
        }catch(NumberFormatException e){
            str_to_int=-1;
        }
        if(str==null || str_to_int<0){
            return;
        }
        if(getLength()+str.length()<=limit){
            super.insertString(offset,str,attr);
        }
   }
}