package bs.devweb.projet.provider;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * Cette classe permet de se connecter a la BDD et est utilisable dans toutes les autres classes
 * Il n'y a besoin d'indiquer les parametres de connexion que dans cette classe
 */
public class DataSourceProvider {

    private static PGSimpleDataSource dataSource;

    public static PGSimpleDataSource getDataSource() {
        if (dataSource == null) {
        	dataSource = new PGSimpleDataSource();
            dataSource.setURL("");
            dataSource.setDatabaseName("");
            dataSource.setUser("");
            dataSource.setPortNumber();
            dataSource.setPassword("");
        }
        return dataSource;
    }
}

