package com.example;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/api/books")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService service;

    @Inject
    UriInfo uriInfo;

    @GET
    @Operation(
        summary = "Returns all the books from the database")
    @APIResponse(
        responseCode = "200",
        description = "A list of books",
        content = @Content(mediaType = APPLICATION_JSON,
        schema = @Schema(implementation = Book.class, type = SchemaType.ARRAY)))
    @APIResponse(
        responseCode = "204",
        description = "No books"
    )
    public Response getBooks(){
        List<Book> books = service.findAll(); // Get books from the database

        if(books.isEmpty())
            return Response.noContent().build();

        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Returns a book for a given identifier if it exists")
    public Response getBook(@PathParam("id") @Min(1) Long id){
        Book book = service.find(id); // Get the book from the database
        if(book == null)
            return Response.noContent().build();

        return Response.ok(book).build();
    }

    @GET
    @Produces(TEXT_PLAIN)
    @Path("/count")
    @Operation(
        summary = "Returns the number of books from the database as a plain text")
    @APIResponse(
            responseCode = "200",
            description = "A book",
            content = @Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Book.class)))
    @APIResponse(
            responseCode = "404",
            description = "No book"
    )
    public Response countBooks(){
        Long nbOfBooks = service.countAll();
        if(nbOfBooks == 0)
            return Response.noContent().build();

        return Response.ok(nbOfBooks).build();
    }

    @POST
    @Operation(
        summary = "Creates a valid book")
    public Response createBook(Book book) throws URISyntaxException {
        book = service.create(book); // Persist the book into the database
        URI createdURI = uriInfo.getAbsolutePathBuilder().path(book.getId().toString()).build();
        return Response.created(createdURI).entity(book).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Deletes an existing book based on the provided identifier")
    public Response deleteBook(@PathParam("id") @Min(1) Long id){
        service.delete(id);// Delete the book from the database
        return Response.noContent().build();
    }
}
