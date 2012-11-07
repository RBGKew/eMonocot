package org.emonocot.portal.controller;

import org.emonocot.api.TaxonService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.api.IdentificationKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 */
@Controller
@RequestMapping("/key")
public class IdentificationKeyController extends
        GenericController<IdentificationKey, IdentificationKeyService> {

    /**
     *
     */
    public IdentificationKeyController() {
        super("key");
    }

    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @param newIdentificationKeyService
     *            Set the identification key service
     */
    @Autowired
    public final void setIdentificationKeyService(
            final IdentificationKeyService newIdentificationKeyService) {
        super.setService(newIdentificationKeyService);
    }

    /**
     * @param newTaxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param identifier
     *            The identifier of the identification key
     * @param model
     *            The model
     * @return The name of the view
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public final String getPage(@PathVariable final Long id,
            final Model model) {
        IdentificationKey key = getService().load(id, "object-page");
        model.addAttribute(key); 
     // TODO add opensession in view filter and remove this code
        if (key.getTaxon() != null) {
            model.addAttribute(taxonService.load(
                    key.getTaxon().getIdentifier(), null));
        }
        return "key/show";
    }

}
