var postload=[];

function addTask(p,f){
	postload.push({p,f});
}

function reloadAll(){
	postload.sort(function(a,b){
		return a.p-b.p;
	});
	postload.forEach((task) => {
		try{
			task.f();
		}
		catch(e){
			console.error('Error during task execution:');
			console.error(e);
		}
	});
}

function reloadCopyButton(){
	$('.copy-btn-wrapper').remove();
	
	var a=$('pre');
	var cnt=0;
	for(var pre of a){
		if(pre.textContent==='')continue;
		if(pre.id===''){
			while($('#copycode'+cnt).length!==0){
				cnt++;
			}
			pre.id='copycode'+cnt;
		}
	}
	for(pre of a){
		if(pre.textContent==='')continue;
		var str='<div class="copy-btn-wrapper">';
		str+='<button type="button" class="copy-btn" data-clipboard-target="#'+pre.id+'">Copy</button>';
		str+='</div>';
		$(pre).before(str);
	}
	
	var clipboard=new ClipboardJS('.copy-btn');
	clipboard.on('success', function(e) {
		e.clearSelection();
	});
	clipboard.on('error', function(e) {
		alert('Copy failed :(');
	});
}

function reloadHighlight(){
	document.querySelectorAll('pre code').forEach((block) => {
		if(!block.className.includes('language-in')&&!block.className.includes('language-out')){
			hljs.highlightBlock(block);
		}
	});
}

function reloadTableStyle(){
	$('table').addClass(['table-bordered','table-sm']);
}

function reloadIOStyle(){
	var a=$('code.language-in').parent();
	a.addClass('sample-in');
	a.prev().addClass('sample-in-wrapper');
	
	var b=$('code.language-out').parent();
	b.addClass('sample-out');
	b.prev().addClass('sample-out-wrapper');
	
	a.prev().before('<h6 class="d-sm-block d-md-none">Input</h6>');
	a.prev().before('<h6 class="d-none d-md-block" style="width:48%;float:left;">Input</h6>');
	a.prev().before('<h6 class="d-none d-md-block" style="width:48%;float:right;">Ouput</h6>');
	b.prev().before('<h6 class="d-sm-block d-md-none">Output</h6>');
	b.after('<div style="clear:both;margin-bottom:8px;"></div>');
}

function reloadMathJax(){
	MathJax.Hub.Config({
		tex2jax: {
	    	skipTags: ['script', 'noscript', 'style', 'textarea', 'pre', 'code'],
	    	inlineMath: [ ['$','$'], ["\\(","\\)"] ],
	    	processEscapes: true
	    }
	});
	MathJax.Hub.Queue(function() {
		var all = MathJax.Hub.getAllJax(), i;
        for(i=0; i < all.length; i += 1) {
        	all[i].SourceElement().parentNode.className += ' has-jax';
        }
	});
	MathJax.Hub.Queue(['Typeset',MathJax.Hub]);
}

addTask(20,reloadTableStyle);
addTask(100,reloadCopyButton);
addTask(1000,reloadIOStyle);

window.addEventListener('load', (event) => {
	reloadAll();
});