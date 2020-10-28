package pe.edu.idat.proyectoagaco.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import pe.edu.idat.proyectoagaco.db.entity.LoginEntity;

@Dao
public interface LoginDao {

    @Insert
    void insertar(LoginEntity login);

    @Update
    void actualizar(LoginEntity login);

    @Query("delete from login where id = :id")
    void eliminar(int id);

    @Query("select * from login where id = :id")
    LiveData<LoginEntity> obtenerLogin(int id);
}
