package bs.devweb.projet.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Cette classe permet de hasher les mots de passe
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public class MotDePasseUtils {

    // on indique les parametres du hash => valeurs conseillees par les utilisateurs de cette API
    public static final int LONGUEUR_SEL = 16;
    public static final int LONGUEUR_HASH = 32;

    public static final int NOMBRE_ITERATION = 2;
    public static final int MEMOIRE = 65536;
    public static final int PARALLELISME = 1;

    /**
     * Cette methode permet de parametrer le hash
     * @return une instance Argon2 pour laquelle les parametres du hash ont ete indiques
     */
    private static Argon2 instancierArgon2() {
        return Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i, LONGUEUR_SEL, LONGUEUR_HASH);
    }

    /**
     * Cette methode permet de hasher une chaine de caracteres
     * @param motDePasse la chaine de caracteres a hasher
     * @return la chaine de caracteres hashee
     */
    public static String genererMotDePasse(String motDePasse) {
        return instancierArgon2().hash(NOMBRE_ITERATION, MEMOIRE, PARALLELISME, motDePasse);
    }

    /**
     * Cette methode permet de verifier si une chaine de caracteres correspond a un hash
     * @param motDePasse la chaine de caracteres que l'on veut tester
     * @param hashCorrect la hash par rapport auquel on teste la chaine de caracteres
     * @return true si il y une correspondance et false sinon
     */
    public static boolean validerMotDePasse(String motDePasse, String hashCorrect) {
        return instancierArgon2().verify(hashCorrect, motDePasse);
    }

}
