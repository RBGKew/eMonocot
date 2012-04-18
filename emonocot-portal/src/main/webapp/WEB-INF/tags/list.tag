<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:spring="http://www.springframework.org/tags"
  xmlns:em="http://e-monocot.org/portal/functions"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
<jsp:directive.attribute name="pager" type="org.emonocot.model.pager.Page"
		required="true" />
<table class="table">
	<tbody>
		<c:forEach var="item" items="${result.records}">
			<tr>
			<c:choose>
				<c:when test="${item.className == 'Taxon'}">
					<spring:url var="itemUrl" value="/taxon/${item.identifier}"/>
					<td>
						<spring:url var="iconUrl" value="/images/taxonPageIconWhite.jpg"/>
						<img src="${iconUrl}" class="pull-left" alt="Taxon"/>
					</td>
					<td>
						<a class="result" href="${itemUrl}" title="${item.name}">
							<c:choose>
								<c:when test="${em:isSynonym(item)}">
									<h4 class="h4Results title-no-bold"><em>${item.name}</em> ${item.authorship}</h4>
								</c:when>
								<c:otherwise>
									<h4 class="h4Results"><em>${item.name}</em> ${item.authorship}</h4>
								</c:otherwise>
							</c:choose>
						</a>
						<br/>
						<div><em><spring:message code="resultStatus" /></em> <spring:message code="${item.status}" /></div>
						<div><em><spring:message code="resultRank" /></em> <spring:message code="${item.rank}" /></div>
						<div><em><spring:message code="resultFamily" /></em> ${item.family}</div>
						<div><em><spring:message code="resultOrder" /></em> ${item.order}</div>
					</td>
					<td>
						<a href="${itemUrl}" class="thumbnail pull-right">
							<c:choose>
								<c:when test="${not empty item.image}">
									<c:url var="thumbnail" value="/images/thumbnails/${item.image.identifier}.jpg"/>
									<img src="${thumbnail}" title="${item.image.caption}"/>
								</c:when>
								<c:otherwise>
									<c:url var="thumbnail" value="/images/no_image_3.jpg"/>
									<img src="${thumbnail}" title="No image available"/>
								</c:otherwise>
							</c:choose>
						</a>
					</td>
				</c:when>
				<c:when test="${item.className == 'Image'}">
					<spring:url var="itemUrl" value="/image/${item.identifier}"/>

					<td>
						<spring:url var="iconUrl" value="/images/imagePageIconWhite.jpg"/>
						<img src="${iconUrl}" class="pull-left" alt="Image"/>
					</td>
					<td>
						<a class="result" href="${itemUrl}" title="${item.caption}"><h4 class="h4Results">${item.caption}</h4></a>
						<br/>
						<div><spring:url var="taxonUrl" value="/taxon/${item.taxon.identifier}"/></div>
						<div><em><spring:message code="imageOf"/></em>: <a href="${taxonUrl}">${item.taxon.name}</a></div>
						<c:if test="${item.creator != null}">
							<div><em><spring:message code="image.creator"/></em>: ${item.creator}</div>
						</c:if>
						<c:if test="${item.locality != null}">
							<div><em><spring:message code="image.locality"/></em>: ${item.locality}</div>
						</c:if>
					</td>
					<td>
						<a href="${itemUrl}" class="thumbnail pull-right">
							<c:url var="thumbnail" value="/images/thumbnails/${item.identifier}.jpg"/>
							<img src="${thumbnail}" title="${item.caption}"/>
						</a>
					</td>
				</c:when>
				<c:when test="${item.className == 'IdentificationKey'}">
					<td>
						<spring:url var="iconUrl" value=""/>
						<!-- TODO icon -->
						<img src="${iconUrl}" class="pull-left" alt="ID Key"/>
					</td>
					<td>
						<spring:url var="itemUrl" value="/key/${item.identifier}"/>
						<a class="result" href="${itemUrl}" title="${item.title}"><h4 class="h4Results">${item.title}</h4></a>
					</td>
					<td>
						<a href="${itemUrl}" class="thumbnail pull-right">
							<c:url var="thumbnail" value="/images/no_image_3.jpg"/>
							<img src="${thumbnail}" title="No image available"/>
						</a>
					</td>
				</c:when>
				<c:otherwise>
					Unknown class ${item.className}
				</c:otherwise>
			</c:choose>
			</tr>
		</c:forEach>
	</tbody>
</table>
</jsp:root>