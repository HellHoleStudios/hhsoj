var postload=[];

function addTask(a,f){
	postload.push({p:a,f:f});
}

function reloadAll(){
	postload.sort(function(a,b){
		return a.p-b.p;
	});
	postload.forEach(task=>{
		try{
			task.f();
		}
		catch(e){
			console.log('Error during task execution:');
			console.log(e);
		}
	});
}

function reloadCopyButton(){
	$('.copy-btn-wrapper').remove();
	
	var a=$('pre');
	var cnt=0;
	for(var i=0;i<a.length;i++){
		if(a[i].textContent=='')continue;
		if(a[i].id==''){
			while($('#copycode'+cnt).length!=0){
				cnt++;
			}
			a[i].id="copycode"+cnt;
		}
	}
	for(var i=0;i<a.length;i++){
		if(a[i].textContent=='')continue;
		var str='<div class="copy-btn-wrapper">'
		str+='<button type="button" class="copy-btn" data-clipboard-target="#'+a[i].id+'">Copy</button>';
		str+='</div>'
		$(a[i]).before(str);
	}
	
	var clipboard=new ClipboardJS('.copy-btn');
	clipboard.on('success', function(e) {
	    e.clearSelection();
	});
	clipboard.on('error', function(e) {
	    alert("Copy failed :(");
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
	document.querySelectorAll('table').forEach((table) => {
		if(table.className.indexOf('table-bordered'==-1)){
			table.className+=' table-bordered table-sm';
		}
	});
}

function reloadIOStyle(){
	a=$('code.language-in').parent();
	a.addClass('sample-in');
	a.prev().addClass('sample-in-wrapper');
	
	b=$('code.language-out').parent();
	b.addClass('sample-out');
	b.prev().addClass('sample-out-wrapper');
	
	a.prev().before('<h6 style="width:48%;float:right;">Input</h6><h6 style="width:48%;float:left;">Output</h6>');
	b.after('<div style="clear:both;margin-bottom:8px;"></div>');
}

function reloadMathJax(){
	MathJax.Hub.Queue(["Typeset",MathJax.Hub]);
}

addTask(10,reloadHighlight);
addTask(20,reloadTableStyle);
addTask(100,reloadCopyButton);
addTask(1000,reloadIOStyle);
addTask(10000,reloadMathJax);

document.addEventListener('DOMContentLoaded', (event) => {
	reloadAll();
});