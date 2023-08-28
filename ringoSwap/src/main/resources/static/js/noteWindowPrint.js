(document).ready(function(){
	listPrint();
    //file분류에 따른 검색방식 지정
    $('.cateBtn').click(categoryEvent)
    //정렬방식 지정
    $('.sortBtn').click(sortEvent)
})