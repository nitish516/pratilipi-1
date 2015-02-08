package com.pratilipi.pagecontent.pratilipi.api;

import com.claymus.api.GenericApi;
import com.claymus.api.annotation.Bind;
import com.claymus.api.annotation.Get;
import com.claymus.api.annotation.Put;
import com.claymus.commons.shared.exception.InsufficientAccessException;
import com.claymus.commons.shared.exception.InvalidArgumentException;
import com.claymus.commons.shared.exception.UnexpectedServerException;
import com.pratilipi.commons.shared.PratilipiContentType;
import com.pratilipi.pagecontent.pratilipi.PratilipiContentHelper;
import com.pratilipi.pagecontent.pratilipi.api.shared.GetPratilipiContentRequest;
import com.pratilipi.pagecontent.pratilipi.api.shared.GetPratilipiContentResponse;
import com.pratilipi.pagecontent.pratilipi.api.shared.PutPratilipiContentRequest;
import com.pratilipi.pagecontent.pratilipi.api.shared.PutPratilipiContentResponse;


@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/content" )
public class PratilipiContentApi extends GenericApi {

	@Get
	public Object getPratilipiContent( GetPratilipiContentRequest request )
			throws InvalidArgumentException, InsufficientAccessException,
			UnexpectedServerException {

		Object content = PratilipiContentHelper.getPratilipiContent(
				request.getPratilipiId(), request.getPageNumber(),
				request.getContentType(), this.getThreadLocalRequest() );
		
		if( request.getContentType() == PratilipiContentType.PRATILIPI ) {
			return new GetPratilipiContentResponse(
					request.getPratilipiId(),
					request.getPageNumber(), 
					request.getContentType(),
					(String) content );
		
		} else {
			return content;
		}

	}

	@Put
	public PutPratilipiContentResponse putPratilipiContent( PutPratilipiContentRequest request )
			throws InvalidArgumentException, InsufficientAccessException,
			UnexpectedServerException {

		int pageCount = PratilipiContentHelper.updatePratilipiContent(
				request.getPratilipiId(), request.getPageNumber(),
				request.getContentType(), request.getPageContent(),
				request.getInsertNew(), this.getThreadLocalRequest() );
			
		return new PutPratilipiContentResponse( request.getPageNumber(), pageCount );
	}		

}