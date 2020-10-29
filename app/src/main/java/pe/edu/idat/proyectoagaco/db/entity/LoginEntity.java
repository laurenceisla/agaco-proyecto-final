package pe.edu.idat.proyectoagaco.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class LoginEntity {
    @PrimaryKey
    public int id;
    public String nombre;
    public String token;
    public String perfil;

    public LoginEntity(int id, String nombre, String token, String perfil) {
        this.id = id;
        this.nombre = nombre;
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
        return nombre;
    }

    public void setEmail(String nombre) {
        this.nombre = nombre;
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
