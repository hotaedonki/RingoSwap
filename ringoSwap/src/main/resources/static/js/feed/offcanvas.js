function showOffcanvasWithUserData() {
	const username = $(this).closest('[data-user-name]').data('user-name');
	
	$.ajax({
		url: 'showOffcanvasWithUserData'
		, type: 'post'
		, data: {username: username}
		, success: function() {
			console.log("성공")
		},
		error: function(error) {
			console.error(error)
		}
	})
}