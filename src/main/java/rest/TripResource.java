package rest;

import dtos.PersonDTO;
import dtos.TripDTO;
import entities.Person;
import entities.Trip;
import entities.User;
import facades.TripFacade;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("trips")
public class TripResource extends Resource {

    private final TripFacade facade = TripFacade.getFacade(EMF);

    @GET
    @RolesAllowed({"user", "admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTrips() {
        List<TripDTO> allTrips = facade.getAllTrips();
        String allTripsToJson = GSON.toJson(allTrips);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(allTripsToJson).build();
    }

    @POST
    @Path("{id}/person")
    @RolesAllowed("user")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addPersonToTrip(@PathParam("id") int id, String personFromJson) {
        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());

        Trip trip = facade.getTripById(id);

        PersonDTO personDTO = GSON.fromJson(personFromJson, PersonDTO.class);

        User user = new User(userId);
        Person person = new Person(personDTO.getAddress(), personDTO.getEmail(), personDTO.getBirthYear(), personDTO.getGender(), user);

        TripDTO tripDTO = facade.createAndAddPersonToTrip(person, trip);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(tripDTO)).build();
    }

}
