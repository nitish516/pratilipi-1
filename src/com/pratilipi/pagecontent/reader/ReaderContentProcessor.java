package com.pratilipi.pagecontent.reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import com.claymus.commons.client.UnexpectedServerException;
import com.claymus.data.access.BlobAccessor;
import com.claymus.data.transfer.BlobEntry;
import com.claymus.module.pagecontent.PageContentProcessor;
import com.pratilipi.commons.server.PratilipiHelper;
import com.pratilipi.commons.shared.PratilipiType;
import com.pratilipi.data.access.DataAccessor;
import com.pratilipi.data.access.DataAccessorFactory;
import com.pratilipi.data.transfer.Author;
import com.pratilipi.data.transfer.Pratilipi;
import com.pratilipi.pagecontent.pratilipis.PratilipisContentProcessor;

public class ReaderContentProcessor extends PageContentProcessor<ReaderContent> {

	private static final Logger logger =
			Logger.getLogger( ReaderContentProcessor.class.getName() );

	public static final String ACCESS_ID_PRATILIPI_ADD = PratilipisContentProcessor.ACCESS_ID_PRATILIPI_ADD;
	public static final String ACCESS_ID_PRATILIPI_UPDATE = PratilipisContentProcessor.ACCESS_ID_PRATILIPI_UPDATE;

	
	@Override
	protected String generateHtml( ReaderContent pratilipiContent, HttpServletRequest request )
			throws IOException, UnexpectedServerException {

		PratilipiType pratilipiType = pratilipiContent.getPratilipiType();

		String url = request.getRequestURI();
		String pratilipiIdStr =
				url.substring( url.lastIndexOf( '/' ) + 1 );
		String pageNoStr =
				request.getParameter( "page" ) == null ? "1" : request.getParameter( "page" );
		

		Long pratilipiId = Long.parseLong( pratilipiIdStr );
		int pageNo = Integer.parseInt( pageNoStr );
		PratilipiHelper pratilipiHelper = PratilipiHelper.get( request );

		
		// Fetching Pratilipi and Author
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		Author author = dataAccessor.getAuthor( pratilipi.getAuthorId() );
		dataAccessor.destroy();

		long pageCount = 0;
		String pageContent = "";
		if( pratilipi.getPageCount() != null && pratilipi.getPageCount() > 0 ) {

			pageCount = pratilipi.getPageCount();
			pageContent = "<img style=\"width:100%;\" src=\"" + PratilipiHelper.getContentImageUrl( pratilipiType, pratilipiId ) + "/" + pageNo + "\">";

		} else {

			// Fetching Pratilipi content
			BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
			BlobEntry blobEntry = blobAccessor.getBlob( PratilipiHelper.getContent( pratilipiType, pratilipiId ) );

			// TODO: Remove this as soon as possible
			if( blobEntry == null ) {

				// Hack to copy Pratilipi content from Data Store to Blob Store
				if( pratilipi.getContent() != null ) {
					logger.log( Level.INFO, "Creating Blob Store entry using Pratilipi content from Data Store ..." );
					blobAccessor.createBlob(
							PratilipiHelper.getContent( pratilipiType, pratilipiId ),
							"text/html",
							pratilipi.getContent(), Charset.forName( "UTF-8" ) );

				// Hack to create empty Pratilipi content from old Pratilipis
				} else {
					logger.log( Level.INFO, "Creating Blob Store entry using empty string ..." );
					blobAccessor.createBlob(
							PratilipiHelper.getContent( pratilipiType, pratilipiId ),
							"text/html",
							"&nbsp;", Charset.forName( "UTF-8" ) );
				}
				
				blobEntry = blobAccessor.getBlob( PratilipiHelper.getContent( pratilipiType, pratilipiId ) );
			}
			
			String content = new String( blobEntry.getData(), Charset.forName( "UTF-8" ) );

			logger.log( Level.INFO, "Content length: " + content.length() );
			
			Matcher matcher =  PratilipiHelper.REGEX_PAGE_BREAK.matcher( content );
			int startIndex = 0;
			int endIndex = 0;
			while( endIndex < content.length() ) {
				pageCount++;
				startIndex = endIndex;
				if( matcher.find() ) {
					endIndex = matcher.end();
					logger.log( Level.INFO, "Page " + pageCount + " length: "
							+ ( endIndex - startIndex )
							+ " (" + startIndex + " - " + endIndex + ") "
							+ matcher.group() );
				} else {
					endIndex = content.length();
					logger.log( Level.INFO, "Page " + pageCount + " length: "
							+ ( endIndex - startIndex )
							+ " (" + startIndex + " - " + endIndex + ")");
				}
				
				if( pageCount == pageNo )
					pageContent = content.substring( startIndex, endIndex );
			}
		}


		// Creating data model required for template processing
		Map<String, Object> dataModel = new HashMap<>();
		
		dataModel.put( "pratilipi", pratilipi );
		dataModel.put( "author", author );
		dataModel.put( "pageContent", pageContent );
	
		if( pageNo > 1 )
			dataModel.put( "previousPageUrl",
					PratilipiHelper.getReaderPageUrl( pratilipiType, pratilipiId )
							+ "?page=" + ( pageNo -1 ) );
		
		if( pageNo < pageCount )
			dataModel.put( "nextPageUrl",
					PratilipiHelper.getReaderPageUrl( pratilipiType, pratilipiId )
							+ "?page=" + ( pageNo + 1 ) );

		dataModel.put( "pratilipiHomeUrl", PratilipiHelper.getPageUrl( pratilipiType, pratilipiId ) );
		dataModel.put( "authorHomeUrl", PratilipiHelper.URL_AUTHOR_PAGE + pratilipi.getAuthorId() );

		dataModel.put( "showEditOptions",
				( pratilipiHelper.getCurrentUserId() == pratilipi.getAuthorId() && pratilipiHelper.hasUserAccess( ACCESS_ID_PRATILIPI_ADD, false ) )
				|| pratilipiHelper.hasUserAccess( ACCESS_ID_PRATILIPI_UPDATE, false ) );

		
		return super.processTemplate(
				dataModel,
				"com/pratilipi/pagecontent/reader/ReaderContent.ftl" );
	}
	
}
