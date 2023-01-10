$(document).ready(function(id) {
	//container
	map = L.map('map').setView([36.7211, -4.4213], 13);
	    		
	//contenido del mapa, calles y todo eso
	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
	    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);
		
    $.ajax({
        url: sessionStorage.url+"/api/apartment/"+window.sessionStorage.apartmentId
    }).then(function(data) {
       	$('.apartment-title').append(data.title);
       	$('.apartment-place').append(data.place);
       	$('.apartment-date').append(data.date.substring(0,10));
        $('.apartment-capacity').append(data.capacity);
		$('.apartment-price').prepend(data.price);
		document.getElementById("description").value = data.description;
		document.getElementById("imageShow").setAttribute("src", data.image);
		L.marker([data.latitude, data.longitude]).addTo(map);

    });
});	