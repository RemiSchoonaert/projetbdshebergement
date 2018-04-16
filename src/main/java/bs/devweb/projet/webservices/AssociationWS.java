package bs.devweb.projet.webservices;

import bs.devweb.projet.entities.Association;
import bs.devweb.projet.entities.Materiel;
import bs.devweb.projet.services.AssociationService;
import bs.devweb.projet.services.MaterielService;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe sert de WebService pour les associations
 */
@Path("association")
public class AssociationWS {

    private AssociationService associationService = AssociationService.getInstance();
    private MaterielService materielService = MaterielService.getInstance();
    private Gson gsonService = new Gson();

    /**
     * Cette methode retourne la liste du materiel pour une association donnee
     * @param idAssociation l'id de l'association dont on souhaite connaitre le materiel possede
     * @return la liste du materiel possede par l'association
     */
    @GET
    @Path("/{id}")
    public Response getListMateriel(@PathParam("id") Integer idAssociation){
        Association association = associationService.getAssociationById(idAssociation);
        List<Materiel> listMateriel = materielService.listMaterielKnowingAssociation(association);
        HashMap<Integer, String> designationMateriel = new HashMap<>();
        for (Materiel materiel : listMateriel) {
            designationMateriel.put(materiel.getId(), materiel.getDesignation());
        }
        return Response.ok().entity(gsonService.toJson(designationMateriel)).build();
    }
}
