package com.pratilipi.pagecontent.auditlog;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.claymus.commons.server.FreeMarkerUtil;
import com.claymus.commons.shared.ClaymusResource;
import com.claymus.commons.shared.Resource;
import com.claymus.commons.shared.exception.UnexpectedServerException;
import com.claymus.pagecontent.PageContentProcessor;

public class AuditLogContentProcessor extends PageContentProcessor<AuditLogContent> {

	@Override
	public Resource[] getDependencies( AuditLogContent auditLogContent, HttpServletRequest request ) {
		return new Resource[] {
				ClaymusResource.JQUERY_2,
				ClaymusResource.POLYMER,
				ClaymusResource.POLYMER_CORE_AJAX,
				ClaymusResource.POLYMER_PAPER_SPINNER,
				new Resource() {
					
					@Override
					public String getTag() {
						return "<link rel='import' href='/polymer/PageContent-AuditLog.html'>";
					}
					
				},
		};
	}
	
	@Override
	public String generateTitle( AuditLogContent auditLogContent, HttpServletRequest request ) {
		return "Audit Log";
	}
	
	@Override
	public String generateHtml(
			AuditLogContent auditLogContent,
			HttpServletRequest request ) throws UnexpectedServerException {
		
		// Creating data model required for template processing
		Map<String, Object> dataModel = new HashMap<>();
		
		// Processing template
		return FreeMarkerUtil.processTemplate( dataModel, getTemplateName() );
	}
	
}
