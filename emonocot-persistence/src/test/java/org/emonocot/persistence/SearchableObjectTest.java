package org.emonocot.persistence;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.comms.Sorting;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.service.FacetName;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.facet.Facet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class SearchableObjectTest extends AbstractPersistenceTest {

    /**
     *
     */
    @Autowired
    private SearchableObjectDao searchableObjectDao;

    /**
    *
    */
    @Autowired
    private TaxonDao taxonDao;

    /**
   *
   */
    @Autowired
    private ImageDao imageDao;

    /**
     * @throws Exception
     *             if there is a problem with the callable
     */
    @Test
    public final void setUpTestDataWithinTransaction() throws Exception {
        doInTransaction(new Callable() {
            public Object call() {
                FullTextSession fullTextSession = Search
                        .getFullTextSession(getSession());
                fullTextSession.purgeAll(Taxon.class);
                fullTextSession.purgeAll(Image.class);
                Taxon taxon1 = createTaxon("Aus", "1", null, null, "Ausaceae",
                        "(1753)", Rank.GENUS, TaxonomicStatus.accepted,
                        new GeographicalRegion[] {});
                Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null,
                        "Ausaceae", "(1775)", Rank.SPECIES,
                        TaxonomicStatus.accepted, new GeographicalRegion[] {
                                Continent.AUSTRALASIA, Region.BRAZIL,
                                Region.CARIBBEAN });
                Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null,
                        "Ausaceae", "(1805)", Rank.SPECIES,
                        TaxonomicStatus.accepted,
                        new GeographicalRegion[] {Region.NEW_ZEALAND });
                Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2,
                        "Ausaceae", "(1895)", Rank.SPECIES,
                        TaxonomicStatus.synonym, new GeographicalRegion[] {});
                Taxon taxon5 = createTaxon("Aus eus", "5", null, taxon3,
                        "Ausaceae", "(1935)", Rank.SPECIES,
                        TaxonomicStatus.synonym, new GeographicalRegion[] {});
                Image img1 = createImage("Aus", "1");
                Image img2 = createImage("Aus bus", "2");
                searchableObjectDao.saveOrUpdate(taxon1);
                searchableObjectDao.saveOrUpdate(taxon2);
                searchableObjectDao.saveOrUpdate(taxon3);
                searchableObjectDao.saveOrUpdate(taxon4);
                searchableObjectDao.saveOrUpdate(taxon5);
                searchableObjectDao.saveOrUpdate(img1);
                searchableObjectDao.saveOrUpdate(img2);
                getSession().flush();
                return null;
            }
        });
    }

    /**
     *
     * @throws Exception
     *             if there is a problem
     */
    public final void tearDownTestDataWithinTransaction() throws Exception {
        doInTransaction(new Callable() {
            public Object call() {
                taxonDao.delete("5");
                taxonDao.delete("4");
                taxonDao.delete("3");
                taxonDao.delete("2");
                taxonDao.delete("1");
                imageDao.delete("1");
                imageDao.delete("2");
                return null;
            }
        });
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        Page<SearchableObject> pager = searchableObjectDao.search("Aus", null,
                null, null, null, null, null);
        assertEquals("there should be seven objects saved", (Integer) 7,
                pager.getSize());
    }

    /**
    *
    */
    @Test
    public final void testSearchWithFacets() {
        Map<FacetName, Integer> selectedFacets = new HashMap<FacetName, Integer>();
        selectedFacets.put(FacetName.CLASS, 1);
        Page<SearchableObject> pager = searchableObjectDao.search("Aus", null,
                null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY},
                selectedFacets, null);
        assertThat("There should be two facets returned",
                pager.getFacetNames(), hasItems("CLASS", "FAMILY"));

        List<Facet> classFacets = pager.getFacets().get("CLASS");
        String[] facetNames = new String[classFacets.size()];
        for (int i = 0; i < facetNames.length; i++) {
            facetNames[i] = classFacets.get(i).getValue();
        }

        assertThat("org.emonocot.model.taxon.Taxon should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.taxon.Taxon"));
        assertThat("org.emonocot.model.media.Image should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.media.Image"));
        assertEquals("There should be one value for the FAMILY facet", 1, pager
                .getFacets().get("FAMILY").size());
    }

    /**
     * @throws Exception
     *             if there is a problem tearing down and adding the test data
     */
    @Test
    public final void testSearchWithFacetsInTaxonDao() throws Exception {
        Map<FacetName, Integer> selectedFacets = new HashMap<FacetName, Integer>();
        selectedFacets.put(FacetName.CLASS, 1);
        Page<Taxon> pager = taxonDao.search("Aus", null, null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY,
                        FacetName.CONTINENT, FacetName.AUTHORITY,
                        FacetName.RANK, FacetName.TAXONOMIC_STATUS },
                selectedFacets, null);
        assertEquals("There should be five taxa returned", (Integer)5, pager.getSize());
        assertThat("There should be two facets returned",
                pager.getFacetNames(), hasItems("CLASS", "FAMILY"));

        List<Facet> classFacets = pager.getFacets().get("CLASS");
        String[] facetNames = new String[classFacets.size()];
        for (int i = 0; i < facetNames.length; i++) {
            facetNames[i] = classFacets.get(i).getValue();
        }

        assertThat("org.emonocot.model.taxon.Taxon should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.taxon.Taxon"));
        assertThat("org.emonocot.model.media.Image should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.media.Image"));
        assertEquals("There should be one value for the FAMILY facet", 1, pager
                .getFacets().get("FAMILY").size());

        selectedFacets.put(FacetName.RANK, 1);
        pager = taxonDao.search("Aus", null, null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY,
                        FacetName.CONTINENT, FacetName.AUTHORITY,
                        FacetName.RANK, FacetName.TAXONOMIC_STATUS },
                selectedFacets, null);
        assertEquals("There should be four taxa returned", (Integer)4, pager.getSize());
    }

    /**
     *
     */
    @Test
    public final void testSearchWithSorting() {
        Page<SearchableObject> results = searchableObjectDao.search("Au*",
                null, null, null, null, null, null);

        Sorting sort = new Sorting("label");
        results = searchableObjectDao.search("Au*", null, null, null, null,
                null, sort);
        String[] actual = new String[results.getSize()];
        for (int i = 0; i < results.getSize(); i++) {
            if (results.getRecords().get(i).getClass().equals(Taxon.class)) {
                actual[i] = ((Taxon) results.getRecords().get(i)).getName();
            } else {
                actual[i] = ((Image) results.getRecords().get(i)).getCaption();
            }
        }

        String[] expected = new String[] {"Aus", "Aus", "Aus bus", "Aus bus",
                "Aus ceus", "Aus deus", "Aus eus" };

        assertArrayEquals(expected, actual);
    }
}
