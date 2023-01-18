package rest;

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

@Path("trips")
public class TripResource extends Resource {

    private final TripFacade facade = TripFacade.getFacade(EMF);
    private final PersonFacade personFacade = PersonFacade.getFacade(EMF);

    private final GuideFacade guideFacade = GuideFacade.getFacade(EMF);

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

    @DELETE
    @Path("{id}/person/{personId}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removePersonFromTrip(@PathParam("id") int id, @PathParam("personId") int personId) {
        Trip trip = facade.getTripById(id);
        Person person = personFacade.getPersonById(personId);

        TripDTO tripDTO;

        if(trip.getPeople().contains(person)) {
            tripDTO = facade.removePersonFromTrip(trip, person);
        } else {
            throw new NotFoundException("");
        }
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(tripDTO)).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeTripById(@PathParam("id") int id) {
        List<TripDTO> updatedTripsList = facade.removeTripById(id);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(updatedTripsList)).build();
    }

    @POST
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createTrip(String tripAsJson) {
        TripDTO tripDTO = GSON.fromJson(tripAsJson, TripDTO.class);

        Guide guide = guideFacade.getGuideById(tripDTO.getGuide().getId());
        Trip trip = new Trip(tripDTO.getDate(), tripDTO.getTime(), tripDTO.getLocation(), tripDTO.getDuration(), tripDTO.getPackingList(), guide);

        TripDTO createdTrip = facade.createTrip(trip);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(createdTrip)).build();

    }

}
