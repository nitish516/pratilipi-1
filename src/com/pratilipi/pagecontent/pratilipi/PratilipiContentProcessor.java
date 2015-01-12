package com.pratilipi.pagecontent.pratilipi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.claymus.commons.server.ClaymusHelper;
import com.claymus.commons.server.FreeMarkerUtil;
import com.claymus.commons.server.SerializationUtil;
import com.claymus.commons.shared.exception.InsufficientAccessException;
import com.claymus.commons.shared.exception.UnexpectedServerException;
import com.claymus.data.transfer.User;
import com.claymus.pagecontent.PageContentProcessor;
import com.claymus.service.shared.data.UserData;
import com.pratilipi.commons.server.PratilipiHelper;
import com.pratilipi.commons.shared.PratilipiContentType;
import com.pratilipi.commons.shared.PratilipiState;
import com.pratilipi.commons.shared.UserReviewState;
import com.pratilipi.data.access.DataAccessor;
import com.pratilipi.data.access.DataAccessorFactory;
import com.pratilipi.data.transfer.Pratilipi;
import com.pratilipi.data.transfer.UserPratilipi;
import com.pratilipi.service.shared.data.PratilipiData;

public class PratilipiContentProcessor extends PageContentProcessor<PratilipiContent> {

	@Override
	public String generateTitle( PratilipiContent pratilipiContent, HttpServletRequest request ) {
		Pratilipi pratilipi = DataAccessorFactory
				.getDataAccessor( request )
				.getPratilipi( pratilipiContent.getId() );
		
		return pratilipi.getTitle()
				+ ( pratilipi.getTitleEn() == null ? "" : " (" + pratilipi.getTitleEn() + ")" );
	}
	
	@Override
	public String generateHtml( PratilipiContent pratilipiContent, HttpServletRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		PratilipiHelper pratilipiHelper = PratilipiHelper.get( request );
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor( request );
		
		Long pratilipiId = pratilipiContent.getId();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		boolean showEditOption = PratilipiContentHelper
				.hasRequestAccessToUpdatePratilipiData( request, pratilipi );

		if( pratilipi.getState() != PratilipiState.PUBLISHED && !showEditOption )
			throw new InsufficientAccessException();
		
		
		UserPratilipi userPratilipi = null;
		if( pratilipiHelper.isUserLoggedIn() )
			userPratilipi = dataAccessor.getUserPratilipi( pratilipiHelper.getCurrentUserId(), pratilipiId );
		List<UserPratilipi> reviewList = dataAccessor.getUserPratilipiList( pratilipiId );
		
		Map<String, String> userIdNameMap = new HashMap<>();
		for( UserPratilipi review : reviewList ) {
			User user = dataAccessor.getUser( review.getUserId() );
			UserData userData = pratilipiHelper.createUserData( user );
			userIdNameMap.put( userData.getId().toString(), userData.getName() );
		}
		
		PratilipiData pratilipiData = pratilipiHelper.createPratilipiData(
				pratilipiId,
				PratilipiContentHelper.hasRequestAccessToReadPratilipiMetaData( request ) );
		

		// Creating data model required for template processing
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "timeZone", pratilipiHelper.getCurrentUserTimeZone() );
		dataModel.put( "userData", pratilipiHelper.createUserData( pratilipiHelper.getCurrentUser() ) );
		dataModel.put( "pratilipiData", pratilipiData );
		dataModel.put( "pratilipiDataEncodedStr", SerializationUtil.encode( pratilipiData ) );
		dataModel.put( "reviewList", reviewList );
		dataModel.put( "userIdNameMap", userIdNameMap );
		dataModel.put( "domain", ClaymusHelper.getSystemProperty( "domain" ) );
		dataModel.put( "showEditOptions", showEditOption );
		dataModel.put( "showWriterOption", showEditOption && pratilipiData.getContentType() != PratilipiContentType.IMAGE );
		dataModel.put( "showReviewedMessage",
				userPratilipi != null
				&& userPratilipi.getReviewState() != UserReviewState.NOT_SUBMITTED );
		dataModel.put( "showReviewOption",
				( userPratilipi == null || userPratilipi.getReviewState() == UserReviewState.NOT_SUBMITTED )
				&& PratilipiContentHelper.hasRequestAccessToAddPratilipiReview( request, pratilipi ) );


		return FreeMarkerUtil.processTemplate( dataModel, getTemplateName() );
	}
	
}
