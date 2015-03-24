<!-- PageContent :: Pratilipi :: Start -->


<#setting time_zone="${ timeZone }">
<#import "../../../../com/claymus/commons/client/ui/Social.ftl" as social>

<#assign shareUrl="http://${ domain }${ pratilipiData.getPageUrl() }">
	

<div class="container" itemscope itemtype="http://schema.org/Product">

	<#if showEditOptions>
		<div id="PageContent-Pratilipi-Publish" class="alert alert-danger" role="alert" style="text-align:center; margin-top:20px; margin-bottom:0px; display:none;"></div>
	</#if>

	<div class="row">

		<#-- Cover Image -->
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4" style="margin-top:25px; margin-bottom:15px;">
			<img id="PageContent-Pratilipi-CoverImage" class="img-responsive" src="${ pratilipiData.getCoverImageUrl() }" itemprop="image">
			<#if showEditOptions>
				<div id="PageContent-Pratilipi-CoverImage-EditOptions"></div>
			</#if>
			<div style="margin-top:10px; margin-bottom:10px; text-align:center">
				<@social.vToolbar shareUrl=shareUrl/>
			</div>
		</div>
		
		<#-- Title, Author Name, Genre List, Summary and Buttons -->
		<div class="col-lg-10 col-md-10 col-sm-9 col-xs-8" style="padding-bottom:15px;">
			<h1 id="PageContent-Pratilipi-Title" itemprop="name">${ pratilipiData.getTitle() }</h1>
			<#if pratilipiData.getAuthorData()??>
				<h4><a href="${ pratilipiData.getAuthorData().getPageUrlAlias() ! pratilipiData.getAuthorData().getPageUrl() }" id="PageContent-Pratilipi-AuthorName">${ pratilipiData.getAuthorData().getFullName() ! pratilipiData.getAuthorData().getFullNameEn() }</a></h4>
			</#if>
			
			<h5 id="PageContent-Pratilipi-GenreList">
				<#list pratilipiData.getGenreNameList() as genreName>
					${ genreName }<#if genreName_has_next>,</#if>
				</#list>
			</h5>
			
			<h6>
				<span>Listed On : ${ pratilipiData.getListingDate()?date } / </span>
				<span>Last Updated On : ${ pratilipiData.getLastUpdated()?date }</span>
			</h6>
			
			<#if !userData.getEmail()??>
				<div id="PageContent-Pratilipi-RatingReadOnly" title="Click to Rate" data-toggle='modal' data-target="#loginModal" onclick="window.location.href='#Rate'"></div>
			</#if>
			<#if showRatingOption>
				<div id="PageContent-Pratilipi-Rating"></div>
			</#if>
			<div id="PageContent-Pratilipi-Summary" style="margin-top:20px; margin-bottom:10px;" itemprop="description">
				${ pratilipiData.getSummary()! }
			</div>
			<#if showEditOptions>
				<div id="PageContent-Pratilipi-Summary-EditOptions" style="text-align:right;"></div>
			</#if>
				
			<button type="button" class="btn btn-success" onclick="window.location.href='${ pratilipiData.getReaderPageUrl() }'">Read For Free</button>
			<#if showWriterOption>
				<button type="button" class="btn btn-primary" onclick="window.location.href='${ pratilipiData.getWriterPageUrl() }'">Edit This ${ pratilipiData.getType().getName() }</button>
			</#if>
			<#if showReviewedMessage>
				<button type="button" class="btn btn-primary" onclick="window.location.href='#Reviews'">
					<span class="glyphicon glyphicon-ok"></span> Reviewed
				</button>
			</#if>
			<#if showReviewOption>
				<button type="button" class="btn btn-primary" onclick="window.location.href='#Review'">Review This ${ pratilipiData.getType().getName() }</button>
			</#if>
		</div>

	</div> <#-- END of row -->

</div> <#-- END of container -->



<div class="container">

	<div id="Reviews" class="well" style="margin-top:25px;">
		<#if !userData.getEmail()??>
			<div title="Click to Write" data-toggle='modal' data-target="#loginModal" onclick="window.location.href='#Review'">
				<textarea rows="4" cols="50" placeholder="Write Review" style="width:100%; padding: 5px;"></textarea>
			</div>
		</#if>
		<#list reviewList as review >
			<#if review.getReview()??>
				<div class="hr-below">
					<h4 style="display:inline-block">${ userIdNameMap[ review.getUserId()?c ] } Says,</h4>
					<span class="pull-right"> ${ review.getReviewDate()?date }</span>
					<p>
						${ review.getReview() }
					</p>
				</div>
			</#if>
		</#list>
		<#if showReviewOption>
			<div id="Review">
				<h4 style="display:inline-block">${ userData.getName() } Says,</h4>
				<div id="PageContent-Pratilipi-Review"></div>
				<div id="PageContent-Pratilipi-Review-AddOptions" style="padding-top:15px"></div>
			</div>
		</#if>
	</div> <#-- END of well -->
	
</div> <#-- END of container -->



	<div id="PageContent-Pratilipi-EncodedData" style="display:none;">${ pratilipiDataEncodedStr }</div>
<#if showEditOptions>
	<script type="text/javascript" language="javascript" src="/pagecontent.pratilipi.witheditoptions/pagecontent.pratilipi.witheditoptions.nocache.js" defer></script>
<#else>
	<script type="text/javascript" language="javascript" src="/pagecontent.pratilipi/pagecontent.pratilipi.nocache.js" defer></script>
</#if>

<script language='javascript'>
	
	var maxRating = 5;
	var rating = ${ pratilipiData.getStarCount() ! 0 / pratilipiData.getRatingCount() ! 1 };
	function setRatingImage( hoverIndex ){
		var ratingDiv = document.getElementById( "PageContent-Pratilipi-RatingReadOnly" );
		for( var i=0; i< maxRating; i++ )
		{
			var img = document.createElement("IMG");
			img.src = getImageSrc ( i, hoverIndex );
			img.title = i;
			ratingDiv.appendChild( img );
		}
	}
	
	function getImageSrc( index ){
		var path = "";
        var hoverIndex = 0;
        if (index >= hoverIndex ) {
            if (index >= rating) {
                path =  "/theme.pratilipi/images/unselected.png";
            }
            else {
                path = "/theme.pratilipi/images/selected_blue.png";
            }
        }
        else {
            path = "/theme.pratilipi/images/hover_blue.png";
        }   
    
        return path;
	}
	
	if( window.attachEvent) {//for IE8 and below
		window.attachEvent( 'onload', function( event ){
			setRatingImage( 0 );
		});
		
	}
	else {
		window.addEventListener( 'load', function( event ){
			setRatingImage( 0 );
		});
		
	}
</script>


<!-- PageContent :: Pratilipi :: End -->