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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.ntr1x.storage.security.filters.IUserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Me")
@Component
@Path("/me/media/publications")
@PermitAll
public class PublicationMe {

    @Inject
    private IPublicationService publications;

    @Inject
    private Provider<IUserPrincipal> principal;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public PublicationPageResponse query(
        @QueryParam("relate") Long relate,
        @QueryParam("since") @ApiParam(example = "2016-10-01T17:30") LocalDateTime since,
        @QueryParam("until") @ApiParam(example = "2016-10-01T21:00") LocalDateTime until,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Publication> p = publications.query(
            scope.get().getId(),
            principal.get().getUser().getId(),
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
    @RolesAllowed({ "auth" })
    public Publication create(@Valid PublicationCreate create) {

        create.user = principal.get().getUser().getId();
        
        return publications.create(scope.get().getId(), create);
    }
}
