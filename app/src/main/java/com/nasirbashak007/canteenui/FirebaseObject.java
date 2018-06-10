package com.nasirbashak007.canteenui;

public class FirebaseObject {
    String name;
    String usn;
    String phone;
    String email;

    //The following methods are all required by Firebase
    FirebaseObject(){    }
    FirebaseObject(String n,String u,String p,String e){
        name = n;
        usn = u;
        phone = p;
        email = e;
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
