package com.ntr1x.storage.media.services;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.core.model.Image;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IResourceService;
import com.ntr1x.storage.media.model.Publication;
import com.ntr1x.storage.media.repository.PublicationRepository;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;
import com.ntr1x.storage.security.services.IUserService;
import com.ntr1x.storage.uploads.services.IImageService;

@Service
public class PublicationService implements IPublicationService {

	@Inject
	private EntityManager em;
	
	@Inject
	private PublicationRepository publications;
	
	@Inject
	private IResourceService resources;
	
	@Inject
	private IUserService users;
	
	@Inject
	private ISecurityService security;
	
	@Inject
	private IImageService images;
	
	@Override
	public Publication create(long scope, PublicationCreate create) {
		
		Publication p = new Publication(); {
			
			User user = users.select(scope, create.user);
			Resource relate = create.relate == null ? null : resources.select(scope, create.relate);
			Image thumbnail = create.thumbnail == null ? null : images.select(scope, create.thumbnail);
			
			p.setScope(scope);
			p.setUser(user);
			p.setRelate(relate);
			p.setTitle(create.title);
			p.setSubtitle(create.subtitle);
			p.setPromo(create.promo);
			p.setBody(create.body);
			p.setPublished(create.published);
			p.setThumbnail(thumbnail);
			
			em.persist(p);
			em.flush();
			
			security.register(p, ResourceUtils.alias(null, "publications/i", p));
			security.grant(p.getScope(), user, p.getAlias(), "admin");
		}
		
		return p;
	}

	@Override
	public Publication update(Long scope, long id, PublicationUpdate update) {
		
		Publication p = publications.select(scope, id); {
			
			Image thumbnail = update.thumbnail == null ? null : images.select(scope, update.thumbnail);
			
			p.setTitle(update.title);
			p.setSubtitle(update.subtitle);
			p.setPromo(update.promo);
			p.setBody(update.body);
			p.setPublished(update.published);
			p.setThumbnail(thumbnail);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}

	@Override
	public Publication select(Long scope, long id) {
		
		return publications.select(scope, id);
	}

	@Override
	public Page<Publication> query(Long scope, Long user, Long relate, LocalDateTime since, LocalDateTime until, Pageable pageable) {
		
		return publications.query(scope, user, relate, since, until, pageable);
	}

	@Override
	public Publication remove(Long scope, long id) {
		
		Publication p = publications.select(scope, id); {
			
			em.remove(p);
			em.flush();
		}
		
		return p;
	}
}
