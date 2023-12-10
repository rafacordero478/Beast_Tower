package com.rafael.beast_tower;

import androidx.annotation.Nullable;

public class User {
    int id;
    String usuario, pass;

    public boolean isNull() {
        if(usuario.equals("") && pass.equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    public User(String usuario, String pass) {
        this.usuario = usuario;
        this.pass = pass;
    }

    public User(){}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", contraseña='" + pass + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
