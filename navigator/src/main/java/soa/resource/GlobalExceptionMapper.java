package soa.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import soa.dto.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) exception;
            return Response.fromResponse(wae.getResponse())
                    .entity(new ErrorResponse("BadRequest", exception.getMessage()))
                    .build();
        }

        // Handle JSON parsing/mapping errors specifically if possible, 
        // though they often come as WebApplicationException or wrapped in it.
        String message = exception.getMessage();
        if (message == null) {
            message = "An unexpected error occurred";
        }
        
        // Check for common deserialization error indicators in message
        if (message.contains("Can not deserialize") || message.contains("Cannot deserialize") || 
            message.contains("unrecognized field") || message.contains("Unexpected character") ||
            message.contains("Error deserializing object from entity stream")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("BadRequest", "Invalid data format: " + message))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("InternalServerError", message))
                .build();
    }
}
