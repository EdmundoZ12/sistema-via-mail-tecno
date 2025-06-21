package com.tecnoweb.grupo7sa;

import com.tecnoweb.grupo7sa.ConfigDB.ConfigDB;
import com.tecnoweb.grupo7sa.ConfigDB.DatabaseConection;
import com.tecnoweb.grupo7sa.data.DUsuario;
import com.tecnoweb.grupo7sa.business.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TecnowebGrupo7saApplication {

    public static void main(String[] args) {


        SpringApplication.run(TecnowebGrupo7saApplication.class, args);
//        ConfigDB configDB = new ConfigDB();
//        DatabaseConection conection = new DatabaseConection(configDB.getUser(), configDB.getPassword(), configDB.getHost(), configDB.getPort(), configDB.getDbName());
//        conection.openConnection();
//        conection.closeConnection();
        DUsuario du = new DUsuario();
        BUsuario bUsuario = new BUsuario();
        bUsuario.findAllUsers();
        bUsuario.findOneById(0);
//        du.update(3,"JUANA", "PEREZ", "juanito@gmail.com", "221", "9874521", "123", "RESPONSABLE");
//        du.delete(3);
//        du.save("JOAQUIN", "PEREZ", "joaquin@gmail.com", "2210", "98745211", "8745210", "123","RESPONSABLE");
//        du.findAllUsers();
//        du.findOneById(1);
//        du.disconnect();

    }

}
