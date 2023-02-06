
package test;
import java.io.Serializable;

public class bussiness_card implements Serializable {

    private String company_name;
    private String person_name;
    private String company_title;
    private String phone_number;
    private String email;

    public bussiness_card(){
        set_company_name("");
        set_person_name("");
        set_company_title("");
        set_phone_number("");
        set_email("");
    }

    public bussiness_card(String company_name,String person_name,String company_title,String phone_number,String email){
        set_company_name(company_name);
        set_person_name(person_name);
        set_company_title(company_title);
        set_phone_number(phone_number);
        set_email(email);
    }

    public void set_company_name(String str){
        company_name=str;
    }

    public void set_person_name(String str){
        person_name=str;
    }

    public void set_company_title(String str){
        company_title=str;
    }

    public void set_phone_number(String str){
        phone_number=str;
    }  

    public void set_email(String str){
        email=str;
    }

    public String get_company_name(){
        return company_name;
    }

    public String get_person_name(){
        return person_name;
    }

    public String get_company_title(){
        return company_title;
    }

    public String get_phone_number(){
        return phone_number;
    }  

    public String get_email(){
        return email;
    }

    public String toString(){
        return "company name:"+company_name+"\n"+"person name:"+person_name+"\n"+"company title:"+company_title+"\n"+"phone number"+phone_number+"\n"+"email:"+email;
    }
}
