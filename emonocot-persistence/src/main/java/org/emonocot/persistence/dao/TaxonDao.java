package org.emonocot.persistence.dao;

import java.util.List;

import org.emonocot.model.Taxon;
import org.emonocot.pager.Page;

/**
 *
 * @author ben
 *
 */
public interface TaxonDao extends Dao<Taxon> {

    /**
     * Returns the root taxa in the resultset (accepted taxa with no parent
     * taxon).
     * @param identifier TODO
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     *
     * @return a Page from the resultset
     */
    List<Taxon> loadChildren(String identifier, Integer pageSize, Integer pageNumber, String fetch);
    
    /**
     * @param example
     *            an object with fields set to the required values
     * @param ignoreCase
     *            whether to treat Uppercase/Lowercase letters the same
     * @param useLike
     *            whether to enable <i>LIKE</i> in query
     * @return a single page of objects that have the same properties as the
     *         example
     */
    Page<Taxon> searchByExample(Taxon example, boolean ignoreCase, boolean useLike);

}
