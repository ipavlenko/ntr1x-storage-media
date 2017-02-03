package com.ntr1x.storage.media.resources;

import java.time.LocalDateTime;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.media.model.Publication;
import com.ntr1x.storage.media.services.IPublicationService;
import com.ntr1x.storage.media.services.IPublicationService.PublicationCreate;
import com.ntr1x.storage.media.services.IPublicationService.PublicationPageResponse;
import com.ntr1x.storage.media.services.IPublicationService.PublicationUpdate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Media")
@Component
@Path("/media/publications")
@PermitAll
public class PublicationResource {

	@Inject
	private IPublicationService publications;
	
	@Inject
	private Provider<IUserScope> scope;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PublicationPageResponse shared(
		@QueryParam("user") Long user,
		@QueryParam("relate") Long relate,
		@QueryParam("since") @ApiParam(example = "2016-10-01T17:30") LocalDateTime since,
		@QueryParam("until") @ApiParam(example = "2016-10-01T21:00") LocalDateTime until,
		@BeanParam PageableQuery pageable
    ) {
    	
        Page<Publication> p = publications.query(
    		scope.get().getId(),
			user,
			relate,
			since,
			until,
			pageable.toPageRequest()
		);
        
        return new PublicationPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///publications:admin" })
    public Publication create(@Valid PublicationCreate create) {

        return publications.create(scope.get().getId(), create);
	}
	
	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///publications/i/{id}:admin" })
	public Publication update(@PathParam("id") long id, @Valid PublicationUpdate update) {
	    
	    return publications.update(scope.get().getId(), id, update);
	}
	
	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///publications/i/{id}:admin" })
    public Publication select(@PathParam("id") long id) {
        
        return publications.select(scope.get().getId(), id);
    }
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///publications/i/{id}:admin" })
    public Publication remove(@PathParam("id") long id) {
        
	    return publications.remove(scope.get().getId(), id);
    }
}
