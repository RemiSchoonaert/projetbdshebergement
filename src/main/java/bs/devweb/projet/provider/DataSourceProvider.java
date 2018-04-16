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
            dataSource.setURL("jdbc:postgresql://ec2-79-125-117-53.eu-west-1.compute.amazonaws.com:5432/d98khftjj2v0co?user=axnsoltojbwnvm&password=32341629078836716800d2f8a15f500c65554b3dc66cee3cb8143abb50b4dbe5&sslmode=require");
            dataSource.setDatabaseName("d98khftjj2v0co");
            dataSource.setUser("axnsoltojbwnvm");
            dataSource.setPortNumber(5432);
            dataSource.setPassword("32341629078836716800d2f8a15f500c65554b3dc66cee3cb8143abb50b4dbe5");
        }
        return dataSource;
    }
}

