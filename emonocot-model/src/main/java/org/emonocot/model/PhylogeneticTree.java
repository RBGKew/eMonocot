package org.emonocot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

@Entity
public class PhylogeneticTree extends SearchableObject implements NonOwned,
		Media {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377124432983928528L;
	
	private Long id;
	
	private String title;
	
	private String description;
	
	private Set<Taxon> taxa = new HashSet<Taxon>();
	
	private String creator;
	
	private Set<Annotation> annotations = new HashSet<Annotation>();
	
	private String phylogeny;
	
	private List<Comment> comments = new ArrayList<Comment>();

	@Override
	@Id
    @GeneratedValue(generator = "system-increment")
	public Long getId() {
		return id;
	}

	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'PhylogeneticTree'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
	public Set<Annotation> getAnnotations() {	
		return annotations;
	}

	@Override
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	@Override
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_PhylogeneticTree", joinColumns = {@JoinColumn(name = "trees_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getTaxa() {
		return taxa;
	}

	@Override
	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setTaxa(Set<Taxon> taxa) {
		this.taxa = taxa;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Lob
	public String getPhylogeny() {
		return phylogeny;
	}

	public void setPhylogeny(String phylogeny) {
		this.phylogeny = phylogeny;
	}

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "commentPage_id")
    @OrderBy("created DESC")
    @Where(clause = "commentPage_type = 'PhylogeneticTree'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
	public List<Comment> getComments() {
		return comments;
	}

	@JsonIgnore
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
    public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = super.toSolrInputDocument();
    	sid.addField("searchable.label_sort", getTitle());
    	sid.addField("phylogeny.title_t", getTitle());
    	addField(sid,"phylogeny.creator_t", getCreator());
    	addField(sid,"phylogeny.description_t", getDescription());
    	StringBuilder summary = new StringBuilder().append(getTitle()).append(" ")
    	.append(getCreator()).append(" ").append(getDescription());
    	if(getTaxa() != null) {
    		for(Taxon t : getTaxa()) {
    		addField(sid,"taxon.class_s", t.getClazz());
    	    addField(sid,"taxon.family_s", t.getFamily());
    	    addField(sid,"taxon.genus_s", t.getGenus());
    	    addField(sid,"taxon.kingdom_s", t.getKingdom());
    	    addField(sid,"taxon.order_s", t.getOrder());
    	    addField(sid,"taxon.phylum_s", t.getPhylum());
    	    addField(sid,"taxon.subfamily_s", t.getSubfamily());
    	    addField(sid,"taxon.subgenus_s", t.getSubgenus());
    	    addField(sid,"taxon.subtribe_s", t.getSubtribe());
    	    addField(sid,"taxon.tribe_s", t.getTribe());
    	    summary.append(" ").append(t.getClazz())
    	    .append(" ").append(t.getClazz())
    	    .append(" ").append(t.getFamily())
    	    .append(" ").append(t.getGenus())
    	    .append(" ").append(t.getKingdom())
    	    .append(" ").append(t.getOrder())
    	    .append(" ").append(t.getPhylum())
    	    .append(" ").append(t.getSubfamily())
    	    .append(" ").append(t.getSubgenus())
    	    .append(" ").append(t.getSubtribe())
    	    .append(" ").append(t.getTribe());
    		}
    	}
    	sid.addField("searchable.solrsummary_t", summary.toString());
    	
    	return sid;
	}

}