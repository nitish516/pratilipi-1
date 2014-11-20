package com.pratilipi.pagecontent.pratilipis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.claymus.commons.server.FreeMarkerUtil;
import com.claymus.commons.server.SerializationUtil;
import com.claymus.commons.shared.exception.UnexpectedServerException;
import com.claymus.data.access.DataListCursorTuple;
import com.claymus.pagecontent.PageContentProcessor;
import com.pratilipi.commons.server.PratilipiHelper;
import com.pratilipi.commons.shared.PratilipiFilter;
import com.pratilipi.commons.shared.PratilipiType;
import com.pratilipi.data.access.DataAccessor;
import com.pratilipi.data.access.DataAccessorFactory;
import com.pratilipi.data.transfer.Language;
import com.pratilipi.data.transfer.Pratilipi;
import com.pratilipi.service.shared.data.PratilipiData;

public class PratilipisContentProcessor extends PageContentProcessor<PratilipisContent> {

	@Override
	public String generateHtml( PratilipisContent pratilipisContent, HttpServletRequest request )
			throws UnexpectedServerException {
		
		PratilipiHelper pratilipiHelper = PratilipiHelper.get( request );
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor( request );
		
		
		PratilipiType pratilipiType = pratilipisContent.getPratilipiType();
		PratilipiFilter pratilipiFilter = pratilipisContent.toFilter();
		
		DataListCursorTuple<Pratilipi> pratilipiListCursorTuple =
				dataAccessor.getPratilipiList( pratilipiFilter, null, 20 );
		List<PratilipiData> pratilipiDataList =
				pratilipiHelper.createPratilipiDataList(
						pratilipiListCursorTuple.getDataList(), false, true, false );

		
		// Creating data model required for template processing
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "pratilipiType", pratilipiType.getName() );
		dataModel.put( "pratilipisType", pratilipiType.getNamePlural() );
		if( pratilipisContent.getPublicDomain() != null && pratilipisContent.getPublicDomain() ) {
			dataModel.put( "pratilipisType", "Classic " + dataModel.get( "pratilipisType" ) );
			
		} else if( pratilipisContent.getLanguageId() != null ) {
			Language language = dataAccessor.getLanguage( pratilipisContent.getLanguageId() );
			dataModel.put( "pratilipisType",  language.getNameEn() + " " + dataModel.get( "pratilipisType" ) );

		}
		dataModel.put( "pratilipiDataList", pratilipiDataList );
		dataModel.put( "pratilipiFilterEncodedStr", SerializationUtil.encode( pratilipiFilter ) );
		

		// Processing template
		return FreeMarkerUtil.processTemplate( dataModel, getTemplateName() );
	}
	
}
