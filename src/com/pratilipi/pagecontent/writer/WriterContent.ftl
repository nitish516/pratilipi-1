<!-- PageContent :: Reader :: Start -->

<script src="//cdn-asia.pratilipi.com/third-party/ckeditor-4.4.5-full/ckeditor.js" charset="utf-8"></script>


<template is="auto-binding" id="PageContent-Reader">

	<core-header-panel flex mode="scroll" on-scroll={{performScrollActions}}>
		
		<core-toolbar class="bg-green">
			<paper-icon-button icon="arrow-back" title="Exit Writer" on-tap="{{performExit}}"></paper-icon-button>
			<div flex>${ pratilipiData.getTitle() }</div>
		</core-toolbar>
		
		<div horizontal center-justified layout class="bg-gray">
			<#if contentSize??>
				<div id="PageContent-Reader-Content" class="paper" contenteditable="true" style="font-size:${ contentSize };" ></div>
			<#else>
				<div id="PageContent-Reader-Content" class="paper" contenteditable="true" ></div>
			</#if>
		</div>
		
		<div class="bg-gray green" style="text-align:center; padding-bottom:16px; margin-bottom:75px;">
			<b>{{ pageNo }} / ${ pageCount }</b>
		</div>
		
	</core-header-panel>
	
	
	<div center horizontal layout id="PageContent-Reader-Navigation" style="position:fixed; bottom:10px; width:100%;">
		<paper-slider flex pin="true" snaps="false" min="1" max="{{ pageCount > 1 ? pageCount : 2 }}" value="${ pageNo }" class="bg-green" style="width:100%" disabled="{{ pageCount == 1 }}" on-change="{{displayPage}}"></paper-slider>
		<paper-fab mini icon="chevron-left" title="Previous Page" class="bg-green" style="margin-right:10px;" disabled="{{ pageNo == 1 }}" on-tap="{{displayPrevious}}"></paper-fab>
		<paper-fab mini icon="chevron-right" title="Next Page" class="bg-green" style="margin-right:10px;" disabled="{{ pageNo == pageCount }}" on-tap="{{displayNext}}"></paper-fab>
		<paper-fab mini icon="reorder" title="Options" class="bg-green" style="margin-right:10px;" on-tap="{{displayOptions}}"></paper-fab>
		<paper-fab icon="{{ isEditorDirty ? 'save' : 'done' }}" title="{{ isEditorDirty ? 'Save' : 'Saved' }}" class="{{ isEditorDirty ? 'bg-red' : 'bg-green' }}" style="margin-right:25px;" on-tap="{{saveContent}}"></paper-fab>
	</div>

	<paper-dialog id="PageContent-Reader-Options" style="color:gray; border:1px solid #EEEEEE;">
		<core-icon-button icon="remove" title="Decrease Text Size" on-tap="{{decTextSize}}"></core-icon-button>
		Text Size
		<core-icon-button icon="add" title="Increase Text Size" on-tap="{{incTextSize}}"></core-icon-button>
		<br/>
		<core-icon-button icon="description">&nbsp; Add New Page After This Page</core-icon-button>
		<br/>
		<core-icon-button icon="description">&nbsp; Add New Page Before This Page</core-icon-button>
		<br/>
		<core-icon-button icon="delete">&nbsp; Delete This Page</core-icon-button>
		<br/>
		<core-icon-button icon="history">&nbsp; Version History</core-icon-button>
		<br/>
		<core-icon-button icon="file-upload">&nbsp; Upload Word Document</core-icon-button>
	</paper-dialog>


	<core-ajax
			id="PageContent-Reader-Ajax-Get"
			url="/api.pratilipi/pratilipi/content"
			contentType="application/json"
			method="GET"
			handleAs="json"
			on-core-response="{{handleAjaxGetResponse}}" ></core-ajax>
			
	<core-ajax
			id="PageContent-Reader-Ajax-Put"
			url="/api.pratilipi/pratilipi/content"
			contentType="application/json"
			method="PUT"
			handleAs="json"
			on-core-response="{{handleAjaxPutResponse}}" ></core-ajax>

</template>


