package test;
import java.io.Serializable;

public class bussiness_card_list implements Serializable {
    private int card_count;
    private int card_capacity;
    private static final int INIT_CARD_CAPACITY=5;

    private bussiness_card[] card_list;

    public bussiness_card get_card(int index){
        if(index<card_count){
            return card_list[index];
        }else{
            return null;
        }
    }

    public bussiness_card_list(){
        card_count=0;
        card_capacity=INIT_CARD_CAPACITY;
        card_list=new bussiness_card[INIT_CARD_CAPACITY];
    }

    public void add_card(String company_name,String person_name,String company_title,String phone_number,String email){
        if(card_count>=card_capacity){
            card_capacity+=5;
            bussiness_card[] temp_list=new bussiness_card[card_capacity];
            for(int i=0;i<card_count;i++){
                temp_list[i]=card_list[i];
            }
            card_list=temp_list;
        }
        card_list[card_count++]=new bussiness_card(company_name,person_name,company_title,phone_number,email);
    }

    public void delete_card(int idx){
        if(idx>=card_count){
            return;
        }

        bussiness_card[] temp_list=new bussiness_card[card_capacity];

        for(int i=0;i<idx;i++){
            temp_list[i]=card_list[i];

        }
        for(int i=idx;i<card_capacity-1;i++){
            temp_list[i]=card_list[i+1];

        }
 
        card_list=temp_list;

        card_count--;


    }
    public void modify_card(int idx,String company_name,String person_name,String company_title,String phone_number,String email){
        card_list[idx].set_company_name(company_name);
        card_list[idx].set_person_name(person_name);
        card_list[idx].set_company_title(company_title);
        card_list[idx].set_phone_number(phone_number);
        card_list[idx].set_email(email);
    }

    public int get_card_capacity(){
        return card_capacity;
    }

    public int get_card_count(){
        return card_count;
    }
}
