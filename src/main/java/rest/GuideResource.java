package rest;

import dtos.GuideDTO;
import dtos.PersonDTO;
import dtos.TripDTO;
import entities.Guide;
import entities.Person;
import entities.Trip;
import entities.User;
import facades.GuideFacade;
import facades.PersonFacade;
import facades.TripFacade;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("guides")
public class GuideResource extends Resource {

    private final GuideFacade facade = GuideFacade.getFacade(EMF);

    @GET
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllGuides() {
        List<GuideDTO> allGuides = facade.getAllGuides();
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(allGuides)).build();
    }

    @POST
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createGuide(String guideAsJson) {
        GuideDTO guideDTO = GSON.fromJson(guideAsJson, GuideDTO.class);
        Guide guide = new Guide(guideDTO.getGender(), guideDTO.getBirthYear(), guideDTO.getProfile(), guideDTO.getImage());

        GuideDTO newGuide = new GuideDTO(facade.createGuide(guide));
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(newGuide)).build();
    }

}
