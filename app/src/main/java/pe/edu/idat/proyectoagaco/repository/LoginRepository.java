package pe.edu.idat.proyectoagaco.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import pe.edu.idat.proyectoagaco.db.AgacoRoomDatabase;
import pe.edu.idat.proyectoagaco.db.dao.LoginDao;
import pe.edu.idat.proyectoagaco.db.entity.LoginEntity;

public class LoginRepository {

    private LoginDao loginDao;

    public LoginRepository (Application application) {
        AgacoRoomDatabase db = AgacoRoomDatabase.getDataBase(application);
        loginDao = db.loginDao();
    }

    public LiveData<LoginEntity> obtenerLogin() {
        return loginDao.obtenerLogin();
    }

    public void insertar(LoginEntity login) {
        new InsertarAsyncTask(loginDao).execute(login);
    }

    private static class InsertarAsyncTask
            extends AsyncTask<LoginEntity, Void, Void> {

        private LoginDao loginDaoAsyncTask;

        InsertarAsyncTask(LoginDao dao) {
            loginDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(LoginEntity... loginEntities) {
            loginDaoAsyncTask.insertar(loginEntities[0]);
            return null;
        }
    }

    public void eliminar(LoginEntity login) {
        new EliminarAsyncTask(loginDao).execute();
    }

    private static class EliminarAsyncTask
            extends AsyncTask<Void, Void, Void> {

        private LoginDao loginDaoAsyncTask;

        EliminarAsyncTask(LoginDao dao) {
            loginDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            loginDaoAsyncTask.eliminar();
            return null;
        }
    }

}