<script>

	var scope = document.querySelector( '#PageContent-Reader' );
	
	scope.pageCount = ${ pageCount };
	scope.pageNo = ${ pageNo };
	var pageNoDisplayed = 0;
	
	var contentArray = [];
	contentArray[scope.pageNo] = ${ pageContent }
	
	
	var ckEditor; // Initialized in initWriter()
	CKEDITOR.disableAutoInline = true;
	CKEDITOR.config.toolbar = [
			['Source','Format','Bold','Italic','Underline','Strike','-','Subscript','Superscript','-','RemoveFormat'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','Outdent','Indent'],
			['NumberedList','BulletedList'],
			['Blockquote','Smiley','HorizontalRule','PageBreak'],
			['Link','Unlink'],
			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo'],
			['ShowBlocks','Maximize']
	];	
	
	
	scope.performScrollActions = function( e ) {
		<#if pageCount gt 1>
			var bottom = jQuery( '.paper' ).position().top
					+ jQuery( '.paper' ).outerHeight( true )
					+ 66;
			if( e.target.y > 60 && bottom > e.target.scrollHeight && jQuery( '#PageContent-Reader-Navigation' ).is( ':visible' ) )
				jQuery( '#PageContent-Reader-Navigation' ).fadeOut( 'fast' );
			else if( ( e.target.y <= 60 || bottom <= e.target.scrollHeight ) && !jQuery( '#PageContent-Reader-Navigation' ).is( ':visible' ) )
				jQuery( '#PageContent-Reader-Navigation' ).fadeIn( 'fast' );
		</#if>
	}
	
	scope.performExit = function( e ) {
		window.location.href="${ exitUrl ! pratilipiData.getPageUrl() }";
	};

	scope.displayOptions = function( e ) {
		var dialog = document.querySelector( '#PageContent-Reader-Options' );
		if( dialog )
			dialog.toggle();
	};

	scope.displayPage = function( e ) {
		if( !checkDirtyAndUpdatePage( e.target.value ) )
			e.target.value = scope.pageNo;
		
	};
	
	scope.displayPrevious = function( e ) {
		if( checkDirtyAndUpdatePage( scope.pageNo - 1 ) )
			document.querySelector( 'paper-slider' ).value = scope.pageNo;
	};

	scope.displayNext = function( e ) {
		if( checkDirtyAndUpdatePage( scope.pageNo + 1 ) )
			document.querySelector( 'paper-slider' ).value = scope.pageNo;
	};
    
	function checkDirtyAndUpdatePage( pageNo ) {
		if( CKEDITOR.instances[ 'PageContent-Reader-Content' ].checkDirty() ) {
			var discardChanges = confirm( "You haven't saved your changes yet ! Press 'Cancel' to go back and save your changes. Press 'Ok' to discard your changes and continue." );
			if( !discardChanges )
				return false;
		}
		
		scope.pageNo = pageNo;
		updateContent();
		document.querySelector( 'core-header-panel' ).scroller.scrollTop = 0;
		prefetchContent();
		setCookie( '${ pageNoCookieName }', scope.pageNo );
		return true;
	}
	
	function updateContent() {
		if( pageNoDisplayed == scope.pageNo )
			return;
			
		if( contentArray[scope.pageNo] == null ) {
			document.querySelector( '#PageContent-Reader-Content' ).innerHTML = "<div style='text-align:center'>Loading ...</div>";
			var ajax = document.querySelector( '#PageContent-Reader-Ajax-Get' );
			ajax.params = JSON.stringify( { pratilipiId:${ pratilipiData.getId()?c }, pageNo:scope.pageNo, contentType:'PRATILIPI' } );
			ajax.go();
		} else {
			document.querySelector( '#PageContent-Reader-Content' ).innerHTML = contentArray[scope.pageNo];
			pageNoDisplayed = scope.pageNo;
		}
		
		ckEditor.resetDirty();
		ckEditor.resetUndo();
		scope.isEditorDirty = false;
	}
	
	function prefetchContent() {
		var ajax = document.querySelector( '#PageContent-Reader-Ajax-Get' );
		if( scope.pageNo > 1 && contentArray[scope.pageNo - 1] == null ) {
			ajax.params = JSON.stringify( { pratilipiId:${ pratilipiData.getId()?c }, pageNo:scope.pageNo - 1, contentType:'PRATILIPI' } );
			ajax.go();
		}
		if( scope.pageNo < scope.pageCount && contentArray[scope.pageNo + 1] == null ) {
			ajax.params = JSON.stringify( { pratilipiId:${ pratilipiData.getId()?c }, pageNo:scope.pageNo + 1, contentType:'PRATILIPI' } );
			ajax.go();
		}
	}
	
	scope.handleAjaxGetResponse = function( event, response ) {
		contentArray[response.response['pageNo']] = response.response['pageContent'];
		updateContent();
    };
    
    scope.saveContent = function( e ) {
    	if( scope.isEditorDirty ) {
	    	console.log( "TODO: save content to server ..." );
			ckEditor.resetDirty();
	    	scope.isEditorDirty = false;
    	}
    };
    

	scope.decTextSize = function( e ) {
		var fontSize = parseInt( jQuery( '#PageContent-Reader-Content' ).css( 'font-size' ).replace( 'px', '' ) );
		var newFontSize = fontSize - 2;
		if( newFontSize < 10 )
			newFontSize = 10;
		jQuery( '#PageContent-Reader-Content' ).css( 'font-size', newFontSize + 'px' );
		setCookie( '${ contentSizeCookieName }', newFontSize + 'px' );
	};

	scope.incTextSize = function( e ) {
		var fontSize = parseInt( jQuery( '#PageContent-Reader-Content' ).css( 'font-size' ).replace( 'px', '' ) );
		var newFontSize = fontSize + 2;
		if( newFontSize > 30 )
			newFontSize = 30;
		jQuery( '#PageContent-Reader-Content' ).css( 'font-size', newFontSize + 'px' );
		setCookie( '${ contentSizeCookieName }', newFontSize + 'px' );
	};
		

	function initWriter() {
		try {
			ckEditor = CKEDITOR.inline( 'PageContent-Reader-Content', {
				on:{
					'instanceReady': function() {
						updateContent();
						prefetchContent();
					}, 'change': function() {
						scope.isEditorDirty = ckEditor.checkDirty();
					},
				}
			});
		} catch( err ) {
			console.log( 'Reader initialization failed with error - ' + '\"' + err.message + '\". Retrying in 100ms ...' );
			window.setTimeout( initWriter, 100 );
		}
	}
	initWriter();
	
</script>


<style>

	#PageContent-Reader-Content img {
		width:100%;
	}
	
</style>


<!-- PageContent :: Reader :: End -->