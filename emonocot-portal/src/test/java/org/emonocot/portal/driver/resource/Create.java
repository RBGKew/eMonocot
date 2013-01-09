package org.emonocot.portal.driver.resource;

import org.emonocot.model.registry.Resource;
import org.emonocot.portal.driver.AbstractCreateForm;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Create extends AbstractCreateForm {
	
	@FindBy(how = How.ID, using = "resource")
	private WebElement form;
	
	public Create() {
		objectClass = Resource.class;
    	onSubmitPageClass = org.emonocot.portal.driver.resource.List.class;
	}

	@Override
	protected WebElement getForm() {
		return form;
	}
	
	public void setObjectIdentifier() {
		super.setObjectIdentifier(getForm().findElement(By.name("identifier")).getAttribute("value"));
	}

}
