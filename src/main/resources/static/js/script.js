
const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {

		//true
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {

		//false
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");

	}

};


//sweet alert for delete contact


function deleteContact(cid) {

	swal({
		title: "Are you sure?",
		text: "you want to delete this contact  . . .",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {

				window.location = "/user/delete/" + cid;

			} else {
				swal("Your imaginary file is safe ! ! ");
			}
		});

}



