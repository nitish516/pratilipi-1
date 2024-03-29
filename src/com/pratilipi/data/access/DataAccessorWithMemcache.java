package com.pratilipi.data.access;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.claymus.data.access.DataListCursorTuple;
import com.claymus.data.access.Memcache;
import com.pratilipi.commons.shared.AuthorFilter;
import com.pratilipi.commons.shared.PratilipiFilter;
import com.pratilipi.data.transfer.Author;
import com.pratilipi.data.transfer.Event;
import com.pratilipi.data.transfer.EventPratilipi;
import com.pratilipi.data.transfer.Genre;
import com.pratilipi.data.transfer.Language;
import com.pratilipi.data.transfer.Pratilipi;
import com.pratilipi.data.transfer.PratilipiAuthor;
import com.pratilipi.data.transfer.PratilipiGenre;
import com.pratilipi.data.transfer.PratilipiTag;
import com.pratilipi.data.transfer.Publisher;
import com.pratilipi.data.transfer.Tag;
import com.pratilipi.data.transfer.UserPratilipi;

@SuppressWarnings("serial")
public class DataAccessorWithMemcache
		extends com.claymus.data.access.DataAccessorWithMemcache
		implements DataAccessor {
	
	private static final String PREFIX_PRATILIPI = "Pratilipi-";
	private static final String PREFIX_LANGUAGE = "Language-";
	private static final String PREFIX_LANGUAGE_LIST = "LanguageList-";
	private static final String PREFIX_AUTHOR = "Author-";
	private static final String PREFIX_EVENT = "Event-";
	private static final String PREFIX_EVENT_PRATILIPI = "EventPratilipi-";
	private static final String PREFIX_EVENT_PRATILIPI_LIST = "EventPratilipiList-";
	private static final String PREFIX_GENRE = "Genre-";
	private static final String PREFIX_GENRE_LIST = "GenreList-";
	private static final String PREFIX_PRATILIPI_GENRE = "PratilipiGenre-";
	private static final String PREFIX_PRATILIPI_GENRE_LIST = "PratilipiGenreList-";
	private static final String PREFIX_USER_PRATILIPI = "UserPratilipi-";
	private static final String PREFIX_USER_PRATILIPI_LIST = "UserPratilipiList-";
	private static final String PREFIX_USER_PRATILIPI_PURCHASE_LIST = "UserPratilipiPurchaseList-";

	
	private final DataAccessor dataAccessor;
	private final Memcache memcache;
	
	
	public DataAccessorWithMemcache(
			DataAccessor dataAccessor, Memcache memcache ) {

		super( dataAccessor, memcache );
		this.dataAccessor = dataAccessor;
		this.memcache = memcache;
	}

	
	@Override
	public Pratilipi newPratilipi() {
		return dataAccessor.newPratilipi();
	}

	@Override
	public Pratilipi getPratilipi( Long id ) {
		Pratilipi pratilipi = memcache.get( PREFIX_PRATILIPI + id );
		if( pratilipi == null ) {
			pratilipi = dataAccessor.getPratilipi( id );
			if( pratilipi != null )
				memcache.put( PREFIX_PRATILIPI + id, pratilipi );
		}
		return pratilipi;
	}

	@Override
	public DataListCursorTuple<Long> getPratilipiIdList(
			PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount ) {
		
		return dataAccessor.getPratilipiIdList( pratilipiFilter, cursorStr, resultCount );
	}
	
	@Override
	public DataListCursorTuple<Pratilipi> getPratilipiList(
			PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount ) {

		return dataAccessor.getPratilipiList( pratilipiFilter, cursorStr, resultCount );
	}

	@Override
	public List<Pratilipi> getPratilipiList( List<Long> idList ) {
		if( idList.size() == 0 )
			return new ArrayList<>( 0 );
		
		
		List<String> memcacheKeyList = new ArrayList<>( idList.size() );
		for( Long id : idList )
			memcacheKeyList.add( PREFIX_PRATILIPI + id );
		Map<String, Pratilipi> memcacheKeyEntityMap = memcache.getAll( memcacheKeyList );

		
		List<Long> missingIdList = new LinkedList<>();
		for( Long id : idList )
			if( memcacheKeyEntityMap.get( PREFIX_PRATILIPI + id ) == null )
				missingIdList.add( id );
		List<Pratilipi> missingPratilipiList = dataAccessor.getPratilipiList( missingIdList );

		
		List<Pratilipi> pratilipiList = new ArrayList<>( idList.size() );
		for( Long id : idList ) {
			Pratilipi pratilipi = memcacheKeyEntityMap.get( PREFIX_PRATILIPI + id );
			if( pratilipi == null ) {
				pratilipi = missingPratilipiList.remove( 0 );
				if( pratilipi != null )
					memcache.put( PREFIX_PRATILIPI + id, pratilipi );
			}
			pratilipiList.add( pratilipi );
		}
		
		
		return pratilipiList;
	}
	
	@Override
	public Pratilipi createOrUpdatePratilipi( Pratilipi pratilipi ) {
		pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi );
		memcache.put( PREFIX_PRATILIPI + pratilipi.getId(), pratilipi );
		return pratilipi;
	}
	
	
	@Override
	public Language newLanguage() {
		return dataAccessor.newLanguage();
	}

	@Override
	public Language getLanguage( Long id ) {
		Language language = memcache.get( PREFIX_LANGUAGE + id );
		if( language == null ) {
			language = dataAccessor.getLanguage( id );
			if( language != null )
				memcache.put( PREFIX_LANGUAGE + id, language );
		}
		return language;
	}

	@Override
	public List<Language> getLanguageList() {
		List<Language> languageList = memcache.get( PREFIX_LANGUAGE_LIST );
		if( languageList == null ) {
			languageList = dataAccessor.getLanguageList();
			memcache.put( PREFIX_LANGUAGE_LIST, new ArrayList<>( languageList ) );
		}
		return languageList;
	}
	
	@Override
	public List<Language> getLanguageList( List<Long> idList ) {
		if( idList.size() == 0 )
			return new ArrayList<>( 0 );

		
		List<String> memcacheKeyList = new ArrayList<>( idList.size() );
		for( Long id : idList )
			memcacheKeyList.add( PREFIX_LANGUAGE + id );
		Map<String, Language> memcacheKeyEntityMap = memcache.getAll( memcacheKeyList );

		
		List<Long> missingIdList = new LinkedList<>();
		for( Long id : idList )
			if( memcacheKeyEntityMap.get( PREFIX_LANGUAGE + id ) == null ) 
				missingIdList.add( id );
		List<Language> missingLanguageList = dataAccessor.getLanguageList( missingIdList );

		
		List<Language> languageList = new ArrayList<>( idList.size() );
		for( Long id : idList ) {
			Language language = memcacheKeyEntityMap.get( PREFIX_LANGUAGE + id );
			if( language == null ) {
				language = missingLanguageList.remove( 0 );
				if( language != null )
					memcache.put( PREFIX_LANGUAGE + id, language );
			}
			languageList.add( language );
		}
		
		
		return languageList;
	}
	
	@Override
	public Language createOrUpdateLanguage( Language language ) {
		language = dataAccessor.createOrUpdateLanguage( language );
		memcache.put( PREFIX_LANGUAGE + language.getId(), language );
		memcache.remove( PREFIX_LANGUAGE_LIST );
		return language;
	}

	
	@Override
	public Author newAuthor() {
		return dataAccessor.newAuthor();
	}

	@Override
	public Author getAuthor( Long id ) {
		if( id == null )
			return null;
		
		Author author = memcache.get( PREFIX_AUTHOR + id );
		if( author == null ) {
			author = dataAccessor.getAuthor( id );
			if( author != null )
				memcache.put( PREFIX_AUTHOR + id, author );
		}
		return author;
	}

	@Override
	public Author getAuthorByEmailId( String email ) {
		Author author = memcache.get( PREFIX_AUTHOR + email );
		if( author == null ) {
			author = dataAccessor.getAuthorByEmailId( email );
			if( author != null )
				memcache.put( PREFIX_AUTHOR + email, author );
		}
		return author;
	}
	
	@Override
	public Author getAuthorByUserId( Long userId ) {
		Author author = memcache.get( PREFIX_AUTHOR + "User-" + userId );
		if( author == null ) {
			author = dataAccessor.getAuthorByUserId( userId );
			if( author != null )
				memcache.put( PREFIX_AUTHOR + "User-" + userId, author );
		}
		return author;
	}
	
	@Override
	public DataListCursorTuple<Author> getAuthorList( String cursor, int resultCount ) {
		return dataAccessor.getAuthorList( cursor, resultCount );
	}
	
	@Override
	public DataListCursorTuple<Long> getAuthorIdList(
			AuthorFilter authorFilter, String cursor, Integer resultCount ) {
		
		return dataAccessor.getAuthorIdList( authorFilter, cursor, resultCount );
	}

	@Override
	public DataListCursorTuple<Author> getAuthorList(
			AuthorFilter authorFilter, String cursor, Integer resultCount ) {
		
		return dataAccessor.getAuthorList( authorFilter, cursor, resultCount );
	}

	@Override
	public List<Author> getAuthorList( List<Long> idList ) {
		if( idList.size() == 0 )
			return new ArrayList<>( 0 );

		
		List<String> memcacheKeyList = new ArrayList<>( idList.size() );
		for( Long id : idList )
			memcacheKeyList.add( PREFIX_AUTHOR + id );
		Map<String, Author> memcacheKeyEntityMap = memcache.getAll( memcacheKeyList );

		
		List<Long> missingIdList = new LinkedList<>();
		for( Long id : idList )
			if( memcacheKeyEntityMap.get( PREFIX_AUTHOR + id ) == null ) 
				missingIdList.add( id );
		List<Author> missingAuthorList = dataAccessor.getAuthorList( missingIdList );

		
		List<Author> authorList = new ArrayList<>( idList.size() );
		for( Long id : idList ) {
			Author author = memcacheKeyEntityMap.get( PREFIX_AUTHOR + id );
			if( author == null ) {
				author = missingAuthorList.remove( 0 );
				if( author != null )
					memcache.put( PREFIX_AUTHOR + id, author );
			}
			authorList.add( author );
		}
		
		
		return authorList;
	}
	
	@Override
	public Author createOrUpdateAuthor( Author author ) {
		author = dataAccessor.createOrUpdateAuthor( author );
		memcache.put( PREFIX_AUTHOR + author.getId(), author );
		return author;
	}

	
	@Override
	public Publisher newPublisher() {
		return dataAccessor.newPublisher();
	}

	@Override
	public Publisher getPublisher( Long id ) {
		// TODO: enable caching
		return dataAccessor.getPublisher( id );
	}
	
	@Override
	public List<Publisher> getPublisherList() {
		// TODO: enable caching
		return dataAccessor.getPublisherList();
	}

	@Override
	public Publisher createOrUpdatePublisher( Publisher publisher ) {
		// TODO: enable caching
		return dataAccessor.createOrUpdatePublisher( publisher );
	}

	
	@Override
	public Event newEvent() {
		return dataAccessor.newEvent();
	}

	@Override
	public Event getEvent( Long id ) {
		Event event = memcache.get( PREFIX_EVENT + id );
		if( event == null ) {
			event = dataAccessor.getEvent( id );
			if( event != null )
				memcache.put( PREFIX_EVENT + id, event );
		}
		return event;
	}
	
	@Override
	public Event createOrUpdateEvent( Event event ) {
		event = dataAccessor.createOrUpdateEvent( event );
		memcache.put( PREFIX_EVENT + event.getId(), event );
		return event;
	}
	
	
	@Override
	public EventPratilipi newEventPratilipi() {
		return dataAccessor.newEventPratilipi();
	}


	@Override
	public EventPratilipi createOrUpdateEventPratilipi(
			EventPratilipi eventPratilipi) {
		eventPratilipi = dataAccessor.createOrUpdateEventPratilipi( eventPratilipi );
		memcache.put( PREFIX_EVENT_PRATILIPI + eventPratilipi.getId(), eventPratilipi );
		memcache.remove( PREFIX_EVENT_PRATILIPI_LIST + eventPratilipi.getEventId() );
		return eventPratilipi;
	}


	@Override
	public List<EventPratilipi> getEventPratilipiListByEventId(Long eventId) {
		List<EventPratilipi> eventPratilipiList =
				memcache.get( PREFIX_EVENT_PRATILIPI_LIST + eventId );
		if( eventPratilipiList == null ) {
			eventPratilipiList =
					dataAccessor.getEventPratilipiListByEventId( eventId );
			memcache.put(
					PREFIX_EVENT_PRATILIPI_LIST + eventId,
					new ArrayList<>( eventPratilipiList ) );
		}
		return eventPratilipiList;
	}
	
	@Override
	public EventPratilipi getEventPratilipiByPratilipiId( Long pratilipiId ){
		EventPratilipi eventPratilipi = memcache.get( PREFIX_EVENT_PRATILIPI + pratilipiId );
		if( eventPratilipi == null ){
			eventPratilipi = dataAccessor.getEventPratilipiByPratilipiId( pratilipiId );
			memcache.put( PREFIX_EVENT_PRATILIPI + pratilipiId, eventPratilipi );
		}
		
		return eventPratilipi;
			
	}

	
	@Override
	public Genre newGenre() {
		return dataAccessor.newGenre();
	}

	@Override
	public Genre getGenre( Long id ) {
		Genre genre = memcache.get( PREFIX_GENRE + id );
		if( genre == null ) {
			genre = dataAccessor.getGenre( id );
			if( genre != null )
				memcache.put( PREFIX_GENRE + id, genre );
		}
		return genre;
	}

	@Override
	public List<Genre> getGenreList() {
		List<Genre> genreList = memcache.get( PREFIX_GENRE_LIST );
		if( genreList == null ) {
			genreList = dataAccessor.getGenreList();
			memcache.put( PREFIX_GENRE_LIST, new ArrayList<>( genreList ) );
		}
		return genreList;
	}
	
	@Override
	public Genre createOrUpdateGenre( Genre genre ) {
		genre = dataAccessor.createOrUpdateGenre( genre );
		memcache.put( PREFIX_GENRE + genre.getId(), genre );
		memcache.remove( PREFIX_GENRE_LIST );
		return genre;
	}


	@Override
	public Tag newTag() {
		return dataAccessor.newTag();
	}

	@Override
	public Tag getTag( Long id ) {
		// TODO: enable caching
		return dataAccessor.getTag( id );
	}

	@Override
	public Tag createOrUpdateTag( Tag tag ) {
		// TODO: enable caching
		return dataAccessor.createOrUpdateTag( tag );
	}

	
	@Override
	public PratilipiAuthor newPratilipiAuthor() {
		// TODO: enable caching
		return dataAccessor.newPratilipiAuthor();
	}

	@Override
	public PratilipiAuthor createOrUpdatePratilipiAuthor( PratilipiAuthor pratilipiAuthor ) {
		// TODO: enable caching
		return dataAccessor.createOrUpdatePratilipiAuthor( pratilipiAuthor );
	}

	
	@Override
	public PratilipiGenre newPratilipiGenre() {
		return dataAccessor.newPratilipiGenre();
	}

	@Override
	public PratilipiGenre getPratilipiGenre( Long pratilipiId, Long genreId ) {
		PratilipiGenre pratilipiGenre = memcache.get(
				PREFIX_PRATILIPI_GENRE + pratilipiId + "-" + genreId );
		if( pratilipiGenre == null ) {
			pratilipiGenre =
					dataAccessor.getPratilipiGenre( pratilipiId, genreId );
			if( pratilipiGenre != null )
				memcache.put(
						PREFIX_PRATILIPI_GENRE + pratilipiId + "-" + genreId,
						pratilipiGenre );
		}
		return pratilipiGenre;
	}

	@Override
	public List<PratilipiGenre> getPratilipiGenreList( Long pratilipiId ) {
		List<PratilipiGenre> pratilipiGenreList =
				memcache.get( PREFIX_PRATILIPI_GENRE_LIST + pratilipiId );
		if( pratilipiGenreList == null ) {
			pratilipiGenreList =
					dataAccessor.getPratilipiGenreList( pratilipiId );
			memcache.put(
					PREFIX_PRATILIPI_GENRE_LIST + pratilipiId,
					new ArrayList<>( pratilipiGenreList ) );
		}
		return pratilipiGenreList;
	}

	@Override
	public PratilipiGenre createPratilipiGenre( PratilipiGenre pratilipiGenre ) {
		pratilipiGenre = dataAccessor.createPratilipiGenre( pratilipiGenre );
		memcache.put(
				PREFIX_PRATILIPI_GENRE + pratilipiGenre.getId(),
				pratilipiGenre );
		memcache.remove(
				PREFIX_PRATILIPI_GENRE_LIST + pratilipiGenre.getPratilipiId() );
		return pratilipiGenre;
	}

	@Override
	public void deletePratilipiGenre( Long pratilipiId, Long genreId ) {
		dataAccessor.deletePratilipiGenre( pratilipiId, genreId );
		memcache.remove(
				PREFIX_PRATILIPI_GENRE + pratilipiId + "-" + genreId );
		memcache.remove(
				PREFIX_PRATILIPI_GENRE_LIST + pratilipiId );
	}

	
	@Override
	public PratilipiTag newPratilipiTag() {
		// TODO: enable caching
		return dataAccessor.newPratilipiTag();
	}

	@Override
	public PratilipiTag createOrUpdatePratilipiTag( PratilipiTag pratilipiTag ) {
		// TODO: enable caching
		return dataAccessor.createOrUpdatePratilipiTag( pratilipiTag );
	}

	
	@Override
	public UserPratilipi newUserPratilipi() {
		return dataAccessor.newUserPratilipi();
	}
	
	@Override
	public UserPratilipi getUserPratilipi( Long userId, Long pratilipiId ) {
		UserPratilipi userPratilipi = memcache.get(
				PREFIX_USER_PRATILIPI + userId + "-" + pratilipiId );
		if( userPratilipi == null ) {
			userPratilipi =
					dataAccessor.getUserPratilipi( userId, pratilipiId );
			if( userPratilipi != null )
				memcache.put(
						PREFIX_USER_PRATILIPI + userId + "-" + pratilipiId,
						userPratilipi );
		}
		return userPratilipi;
	}

	@Override
	public List<UserPratilipi> getUserPratilipiList( Long pratilipiId ) {
		List<UserPratilipi> userPratilipiList =
				memcache.get( PREFIX_USER_PRATILIPI_LIST + pratilipiId );
		if( userPratilipiList == null ) {
			userPratilipiList =
					dataAccessor.getUserPratilipiList( pratilipiId );
			memcache.put(
					PREFIX_USER_PRATILIPI_LIST + pratilipiId,
					new ArrayList<>( userPratilipiList ) );
		}
		return userPratilipiList;
	}

	@Override
	public List<Long> getPurchaseList( Long userId ) {
		List<Long> purchaseList =
				memcache.get( PREFIX_USER_PRATILIPI_PURCHASE_LIST + userId );
		if( purchaseList == null ) {
			purchaseList =
					dataAccessor.getPurchaseList( userId );
			memcache.put(
					PREFIX_USER_PRATILIPI_PURCHASE_LIST + userId,
					new ArrayList<>( purchaseList ) );
		}
		return purchaseList;
	}

	@Override
	public UserPratilipi createOrUpdateUserPratilipi( UserPratilipi userPratilipi ) {
		userPratilipi = dataAccessor.createOrUpdateUserPratilipi( userPratilipi );
		memcache.put(
				PREFIX_USER_PRATILIPI + userPratilipi.getId(),
				userPratilipi );
		memcache.remove(
				PREFIX_USER_PRATILIPI_LIST + userPratilipi.getPratilipiId() );
		memcache.remove(
				PREFIX_USER_PRATILIPI_PURCHASE_LIST + userPratilipi.getUserId() );
		return userPratilipi;
	}


}
