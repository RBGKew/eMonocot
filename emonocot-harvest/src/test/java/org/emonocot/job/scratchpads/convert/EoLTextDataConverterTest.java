package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.emonocot.job.scratchpads.model.EoLAgent;
import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.model.common.License;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.DescriptionService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;

public class EoLTextDataConverterTest {
	
	private EoLTextDataObjectConverter converter = new EoLTextDataObjectConverter();
	private ConversionService conversionService;
	private DescriptionService descriptionService;
	
	private EoLDataObject dataObject = new EoLDataObject();
	private Taxon taxon = new Taxon();
	private EoLAgent eolAgent = new EoLAgent();
	private TextContent persistedTextContent = new TextContent();
	
	@Before
	public void setUp() {
	    conversionService = EasyMock.createMock(ConversionService.class);
	    descriptionService = EasyMock.createMock(DescriptionService.class);
	    converter.setConversionService(conversionService);
	    converter.setDescriptionService(descriptionService);
	    dataObject.setCreated("1300970978");
		dataObject.setModified("1300970994");
		dataObject.setLicense("http://creativecommons.org/licenses/by-nc/3.0/");
		dataObject.setSubject("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription");
		dataObject.setSource("http://scratchpad.cate-araceae.org/content/anthurium-schott");
		dataObject.setDescription("&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or short- to long-climbing, rarely rhizomatous,   internodes very short (plant &#13;");
		eolAgent.setRole("author");
		eolAgent.setURI("http://scratchpad.cate-araceae.org/users/ben");
		dataObject.setTaxon(taxon);
		dataObject.setAgent(eolAgent);
		persistedTextContent.setCreated(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		persistedTextContent.setModified(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		persistedTextContent.setLicense(License.ATTRIBUTION_NONCOMMERCIAL);
		persistedTextContent.setSource("http://scratchpad.cate-araceae.org/content/anthurium-schott");
		persistedTextContent.setTaxon(taxon);
		persistedTextContent.setFeature(Feature.GENERAL_DESCRIPTION);
		persistedTextContent.setContent("&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or short- to long-climbing, rarely rhizomatous,   internodes very short (plant &#13;");
		persistedTextContent.setCreator("http://scratchpad.cate-araceae.org/users/ben");
	}	

	/**
	 * Test that demonstrates the use of this converter where the taxon is new (taxon.getId() == null)
	 * 
	 * A new TextData object is created and populated. 
	 */
	@Test
	public void testConvertWithNewTaxon() {
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970978"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970994"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://creativecommons.org/licenses/by-nc/3.0/"),EasyMock.eq(License.class))).andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription"),EasyMock.eq(Feature.class))).andReturn(Feature.GENERAL_DESCRIPTION);
		EasyMock.replay(conversionService,descriptionService);
		
		TextContent textContent = converter.convert(dataObject);
		
		EasyMock.verify(conversionService,descriptionService);
		assertNotNull("Object returned must not be null",textContent);
		assertTrue("Object returned must be an instance of TextContent", textContent instanceof TextContent);
		assertEquals("Created Date should be set properly",textContent.getCreated(),new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		assertEquals("Modified Date should be set properly",textContent.getModified(),new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		assertEquals("License should be set properly",textContent.getLicense(),License.ATTRIBUTION_NONCOMMERCIAL);
		assertEquals("Feature should be set properly",textContent.getFeature(),Feature.GENERAL_DESCRIPTION);
		assertEquals("Taxon should be set properly",textContent.getTaxon(),taxon);
		assertEquals("Source should be set properly",textContent.getSource(),"http://scratchpad.cate-araceae.org/content/anthurium-schott");
		assertEquals("Content should be set properly",textContent.getContent(),"&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or short- to long-climbing, rarely rhizomatous,   internodes very short (plant &#13;");
		assertEquals("Creator should be set properly",textContent.getCreator(),"http://scratchpad.cate-araceae.org/users/ben");
	}
	
	/**
	 * Test that demonstrates the use of this converter where the taxon is not new (taxon.getId() != null) but the
	 * text data is new (descriptionService.getTextContent(Feature feature, Taxon taxon) == null)
	 * 
	 * A new TextData object is created and populated. 
	 */
	@Test
	public void testConvertWithPersistentTaxonNewContent() {		
		taxon.setId(1l);
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970978"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970994"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://creativecommons.org/licenses/by-nc/3.0/"),EasyMock.eq(License.class))).andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription"),EasyMock.eq(Feature.class))).andReturn(Feature.GENERAL_DESCRIPTION);
		EasyMock.expect(descriptionService.getTextContent(EasyMock.eq(Feature.GENERAL_DESCRIPTION),EasyMock.eq(taxon))).andReturn(null);
		EasyMock.replay(conversionService,descriptionService);
		
		TextContent textContent = converter.convert(dataObject);
		
		EasyMock.verify(conversionService,descriptionService);
		assertNotNull("Object returned must not be null",textContent);
		assertTrue("Object returned must be an instance of TextContent", textContent instanceof TextContent);
		assertEquals("Created Date should be set properly",textContent.getCreated(),new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		assertEquals("Modified Date should be set properly",textContent.getModified(),new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		assertEquals("License should be set properly",textContent.getLicense(),License.ATTRIBUTION_NONCOMMERCIAL);
		assertEquals("Feature should be set properly",textContent.getFeature(),Feature.GENERAL_DESCRIPTION);
		assertEquals("Taxon should be set properly",textContent.getTaxon(),taxon);
		assertEquals("Source should be set properly",textContent.getSource(),"http://scratchpad.cate-araceae.org/content/anthurium-schott");
		assertEquals("Content should be set properly",textContent.getContent(),"&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or short- to long-climbing, rarely rhizomatous,   internodes very short (plant &#13;");
		assertEquals("Creator should be set properly",textContent.getCreator(),"http://scratchpad.cate-araceae.org/users/ben");
	}
	
	/**
	 * Test that demonstrates the use of this converter where the taxon is not new (taxon.getId() != null) and the
	 * text data is old (descriptionService.getTextContent(Feature feature, Taxon taxon) returns an object which equals the new object)
	 * 
	 * The persistent object is returned and the new one is not persisted. 
	 */
	@Test
	public void testConvertWithPersistentTaxonOldContent() {
		taxon.setId(1l);
		
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970978"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970994"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://creativecommons.org/licenses/by-nc/3.0/"),EasyMock.eq(License.class))).andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription"),EasyMock.eq(Feature.class))).andReturn(Feature.GENERAL_DESCRIPTION);
		EasyMock.expect(descriptionService.getTextContent(EasyMock.eq(Feature.GENERAL_DESCRIPTION),EasyMock.eq(taxon))).andReturn(persistedTextContent);
		EasyMock.replay(conversionService,descriptionService);
		
		TextContent textContent = converter.convert(dataObject);
		
		EasyMock.verify(conversionService,descriptionService);
		assertEquals("Object returned must be the persisted text content",textContent,persistedTextContent);
	}
	
	/**
	 * Test that demonstrates the use of this converter where the taxon is not new (taxon.getId() != null) and the
	 * text data is newer (descriptionService.getTextContent(Feature feature, Taxon taxon) returns an object which does not the new object)
	 * 
	 * The new object is returned and is the old object is overwritten once the data is persisted. 
	 */
	@Test
	public void testConvertWithPersistentTaxonNewerContent() {
		taxon.setId(1l);
		dataObject.setModified("1302702173");
		
		EasyMock.expect(conversionService.convert(EasyMock.eq("1300970978"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("1302702173"),EasyMock.eq(DateTime.class))).andReturn(new DateTime(2011, 4, 13,13, 43,0,0, DateTimeZone.forID("GMT")));
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://creativecommons.org/licenses/by-nc/3.0/"),EasyMock.eq(License.class))).andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
		EasyMock.expect(conversionService.convert(EasyMock.eq("http://rs.tdwg.org/ontology/voc/SPMInfoItems#GeneralDescription"),EasyMock.eq(Feature.class))).andReturn(Feature.GENERAL_DESCRIPTION);
		EasyMock.expect(descriptionService.getTextContent(EasyMock.eq(Feature.GENERAL_DESCRIPTION),EasyMock.eq(taxon))).andReturn(persistedTextContent);
		EasyMock.replay(conversionService,descriptionService);
		
		TextContent textContent = converter.convert(dataObject);
		
		EasyMock.verify(conversionService,descriptionService);
		assertTrue("Object returned must be an instance of TextContent", textContent instanceof TextContent);
		assertEquals("Created Date should be set properly",textContent.getCreated(),new DateTime(2011, 3, 24,12, 49,0,0, DateTimeZone.forID("GMT")));
		assertEquals("Modified Date should be set properly",textContent.getModified(),new DateTime(2011, 4, 13,13, 43,0,0, DateTimeZone.forID("GMT")));
		assertEquals("License should be set properly",textContent.getLicense(),License.ATTRIBUTION_NONCOMMERCIAL);
		assertEquals("Feature should be set properly",textContent.getFeature(),Feature.GENERAL_DESCRIPTION);
		assertEquals("Taxon should be set properly",textContent.getTaxon(),taxon);
		assertEquals("Source should be set properly",textContent.getSource(),"http://scratchpad.cate-araceae.org/content/anthurium-schott");
		assertEquals("Content should be set properly",textContent.getContent(),"&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or short- to long-climbing, rarely rhizomatous,   internodes very short (plant &#13;");
		assertEquals("Creator should be set properly",textContent.getCreator(),"http://scratchpad.cate-araceae.org/users/ben");
		assertFalse("Object returned must not be the persisted text content",textContent == persistedTextContent);
		
	}
}
