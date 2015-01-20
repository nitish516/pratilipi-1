<!DOCTYPE html>
<html lang="en">
	<head>
	
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		
		<#if request.getRequestURI() == "/">
			<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		</#if>

		<link rel="shortcut icon" type="image/png" href="/theme.pratilipi/favicon.png">

		<#if basicMode>
			<script src="//cdn-asia.pratilipi.com/third-party/jquery-1.11.1/jquery-1.11.1.min.js"></script>
			<script src="//cdn-asia.pratilipi.com/third-party/bootstrap-3.2.0/js/bootstrap.min.js" defer></script>
			<link rel="stylesheet" href="//cdn-asia.pratilipi.com/third-party/bootstrap-3.2.0/css/bootstrap.min.css">
			<script src="//cdn-asia.pratilipi.com/third-party/jquery-file-upload-9.7.0/js/vendor/jquery.ui.widget.js" defer></script>
			<script src="//cdn-asia.pratilipi.com/third-party/jquery-file-upload-9.7.0/js/jquery.iframe-transport.js" defer></script>
			<script src="//cdn-asia.pratilipi.com/third-party/jquery-file-upload-9.7.0/js/jquery.fileupload.js" defer></script>
			<script src="//cdn-asia.pratilipi.com/third-party/ckeditor-4.4.5-full/ckeditor.js" charset="utf-8" defer></script>
			<script language="javascript" defer>
				window.onload = function() {
					CKEDITOR.config.toolbar = [
							['Source','Format','Bold','Italic','Underline','Strike','-','Subscript','Superscript','-','RemoveFormat'],
							['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','Outdent','Indent'],
							['NumberedList','BulletedList'],
							['Blockquote','Smiley','HorizontalRule','PageBreak'],
							['Link','Unlink'],
							['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo'],
							['ShowBlocks','Maximize']];
					CKEDITOR.config.toolbar_BASIC = [
							['Bold','Italic','Underline','Strike','-','Subscript','Superscript','-','RemoveFormat'],
							['NumberedList','BulletedList'],
							['Blockquote','Smiley','HorizontalRule'],
							['Link','Unlink']];	
					
				}				
			</script>
		<#else>
			<!-- jQuery 2.1.1 -->
			<script src="//cdn-asia.pratilipi.com/third-party/jquery-2.1.1/jquery-2.1.1.min.js"></script>
			<!-- Polymer 0.5.2 -->
			<script src="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/webcomponentsjs/webcomponents.js"></script>
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/polymer/polymer.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-ajax/core-ajax.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-collapse/core-collapse.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-header-panel/core-header-panel.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-icon-button/core-icon-button.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-icons/core-icons.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-item/core-item.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-menu/core-menu.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-scroll-header-panel/core-scroll-header-panel.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-scroll-threshold/core-scroll-threshold.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/core-toolbar/core-toolbar.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-button/paper-button.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-dialog/paper-action-dialog.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-dialog/paper-dialog.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-fab/paper-fab.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-icon-button/paper-icon-button.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-item/paper-item.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-menu-button/paper-menu-button.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-slider/paper-slider.html">
			<link rel="import" href="//cdn-asia.pratilipi.com/third-party/polymer-0.5.2/paper-spinner/paper-spinner.html">
		</#if>

		<!-- Claymus Resources -->
		<#if basicMode>
			<link type="text/css" rel="stylesheet" href="/theme.default/style.css">
			<script type="text/javascript" language="javascript" src="/theme.default/script.js" defer></script>
			<script type="text/javascript" language="javascript" src="/theme.default/script.basicmode.js" defer></script>
		<#else>
			<link type="text/css" rel="stylesheet" href="/theme.default/style.css">
			<link type="text/css" rel="stylesheet" href="/theme.default/style.polymer.css">
			<script type="text/javascript" language="javascript" src="/theme.default/script.js" defer></script>
		</#if>

		<!-- Pratilipi Resources -->
		<#if basicMode>
			<link type="text/css" rel="stylesheet" href="/theme.pratilipi/style.css">
			<link type="text/css" rel="stylesheet" href="/theme.pratilipi/style.basicmode.css">
			<script type="text/javascript" language="javascript" src="/theme.pratilipi/script.js" defer></script>
			<script type="text/javascript" language="javascript" src="/theme.pratilipi/script.basicmode.js" defer></script>
		<#else>
			<link type="text/css" rel="stylesheet" href="/theme.pratilipi/style.css">
			<script type="text/javascript" language="javascript" src="/theme.pratilipi/script.js" defer></script>
		</#if>
		
		<#if page.getType()! != "READ" && page.getType()! != "WRITE">
			<script type="text/javascript" language="javascript" src="/pagecontent.userforms/pagecontent.userforms.nocache.js" async></script>
		</#if>

		<title>${ (page.getTitle() + " | Pratilipi") ! "Pratilipi" }</title>
		
		<!-- For Google Analytics tracking -->
		<script language="javascript">
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			
			<#if userId == 0>
				ga('create', 'UA-53742841-2', 'auto' );
				ga('require', 'displayfeatures');
				ga('send', 'pageview');
			<#else>
				ga('create', 'UA-53742841-2', { 'userId': ${ userId?c } });
				ga('require', 'displayfeatures');
				ga('set', 'dimension1', ${ userId?c });
				ga('send', 'pageview');
			</#if>
		</script>		
		
	</head>
	<body <#if !basicMode>fullbleed layout vertical</#if>>
		
		<span id="Claymus-Loading">Loading...</span>

		<#if websiteWidgetHtmlListMap["HEADER"] ??>
			<#list websiteWidgetHtmlListMap["HEADER"] as websiteWidgetHtml>
				${ websiteWidgetHtml }
			</#list>
		</#if>
		
		<#list pageContentHtmlList as pageContentHtml>
			${ pageContentHtml }
		</#list>
		
		<#if websiteWidgetHtmlListMap["FOOTER"] ??>
			<#list websiteWidgetHtmlListMap["FOOTER"] as websiteWidgetHtml>
				${ websiteWidgetHtml }
			</#list>
		</#if>

	</body>
</html>
