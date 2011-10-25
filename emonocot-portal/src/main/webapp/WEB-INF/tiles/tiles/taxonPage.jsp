<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
		<!-- <div class="row">
				<div class="twelvecol"> -->	
					<div class="content-wrapper">
			
						<header>
							<h2 id="page-title">
								<span class="taxonName">${taxon.name}</span>${taxon.authorship}
							</h2>
						</header>
					
				<c:if test="${taxon.protologue != null}">
					<c:set var="protologue" value="${taxon.protologue}" />
					<div class="row">
						<div id="protologue" class="twelvecol">${protologue.title}
							${protologue.datePublished} ${protologue.volume}
							${protologue.pages}</div>
					</div>
				</c:if>
				<div class="row">
					<div class="twelvecol">
					 		<c:if test="${not empty taxon.images}">
						<div id="container">
	<div id="gallery" class="ad-gallery">
      <div class="ad-image-wrapper">&#160;</div>
      <div class="ad-controls">&#160;</div>
      <div class="ad-nav">
        <div class="ad-thumbs">
          <ul class="ad-thumb-list" >
          <c:forEach var="image" items="${taxon.images}" varStatus="status">
            <li>
              <a href="${image.url}" >
                <img src="${image.url}" class="${status.index}" title="${image.caption}"/>
              </a>
            </li>
            </c:forEach>
           </ul>
          </div> 
		</div>
		<div id="descriptions">&#160;</div>
	</div>
	
