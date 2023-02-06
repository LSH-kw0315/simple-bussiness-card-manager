package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;


class frame extends JFrame {
    Container content_pane;
    boolean visible;

    bussiness_card_list card_list;

    private static final String FILE_NAME="card_list.ser";
    public frame(){
        setTitle("명함관리 프로그램");
        setSize(300,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                save_card_list_to_file();
                System.exit(0);
            }
        });
        content_pane=getContentPane();
        visible=false;

        read_card_list_from_file();
        main_frame();
    }

    private void update_root_frame(){
        content_pane.repaint();
        if(!visible){
            visible = true;
            setVisible(visible);
        }else{
            content_pane.revalidate();
        }
    }

    private void main_frame(){
        content_pane.removeAll();
        content_pane.setLayout(new BorderLayout());



        JPanel wrapping_all=new JPanel(new GridLayout(3,1,5,5));

        JButton register=new JButton();
        register.setText("명함 등록");
        register.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                register_frame();
            }
        } 
        );


        JButton search=new JButton();
        search.setText("명함 조회");
        search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                search_plain_frame(0);
            }
        });



        JButton close=new JButton();
        close.setText("프로그램 종료");
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                save_card_list_to_file();
                System.exit(0);
            }
        });

        wrapping_all.add(register);
        wrapping_all.add(search);
        wrapping_all.add(close);
        

        content_pane.add(wrapping_all,BorderLayout.CENTER);
        setSize(300,300);
        update_root_frame();
    }
    private void register_frame(){
        content_pane.removeAll();
        content_pane.setLayout(new GridLayout(7,1,0,0));
        


        JPanel zero_row=new JPanel(new FlowLayout());
        JLabel guide=new JLabel("항목 작성");
        zero_row.add(guide);
        content_pane.add(zero_row);

        JPanel first_wrapper=new JPanel(new BorderLayout());
        JPanel first_row=new JPanel(new FlowLayout());
        JLabel company_name_guide=new JLabel("회사명:");
        JTextField company_name=new JTextField(20);
        company_name.setDocument(new name_limit(20));
        first_row.add(company_name_guide);
        first_row.add(company_name);
        first_wrapper.add(first_row,BorderLayout.WEST);
        content_pane.add(first_wrapper);

        JPanel second_wrapper=new JPanel(new BorderLayout());
        JPanel second_row=new JPanel(new FlowLayout());
        JLabel person_name_guide=new JLabel("성명:");
        JTextField person_name=new JTextField(20);
        person_name.setDocument(new name_limit(20));
        second_row.add(person_name_guide);
        second_row.add(person_name);
        second_wrapper.add(second_row,BorderLayout.WEST);
        content_pane.add(second_wrapper);

        JPanel third_wrapper=new JPanel(new BorderLayout());
        JPanel third_row=new JPanel(new FlowLayout());
        JLabel company_title_guide=new JLabel("직함:");
        JTextField company_title=new JTextField(20);
        company_title.setDocument(new name_limit(20));
        third_row.add(company_title_guide);
        third_row.add(company_title);
        third_wrapper.add(third_row,BorderLayout.WEST);
        content_pane.add(third_wrapper);

        JPanel fourth_wrapper=new JPanel(new BorderLayout());
        JPanel fourth_row=new JPanel(new FlowLayout());
        JLabel phone_number_guide=new JLabel("전화번호(-를 빼고 입력):");
        JTextField phone_number=new JTextField(20);
        phone_number.setDocument(new number_limit(11));
        fourth_row.add(phone_number_guide);
        fourth_row.add(phone_number);
        fourth_wrapper.add(fourth_row,BorderLayout.WEST);
        content_pane.add(fourth_wrapper);

        JPanel fifth_wrapper=new JPanel(new BorderLayout());
        JPanel fifth_row=new JPanel(new FlowLayout());
        JLabel email_guide=new JLabel("이메일:");
        JTextField email=new JTextField(20);
        email.setDocument(new text_limit(30));
        fifth_row.add(email_guide);
        fifth_row.add(email);
        fifth_wrapper.add(fifth_row,BorderLayout.WEST);
        content_pane.add(fifth_wrapper);

        JPanel sixth_row=new JPanel(new FlowLayout());
        JButton back_to_main=new JButton();
        back_to_main.setText("뒤로");
        back_to_main.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                main_frame();
            }
        });

        JButton confirm=new JButton();
        confirm.setText("등록하기");
        confirm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                boolean check_success=check_card_content(company_name.getText(),person_name.getText(),company_title.getText(),phone_number.getText(), email.getText());
                if(check_success){
                	String exact_number=phone_number.getText().substring(0, 3)+"-"+phone_number.getText().substring(3, 7)+"-"+phone_number.getText().substring(7, 11);
                    card_list.add_card(company_name.getText(), person_name.getText(), company_title.getText(), exact_number, email.getText());
              
                    main_frame();
                    
                }
            }
        });

        sixth_row.add(back_to_main);
        sixth_row.add(confirm);
        content_pane.add(sixth_row);

        setSize(400,300);
        update_root_frame();
    }
    private void search_plain_frame(int pages){
        content_pane.removeAll();  
        
        content_pane.setLayout(new BorderLayout());

        JPanel search_panel=new JPanel(new FlowLayout());
        String[] kind_of_field={"회사이름","사람이름","직함","전화번호","이메일"};
        JComboBox<String> combo_box=new JComboBox<String>(kind_of_field);
        JTextField search_bar=new JTextField(20);
        JButton search_button=new JButton("검색");
        JButton reset_button=new JButton("초기화");
        
        search_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int idx=combo_box.getSelectedIndex();
                bussiness_card_list target_list=new bussiness_card_list();
                if(search_bar.getText().isEmpty() || search_bar.getText().isBlank()){
                    return;
                }
                switch (idx) {
                    case 0:
                    
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_company_name().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 1:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_person_name().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 2:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_company_title().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 3:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_phone_number().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(), card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 4:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_email().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    default:
                        return;
                }
            }
        });
        search_bar.setDocument(new text_limit(30));
        reset_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                search_plain_frame(0);
            }
        });

        search_panel.add(combo_box);
        search_panel.add(search_bar);
        search_panel.add(search_button);
        search_panel.add(reset_button);
        content_pane.add(search_panel,BorderLayout.NORTH);

        JPanel card_panel=new JPanel(new GridLayout(6,1));

        for(int i=pages*5;i<(pages+1)*5;i++){
            bussiness_card card=card_list.get_card(i);
            JPanel card_wrap=new JPanel(new FlowLayout());
            JLabel label=new JLabel(" ");
            if(card!=null){
                label.setText(card.get_company_name()+"의 "+card.get_person_name()+" "+card.get_company_title()+"의 명함");
                card_wrap.add(label);
                JButton btn=new JButton("명함 열람");
                final int idx=i;
                btn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        view_plain_frame(idx);
                    }
                });
                card_wrap.add(btn);
                card_panel.add(card_wrap);
            }else{
                card_wrap.add(label);
                card_panel.add(card_wrap);
            }
        }
    
        JPanel card_button_panel=new JPanel(new BorderLayout());

        JButton to_next=new JButton("->");
        to_next.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if((pages+1)*5>=card_list.get_card_count()){
                    return;
                }
                search_plain_frame(pages+1);
            }
        });

        JButton to_prev=new JButton("<-");
        to_prev.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if((pages-1)*5<0){
                    return;
                }
                search_plain_frame(pages-1);
            }
        });

        JButton back_to_main=new JButton("메인으로");
        back_to_main.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                main_frame();
            }
        });
        card_button_panel.add(to_prev,BorderLayout.WEST);
        card_button_panel.add(to_next,BorderLayout.EAST);
        card_button_panel.add(back_to_main,BorderLayout.CENTER);
        card_panel.add(card_button_panel);

        content_pane.add(card_panel,BorderLayout.CENTER);

        setSize(500,300);
        update_root_frame();
    }

    private void search_target_frame(bussiness_card_list list,int pages){
        content_pane.removeAll();  
    
        content_pane.setLayout(new BorderLayout());

        JPanel search_panel=new JPanel(new FlowLayout());
        String[] kind_of_field={"회사이름","사람이름","직함","전화번호","이메일"};
        JComboBox<String> combo_box=new JComboBox<String>(kind_of_field);
        JTextField search_bar=new JTextField(20);
        JButton search_button=new JButton("검색");
        JButton reset_button=new JButton("초기화");
        
        search_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int idx=combo_box.getSelectedIndex();
                bussiness_card_list target_list=new bussiness_card_list();
                if(search_bar.getText().isEmpty() || search_bar.getText().isBlank()){
                    return;
                }
                switch (idx) {
                    case 0:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_company_name().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(), card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 1:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_person_name().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 2:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_company_title().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 3:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_phone_number().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(), card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    case 4:
                        for(int i=0;i<card_list.get_card_count();i++){
                            bussiness_card card=card_list.get_card(i);
                            if(card.get_email().contains(search_bar.getText())){
                                target_list.add_card(card.get_company_name(), card.get_person_name(), card.get_company_title(),  card.get_phone_number(), card.get_email());
                            }
                        }
                        search_target_frame(target_list, 0);
                        break;
                    default:
                        return;
                }
            }
        });
        search_bar.setDocument(new text_limit(30));
        reset_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                search_plain_frame(0);
            }
        });

        search_panel.add(combo_box);
        search_panel.add(search_bar);
        search_panel.add(search_button);
        search_panel.add(reset_button);
        content_pane.add(search_panel,BorderLayout.NORTH);

        JPanel card_panel=new JPanel(new GridLayout(6,1));

        for(int i=pages*5;i<(pages+1)*5;i++){
            bussiness_card card=list.get_card(i);
            JPanel card_wrap=new JPanel(new FlowLayout());
            JLabel label=new JLabel(" ");
            if(card!=null){
                label.setText(card.get_company_name()+"의 "+card.get_person_name()+" "+card.get_company_title()+"의 명함");
                card_wrap.add(label);
                JButton view_card=new JButton("명함 열람");
                final int idx=i;
                view_card.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        view_searched_frame(list,idx);
                    }
                });
                card_wrap.add(view_card);
                card_panel.add(card_wrap);
            }else{
                card_wrap.add(label);
                card_panel.add(card_wrap);
            }
        }
    
        JPanel card_button_panel=new JPanel(new BorderLayout());

        JButton to_next=new JButton("->");
        to_next.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if((pages+1)*5>=list.get_card_count()){
                    return;
                }
                search_target_frame(list,pages+1);
            }
        });

        JButton to_prev=new JButton("<-");
        to_prev.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if((pages-1)*5<0){
                    return;
                }
                search_target_frame(list,pages-1);
            }
        });

        JButton back_to_main=new JButton("메인으로");
        back_to_main.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                main_frame();
            }
        });
        card_button_panel.add(to_prev,BorderLayout.WEST);
        card_button_panel.add(to_next,BorderLayout.EAST);
        card_button_panel.add(back_to_main,BorderLayout.CENTER);
        card_panel.add(card_button_panel);

        content_pane.add(card_panel,BorderLayout.CENTER);

        setSize(500,300);
        update_root_frame();
    }

    private void modify_plain_frame(int list_idx){
        content_pane.removeAll();
        content_pane.setLayout(new GridLayout(7,1,0,0));
        


        bussiness_card card=card_list.get_card(list_idx);

        JPanel zero_row=new JPanel(new FlowLayout());
        JLabel guide=new JLabel("항목 수정");
        zero_row.add(guide);
        content_pane.add(zero_row);

        JPanel first_wrapper=new JPanel(new BorderLayout());
        JPanel first_row=new JPanel(new FlowLayout());
        JLabel company_name_guide=new JLabel("회사명:");
        JTextField company_name=new JTextField(20);
        company_name.setDocument(new name_limit(20,card.get_company_name()));
        first_row.add(company_name_guide);
        first_row.add(company_name);
        first_wrapper.add(first_row,BorderLayout.WEST);
        content_pane.add(first_wrapper);

        JPanel second_wrapper=new JPanel(new BorderLayout());
        JPanel second_row=new JPanel(new FlowLayout());
        JLabel person_name_guide=new JLabel("성명:");
        JTextField person_name=new JTextField(20);
        person_name.setDocument(new name_limit(20,card.get_person_name()));
        second_row.add(person_name_guide);
        second_row.add(person_name);
        second_wrapper.add(second_row,BorderLayout.WEST);
        content_pane.add(second_wrapper);

        JPanel third_wrapper=new JPanel(new BorderLayout());
        JPanel third_row=new JPanel(new FlowLayout());
        JLabel company_title_guide=new JLabel("직함:");
        JTextField company_title=new JTextField(20);
        company_title.setDocument(new name_limit(20,card.get_company_title()));
        third_row.add(company_title_guide);
        third_row.add(company_title);
        third_wrapper.add(third_row,BorderLayout.WEST);
        content_pane.add(third_wrapper);

        JPanel fourth_wrapper=new JPanel(new BorderLayout());
        JPanel fourth_row=new JPanel(new FlowLayout());
        JLabel phone_number_guide=new JLabel("전화번호(-를 빼고 입력):");
        JTextField phone_number=new JTextField(20);
        String[] splited_number=card.get_phone_number().split("-");
        String exact_number="";
        for(String number:splited_number){
            exact_number+=number;
        }
        phone_number.setDocument(new number_limit(11,exact_number));
        fourth_row.add(phone_number_guide);
        fourth_row.add(phone_number);
        fourth_wrapper.add(fourth_row,BorderLayout.WEST);
        content_pane.add(fourth_wrapper);

        JPanel fifth_wrapper=new JPanel(new BorderLayout());
        JPanel fifth_row=new JPanel(new FlowLayout());
        JLabel email_guide=new JLabel("이메일:");
        JTextField email=new JTextField(20);
        email.setDocument(new text_limit(30));
        email.setText(card.get_email());
        fifth_row.add(email_guide);
        fifth_row.add(email);
        fifth_wrapper.add(fifth_row,BorderLayout.WEST);
        content_pane.add(fifth_wrapper);

        JPanel sixth_row=new JPanel(new FlowLayout());
        JButton go_back=new JButton();
        go_back.setText("명함 열람으로");
        go_back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
               view_plain_frame(list_idx); 
            }
        });

        JButton go_search=new JButton();
        go_search.setText("리스트로");
        go_search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                search_plain_frame(list_idx/5);
            }
        });

        JButton go_main=new JButton();
        go_main.setText("메인으로");
        go_main.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                main_frame();
            }
        });

        JButton confirm=new JButton();
        confirm.setText("수정하기");
        confirm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                boolean check_success=check_card_content(company_name.getText(),person_name.getText(),company_title.getText(),phone_number.getText(), email.getText());
                if(check_success){
                	String exact_number=phone_number.getText().substring(0, 3)+"-"+phone_number.getText().substring(3, 7)+"-"+phone_number.getText().substring(7, 11);
                    card_list.modify_card(list_idx,company_name.getText(), person_name.getText(), company_title.getText(),exact_number, email.getText());
                    view_plain_frame(list_idx);
                }
            }
        });

        sixth_row.add(go_back);
        sixth_row.add(go_search);
        sixth_row.add(go_main);
        sixth_row.add(confirm);
        content_pane.add(sixth_row);

        setSize(400,300);
        update_root_frame();
    }

    private void modify_searched_frame(bussiness_card_list list,int list_idx){
        content_pane.removeAll();
        content_pane.setLayout(new GridLayout(7,1,0,0));
        


        bussiness_card card=list.get_card(list_idx);

        JPanel zero_row=new JPanel(new FlowLayout());
        JLabel guide=new JLabel("항목 수정");
        zero_row.add(guide);
        content_pane.add(zero_row);

        JPanel first_wrapper=new JPanel(new BorderLayout());
        JPanel first_row=new JPanel(new FlowLayout());
        JLabel company_name_guide=new JLabel("회사명:");
        JTextField company_name=new JTextField(20);
        company_name.setDocument(new name_limit(20,card.get_company_name()));
        first_row.add(company_name_guide);
        first_row.add(company_name);
        first_wrapper.add(first_row,BorderLayout.WEST);
        content_pane.add(first_wrapper);

        JPanel second_wrapper=new JPanel(new BorderLayout());
        JPanel second_row=new JPanel(new FlowLayout());
        JLabel person_name_guide=new JLabel("성명:");
        JTextField person_name=new JTextField(20);
        person_name.setDocument(new name_limit(20,card.get_person_name()));
        second_row.add(person_name_guide);
        second_row.add(person_name);
        second_wrapper.add(second_row,BorderLayout.WEST);
        content_pane.add(second_wrapper);

        JPanel third_wrapper=new JPanel(new BorderLayout());
        JPanel third_row=new JPanel(new FlowLayout());
        JLabel company_title_guide=new JLabel("직함:");
        JTextField company_title=new JTextField(20);
        company_title.setDocument(new name_limit(20,card.get_company_title()));
        third_row.add(company_title_guide);
        third_row.add(company_title);
        third_wrapper.add(third_row,BorderLayout.WEST);
        content_pane.add(third_wrapper);

        JPanel fourth_wrapper=new JPanel(new BorderLayout());
        JPanel fourth_row=new JPanel(new FlowLayout());
        JLabel phone_number_guide=new JLabel("전화번호(-를 빼고 입력):");
        JTextField phone_number=new JTextField(20);
        String[] splited_number=card.get_phone_number().split("-");
        String exact_number="";
        for(String number:splited_number){
            exact_number+=number;
        }
        phone_number.setDocument(new number_limit(11,exact_number));
        fourth_row.add(phone_number_guide);
        fourth_row.add(phone_number);
        fourth_wrapper.add(fourth_row,BorderLayout.WEST);
        content_pane.add(fourth_wrapper);

        JPanel fifth_wrapper=new JPanel(new BorderLayout());
        JPanel fifth_row=new JPanel(new FlowLayout());
        JLabel email_guide=new JLabel("이메일:");
        JTextField email=new JTextField(20);
        email.setDocument(new text_limit(30));
        email.setText(card.get_email());
        fifth_row.add(email_guide);
        fifth_row.add(email);
        fifth_wrapper.add(fifth_row,BorderLayout.WEST);
        content_pane.add(fifth_wrapper);

        JPanel sixth_row=new JPanel(new FlowLayout());
        JButton go_back=new JButton();
        go_back.setText("명함 열람으로");
        go_back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
               view_searched_frame(list,list_idx); 
            }
        });

        JButton go_search=new JButton();
        go_search.setText("리스트로");
        go_search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                search_target_frame(list,list_idx/5);
            }
        });

        JButton go_main=new JButton();
        go_main.setText("메인으로");
        go_main.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                main_frame();
            }
        });

        JButton confirm=new JButton();
        confirm.setText("수정하기");
        confirm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                boolean check_success=check_card_content(company_name.getText(),person_name.getText(),company_title.getText(),phone_number.getText(), email.getText());
                if(check_success){
                	bussiness_card target=list.get_card(list_idx);
                    String target_company_name=target.get_company_name();
                    String target_person_name=target.get_person_name();
                    String target_company_title=target.get_company_title();
                    String target_phone_number=target.get_phone_number();
                    String target_email=target.get_email();
                    
                    String exact_number=phone_number.getText().substring(0, 3)+"-"+phone_number.getText().substring(3, 7)+"-"+phone_number.getText().substring(7, 11);
                    list.modify_card(list_idx,company_name.getText(), person_name.getText(), company_title.getText(), exact_number, email.getText());
                    
                    for(int i=0;i<card_list.get_card_count();i++){
                        bussiness_card card=card_list.get_card(i);
                        if(card.get_company_name().equals(target_company_name) && card.get_person_name().equals(target_person_name)&&card.get_company_title().equals(target_company_title)&&card.get_phone_number().equals(target_phone_number)&&card.get_email().equals(target_email)){
                        	
                        	card_list.modify_card(i,company_name.getText(), person_name.getText(), company_title.getText(), exact_number, email.getText());
                            break;
                        }
                    }
                    view_searched_frame(list,list_idx);
                }
            }
        });

        sixth_row.add(go_back);
        sixth_row.add(go_search);
        sixth_row.add(go_main);
        sixth_row.add(confirm);
        content_pane.add(sixth_row);

        setSize(400,300);
        update_root_frame();
    }

    private void view_plain_frame(int list_idx){
        content_pane.removeAll();  
    
        content_pane.setLayout(new GridLayout(7,1));
        
        bussiness_card target=card_list.get_card(list_idx);
        
        JPanel title_panel=new JPanel(new FlowLayout());
        JLabel title=new JLabel(target.get_company_name()+"의 "+target.get_person_name()+" "+target.get_company_title()+"의 명함");
        title_panel.add(title);

        JPanel first_row_panel=new JPanel(new FlowLayout());
        JLabel company_name=new JLabel("회사명:"+target.get_company_name());
        first_row_panel.add(company_name);

        JPanel second_row_panel=new JPanel(new FlowLayout());
        JLabel person_name=new JLabel("성명:"+target.get_person_name());
        second_row_panel.add(person_name);

        JPanel third_row_panel=new JPanel(new FlowLayout());
        JLabel company_title=new JLabel("직함:"+target.get_company_title());
        third_row_panel.add(company_title);

        JPanel fourth_row_panel=new JPanel(new FlowLayout());
        JLabel phone_number=new JLabel("전화번호:"+target.get_phone_number());
        fourth_row_panel.add(phone_number);

        JPanel fifth_row_panel=new JPanel(new FlowLayout());
        JLabel email=new JLabel("이메일:"+target.get_email());
        fifth_row_panel.add(email);

        JPanel button_panel=new JPanel(new FlowLayout());

        JButton go_back=new JButton("돌아가기");
        go_back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                search_plain_frame(list_idx/5);
            }
        });


        JButton go_modify=new JButton("수정하기");
        go_modify.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                modify_plain_frame(list_idx);
            }
        });

        JButton delete=new JButton("삭제하기");
        delete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(content_pane, "명함이 삭제되었습니다.", "명함 삭제", JOptionPane.INFORMATION_MESSAGE);
                card_list.delete_card(list_idx);
                search_plain_frame(list_idx/5);
            }
        });
        button_panel.add(go_back);
        button_panel.add(go_modify);
        button_panel.add(delete);

        content_pane.add(title_panel);
        content_pane.add(first_row_panel);
        content_pane.add(second_row_panel);
        content_pane.add(third_row_panel);
        content_pane.add(fourth_row_panel);
        content_pane.add(fifth_row_panel);
        content_pane.add(button_panel);

        setSize(400,300);
        update_root_frame();
    }

    private void view_searched_frame(bussiness_card_list list,int list_idx){
        content_pane.removeAll();  
     
        content_pane.setLayout(new GridLayout(7,1));
        
        bussiness_card target=list.get_card(list_idx);
        
        JPanel title_panel=new JPanel(new FlowLayout());
        JLabel title=new JLabel(target.get_company_name()+"의 "+target.get_person_name()+" "+target.get_company_title()+"의 명함");
        title_panel.add(title);

        JPanel first_row_panel=new JPanel(new FlowLayout());
        JLabel company_name=new JLabel("회사명:"+target.get_company_name());
        first_row_panel.add(company_name);

        JPanel second_row_panel=new JPanel(new FlowLayout());
        JLabel person_name=new JLabel("성명:"+target.get_person_name());
        second_row_panel.add(person_name);

        JPanel third_row_panel=new JPanel(new FlowLayout());
        JLabel company_title=new JLabel("직함:"+target.get_company_title());
        third_row_panel.add(company_title);

        JPanel fourth_row_panel=new JPanel(new FlowLayout());
        JLabel phone_number=new JLabel("전화번호:"+target.get_phone_number());
        fourth_row_panel.add(phone_number);

        JPanel fifth_row_panel=new JPanel(new FlowLayout());
        JLabel email=new JLabel("이메일:"+target.get_email());
        fifth_row_panel.add(email);

        JPanel button_panel=new JPanel(new FlowLayout());
        JButton go_back=new JButton("돌아가기");
        go_back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                search_target_frame(list, list_idx/5);
            }
        });

        JButton go_modify=new JButton("수정하기");
        go_modify.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                modify_searched_frame(list,list_idx);
            }
        });

        JButton delete=new JButton("삭제하기");
        delete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(content_pane, "명함이 삭제되었습니다.", "명함 삭제", JOptionPane.INFORMATION_MESSAGE );
                String target_company_name=target.get_company_name();
                String target_person_name=target.get_person_name();
                String target_company_title=target.get_company_title();
                String target_phone_number=target.get_phone_number();
                String target_email=target.get_email();
                
                for(int i=0;i<card_list.get_card_count();i++){
                    bussiness_card card=card_list.get_card(i);
                    if(card.get_company_name().equals(target_company_name) && card.get_person_name().equals(target_person_name)&&card.get_company_title().equals(target_company_title)&&card.get_phone_number().equals(target_phone_number)&&card.get_email().equals(target_email)){
                        card_list.delete_card(i);
                        break;
                    }
                }
                list.delete_card(list_idx);
                search_target_frame(list,list_idx/5);
            }
        });
        button_panel.add(go_back);
        button_panel.add(go_modify);
        button_panel.add(delete);

        content_pane.add(title_panel);
        content_pane.add(first_row_panel);
        content_pane.add(second_row_panel);
        content_pane.add(third_row_panel);
        content_pane.add(fourth_row_panel);
        content_pane.add(fifth_row_panel);
        content_pane.add(button_panel);

        setSize(400,300);
        update_root_frame();
    }

    private void read_card_list_from_file(){
        try(FileInputStream fis =new FileInputStream(FILE_NAME);ObjectInputStream ois = new ObjectInputStream(fis)){
            card_list=(bussiness_card_list)ois.readObject();
        }catch(IOException io_execption){
            card_list=new bussiness_card_list();
        }catch(ClassNotFoundException class_not_found_exception){
            card_list=new bussiness_card_list();
        }
    }

    private void save_card_list_to_file(){
        try(FileOutputStream fos=new FileOutputStream(FILE_NAME);ObjectOutputStream oos=new ObjectOutputStream(fos)){
            oos.writeObject(card_list);
        }catch(IOException io_Exception){
            System.out.println("I/O exception");
        }
    }

    private boolean check_card_content(String company_name,String person_name,String company_title,String phone_number,String email){
        if(company_name.length()<=0){
            JOptionPane.showMessageDialog(content_pane, "회사 이름은 적어도 한 글자 이상이어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(person_name.length()<=1){
            JOptionPane.showMessageDialog(content_pane, "성명은 적어도 두 글자 이상이어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(company_title.length()<=1){
            JOptionPane.showMessageDialog(content_pane, "직함은 적어도 두 글자 이상이어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(phone_number.length()<11){
            JOptionPane.showMessageDialog(content_pane, "전화번호 형식이 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!email.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?")){
            JOptionPane.showMessageDialog(content_pane, "이메일 형식이 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(content_pane, "등록/수정에 성공하셨습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}