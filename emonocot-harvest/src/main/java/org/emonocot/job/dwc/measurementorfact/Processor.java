package org.emonocot.job.dwc.measurementorfact;

import org.emonocot.api.MeasurementOrFactService;
import org.emonocot.job.dwc.exception.RequiredFieldException;
import org.emonocot.job.dwc.read.OwnedEntityProcessor;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends OwnedEntityProcessor<MeasurementOrFact, MeasurementOrFactService> {
		
	
	@Autowired
	public void setMeasurementOrFactService(MeasurementOrFactService service) {
		super.service = service;
	}
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);
 
    @Override
    protected void doValidate(MeasurementOrFact t) {
    	if (t.getMeasurementType() == null) {
            throw new RequiredFieldException(t + " has no measurement type set", RecordType.MeasurementOrFact, getStepExecution().getReadCount());
        }
        
        
    }

    @Override
    protected void doUpdate(MeasurementOrFact persisted, MeasurementOrFact t) {
    	persisted.setMeasurementAccuracy(t.getMeasurementAccuracy());
    	persisted.setMeasurementDeterminedBy(t.getMeasurementDeterminedBy());
    	persisted.setMeasurementDeterminedDate(t.getMeasurementDeterminedDate());
    	persisted.setMeasurementMethod(t.getMeasurementMethod());
    	persisted.setMeasurementRemarks(t.getMeasurementRemarks());
    	persisted.setMeasurementType(t.getMeasurementType());
    	persisted.setMeasurementUnit(t.getMeasurementUnit());
    	persisted.setMeasurementValue(t.getMeasurementValue());
    	persisted.setBibliographicCitation(t.getBibliographicCitation());
    	persisted.setSource(t.getSource());
    }

    @Override
    protected RecordType getRecordType() {
	    return RecordType.MeasurementOrFact;
    }

	@Override
	protected void doCreate(MeasurementOrFact t) {
		
	}
}
