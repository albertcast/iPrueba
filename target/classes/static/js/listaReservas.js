$(document).ready(function() {
    $.ajax({
      	url: sessionStorage.url+"/api/booking/",
		success: function(data){			
			 let output = data.map(i => "<tr><th scope='row'>" + i.guest + "</td><td>" + i.arrivalDate 
										+ "</td><td>" + i.departureDate+ "</td><td>" + i.apartment+ "</td></tr>").join('');
			document.getElementById("tablaUser").innerHTML += output;
		}
    })
});	

