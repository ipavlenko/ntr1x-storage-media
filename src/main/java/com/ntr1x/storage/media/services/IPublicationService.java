package com.ntr1x.storage.media.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.core.converter.ConverterProvider.LocalDateTimeConverter;
import com.ntr1x.storage.media.model.Publication;
import com.ntr1x.storage.uploads.services.IImageService.RelatedImage;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPublicationService {
	
	Publication create(long scope, PublicationCreate create);
	Publication update(Long scope, long id, PublicationUpdate update);
	
	Publication select(Long scope, long id);
	
	Page<Publication> query(Long scope, Long user, Long relate, LocalDateTime since, LocalDateTime until, Pageable pageable);
	
	Publication remove(Long scope, long id);
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationPageResponse {

    	public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Publication> content;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationCreate {
        
		public Long relate;
		public long user;
		
		@NotBlank
    	public String title;
        
        public String subtitle;
    	public String promo;
    	public String body;
    	
    	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    	@ApiModelProperty(example="2016-10-07T04:05")
        public LocalDateTime published;
    	
    	public Long thumbnail;
    	
    	public RelatedImage[] images;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationUpdate {
		
		@NotBlank
    	public String title;
        
        public String subtitle;
    	public String promo;
    	public String body;
    	
    	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    	@ApiModelProperty(example="2016-10-07T04:05")
        public LocalDateTime published;
    	
    	public Long thumbnail;
    	
    	public RelatedImage[] images;
    }
}
