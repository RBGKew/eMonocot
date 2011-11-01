package org.emonocot.service.impl;

import org.emonocot.api.AnnotationService;
import org.emonocot.model.common.Annotation;
import org.emonocot.persistence.dao.AnnotationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class AnnotationServiceImpl extends
        ServiceImpl<Annotation, AnnotationDao> implements AnnotationService {

    /**
     *
     * @param annotationDao Set the annotation dao
     */
    @Autowired
    public void setAnnotationDao(AnnotationDao annotationDao) {
        this.dao = annotationDao;
    }
}
