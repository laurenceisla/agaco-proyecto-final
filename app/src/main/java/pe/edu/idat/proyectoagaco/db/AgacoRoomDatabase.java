package pe.edu.idat.proyectoagaco.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pe.edu.idat.proyectoagaco.db.dao.LoginDao;
import pe.edu.idat.proyectoagaco.db.entity.LoginEntity;

@Database(entities = {LoginEntity.class}, version = 1)
public abstract class AgacoRoomDatabase extends RoomDatabase {

    public abstract LoginDao loginDao();

    private static volatile AgacoRoomDatabase instancia;

    public static AgacoRoomDatabase getDataBase(final Context context) {
        if (instancia == null) {
            synchronized (AgacoRoomDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AgacoRoomDatabase.class,
                            "AgacoDatabase"
                    ).build();
                }
            }
        }

        return instancia;
    }

}
