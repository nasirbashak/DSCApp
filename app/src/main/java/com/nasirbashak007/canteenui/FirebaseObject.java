package com.nasirbashak007.canteenui;

import java.util.HashMap;

public class FirebaseObject{
    String name;
    String usn;
    String phone;
    String email;
    String amount;
    HashMap<String, String> transactions;

    //The following methods are all required by Firebase
    FirebaseObject(){    }
    FirebaseObject(String n,String u,String p,String e){
        name = n;
        usn = u;
        phone = p;
        email = e;
        amount = "0";
        transactions = new HashMap<String, String>();
        transactions.put("Created",name);
    }
    FirebaseObject(String n,String u,String p,String e, String a){
        name = n;
        usn = u;
        phone = p;
        email = e;
        amount = a;
        transactions = new HashMap<String, String>();
        transactions.put("Created",name);
    }
    FirebaseObject(HashMap h){
        name = (String)h.get("name");
        email = (String)h.get("email");
        phone = (String)h.get("phone");
        usn = (String)h.get("usn");
        amount = (String)h.get("amount");
        if(amount.equals("null"))
            amount = "0";
        transactions = (HashMap) h.get("transactions");
    }

    public String getName() {
        return name;
    }

    public String getUsn() {
        return usn;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public HashMap<String, String> getTransactions() {
        return transactions;
    }

    public void setTransactions(HashMap t) {
        this.transactions = t;
    }

    public void setName(String n) {
        name = n;
    }

    public void setUsn(String u) {
        usn = u;
    }

    public void setPhone(String p) {
        phone = p;
    }

    public void setEmail(String m) {
        email = m;
    }

}
