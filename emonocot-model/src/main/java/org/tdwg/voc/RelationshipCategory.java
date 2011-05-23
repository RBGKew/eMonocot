package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import org.emonocot.model.marshall.UriConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
*
* @author ben
*
*/
public class RelationshipCategory {

   /**
    *
    */
   private TaxonRelationshipTerm tcTaxonRelationshipTerm;

   /**
   *
   */
  @XStreamAlias("rdf:resource")
  @XStreamAsAttribute
  @XStreamConverter(UriConverter.class)
  private URI resource;

  /**
   *
   * @return the resource
   */
  public final Serializable getResource() {
      return resource;
  }

  /**
   *
   * @param newResource Set the resource
   */
  public final void setResource(final URI newResource) {
      this.resource = newResource;
  }

   /**
    *
    */
   protected RelationshipCategory() {
   }

   /**
    *
    * @param taxonRelationshipTerm Set the taxon relationship term
    * @param useRelation express the term as a link or embed the object
    */
   protected RelationshipCategory(
           final TaxonRelationshipTerm taxonRelationshipTerm,
           final boolean useRelation) {
       if (useRelation) {
           if (taxonRelationshipTerm != null
                   && taxonRelationshipTerm.getIdentifier() != null) {
                   setResource(taxonRelationshipTerm.getIdentifier());
           } else {
               this.tcTaxonRelationshipTerm = taxonRelationshipTerm;
           }
       } else {
           this.tcTaxonRelationshipTerm = taxonRelationshipTerm;
       }
   }

   /**
    *
    * @return the taxon relationship term
    */
   protected final TaxonRelationshipTerm getTaxonRelationshipTerm() {
       return tcTaxonRelationshipTerm;
   }

   /**
    *
    * @param taxonRelationshipTerm Set the taxon relationship term
    */
   protected final void setTaxonRelationshipTerm(
           final TaxonRelationshipTerm taxonRelationshipTerm) {
       this.tcTaxonRelationshipTerm = taxonRelationshipTerm;
   }

   /**
    *
    * @param taxonRelationshipTerm Set the taxon relationship term
    */
   protected RelationshipCategory(
           final TaxonRelationshipTerm taxonRelationshipTerm) {
       this.tcTaxonRelationshipTerm = taxonRelationshipTerm;
   }
}