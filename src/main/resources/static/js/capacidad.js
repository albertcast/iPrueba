
    $.ajax({
        url: sessionStorage.url+"/api/apartment/"+window.sessionStorage.apartmentId
    }).then(function(data) {
		for(let i=0;i<5;i++){
			let output="<option value=\"\">"+i+" Personas</option>";
			document.getElementById("capac").innerHTML += output;
		}

    });