</div>
</c:if>
		<!-- 		<div id="img"></div>
            			<ul id="gallery" class="jcarousel-skin-tango">
            				<c:forEach var="image" items="${taxon.images}" varStatus="status">
               					 <li><a href="${image.url}" title="${image.caption}"><img src="${image.url}" alt="${status.index}" width="100" height="80" /></a></li>
			                </c:forEach>    
            			</ul> -->	
						
						<!-- 
							<div id="showcase" class="showcase">
								<c:forEach var="image" items="${taxon.images}" varStatus="status">
									<div class="showcase-slide">
										<div class="showcase-content">
											<a href="/portal/image/${image.identifier}"><img src="${image.url}" title="${image.caption}" alt="${status.index}" /></a>
										</div>
										<div class="showcase-thumbnail">
											///<img src="${image.url}" alt="${status.index}" width="140px" /> 
						 				<img src="${image.url}" alt="${status.index}" />
											<div class="showcase-thumbnail-caption">${image.caption}</div>
											 <div class="showcase-thumbnail-cover">/* */</div> 
										</div>
										<div class="showcase-caption">
											<h2>${image.caption}</h2>
										</div>
									</div>
								</c:forEach>
							</div>
							<div class="showcase-slide" style="display: none;">
								<div class="showcase-content">
									<div class="showcase-content-wrapper"></div>
								</div>
							</div>
							 -->
							
						
				<!--  	</c:if>-->	
				
				
				
				
						<BR />
					</div>
				</div>
				<br />
				<div id="textContent">
					<c:forEach var="feature" items="${em:features()}">
						<c:set var="content" value="${em:content(taxon,feature)}" />
						<c:if test="${content != null}">
							<div class="row">
								<div class="twelvecol">
									<h5>${feature}</h5>
									<p>${content.content}</p>
								</div>
							</div>
						</c:if>
				</c:forEach>
			</div>
			<br/>
			<c:if test="${not empty em:regions(taxon)}">
				<div class="row">
					<h5>
						<spring:message code="distribution" />
					</h5>
					<div class="twelvecol">
					  <div id="map" style="height:470px; width:700px">
						<jsp:element  name="img">
						    <jsp:attribute name="id">alternative-map</jsp:attribute>
							<jsp:attribute name="src">
								<c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php">
									<c:param name="l" value="earth" /><!-- Layer -->
									<c:param name="ms" value="470" /><!-- Map Size -->
									<c:param name="bbox" value="-180,-90,180,90" /><!-- Bounding Box -->
									<c:param name="ad" value="${em:map(taxon)}" /><!-- Areas -->
									<c:param name="as" value="present:FF0000,,0.25" /><!-- Area Styling -->
								</c:url>
							</jsp:attribute>
						</jsp:element>
				       </div>
					</div>
					<c:set var="mapUrl">
					    <c:url value="http://edit.br.fgov.be/edit_wp5/v1/areas.php">
					        <c:param name="callback" value="foo"/><!-- callback -->
					        <c:param name="ms" value="1" /><!-- Map Size -->
						    <c:param name="l" value="earth" /><!-- Layer -->							
							<c:param name="img" value="false" /><!-- Bounding Box -->
							<c:param name="ad" value="${em:map(taxon)}" /><!-- Areas -->
							<c:param name="as" value="present:FF0000,,0.25" /><!-- Area Styling -->
					    </c:url>
					</c:set>
					<script type="text/javascript">
					var map;
					
					function foo(data) {
						if(!data) {
							
						} else {
						    $('#alternative-map').hide();
						    var base_layer = new OpenLayers.Layer.WMS("OpenLayers WMS","http://labs.metacarta.com/wms/vmap0",{layers: 'basic'}, {maxExtent: new OpenLayers.Bounds(-180, -90, 180, 90),isBaseLayer: true,displayInLayerSwitcher: false });
						    map = new OpenLayers.Map('map',{maxExtent: new OpenLayers.Bounds(-180, -90, 180, 90), maxResolution: 0.72, restrictedExtent: new OpenLayers.Bounds(-180, -90, 180, 90), projection: new OpenLayers.Projection("EPSG:4326")});
						    map.addLayers([base_layer]);

						    for(i in data.layers) {
						        var layerName = "topp:tdwg_level_" + data.layers[i].tdwg.substr(4,1);
						                
						        var layer = new OpenLayers.Layer.WMS.Untiled("layer " + (i + 1), data.geoserver, {layers: layerName, transparent:"true", format:"image/png"}, {maxExtent: new OpenLayers.Bounds(-180, -90, 180, 90), isBaseLayer: false, displayInLayerSwitcher: false});
						        layer.params.SLD = data.layers[i].sld;       
						        map.addLayers([layer]);
						     }

						     var bbox = data.bbox.split(",");
						     map.zoomToExtent(new OpenLayers.Bounds(parseInt(bbox[0]),parseInt(bbox[1]),parseInt(bbox[2]),parseInt(bbox[3])));
						  }
					}
					$(document).ready(function() {
							$.ajax({url: "${mapUrl}", dataType: "script", type: "GET", cache: true, callback: foo, data:null});
					});
					</script>
					<div class="twelvecol">
						<ul id="distribution-textual">
							<c:forEach var="region" items="${em:regions(taxon)}">
								<li><spring:message code="${region}" /></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				</c:if>
				<br/>
				<c:if test="${taxon.parent != null}">
					<div class="row">
						<h5>
							<spring:message code="parent" />
						</h5>
						<div class="twelvecol">
							<jsp:element name="a">
                  				<jsp:attribute name="href">
                    				<c:url value="/taxon/${taxon.parent.identifier}" />
                  				</jsp:attribute>
                  				${taxon.parent.name} ${taxon.parent.authorship}
                			</jsp:element>
						</div>
					</div>
				</c:if>
				<br/>
				<c:if test="${not empty taxon.children}">
					<div class="row">
						<h5>
							<spring:message code="children" />
						</h5>
						<div class="twelvecol">
							<ul>
								<c:forEach var="child" items="${taxon.children}">
									<li>
										<jsp:element name="a">
                    						<jsp:attribute name="href">
                      							<c:url value="/taxon/${child.identifier}" />
                    						</jsp:attribute>
                    						${child.name} ${child.authorship}
                  						</jsp:element>
                  					</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:if>
				<br/>
				<c:if test="${taxon.accepted != null}">
					<div class="row">
						<h5>
							<spring:message code="accepted" />
						</h5>
						<div class="twelvecol">
							<jsp:element name="a">
                    			<jsp:attribute name="href">
                      				<c:url value="/taxon/${taxon.accepted.identifier}" />
                    			</jsp:attribute>
                    			${taxon.accepted.name} ${taxon.accepted.authorship}
               				</jsp:element>
						</div>
					</div>
				</c:if>
				<br/>
				<c:if test="${not empty taxon.synonyms}">
					<div class="row">
						<h5>
							<spring:message code="synonyms" />
						</h5>
						<div class="twelvecol">
							<ul>
								<c:forEach var="synonym" items="${taxon.synonyms}">
									<li>
										<jsp:element name="a">
                      						<jsp:attribute name="href">
                        						<c:url value="/taxon/${synonym.identifier}" />
                      						</jsp:attribute>
                      						${synonym.name} ${synonym.authorship}
                    					</jsp:element>
                    				</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:if>
				
				
				
		</div>
</jsp:root>
