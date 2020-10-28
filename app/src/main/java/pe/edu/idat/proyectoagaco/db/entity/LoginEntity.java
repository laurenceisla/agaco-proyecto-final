package pe.edu.idat.proyectoagaco.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class LoginEntity {
    @PrimaryKey
    public int id;
    public String email;
    public String token;
    public String perfil;

    public LoginEntity(int id, String email, String token, String perfil) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
