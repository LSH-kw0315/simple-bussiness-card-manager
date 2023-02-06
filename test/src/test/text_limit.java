package test;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class text_limit extends PlainDocument{
   private int limit;
   public text_limit(int limit){
        super();
        this.limit=limit;
   } 
   public text_limit(int limit,String str){
        super();
        this.limit=limit;
    } 
   public void insertString(int offset,String str,AttributeSet attr) throws BadLocationException{
        if(str==null){
            return;
        }
        if(getLength()+str.length()<=limit){
            super.insertString(offset,str,attr);
        }
   }
}
