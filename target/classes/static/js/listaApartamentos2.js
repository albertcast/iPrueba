$(document).ready(function() {
    $.ajax({
      	url: sessionStorage.url+"/api/apartment/",
		success: function(data){		
			sessionStorage.setItem("apartmentId", data.apartmentId);
										
			let output = data.map(i => "<div class='col-lg-4 col-md-6'>"+
                    "<div class='room-item'>"+
                        "<img src="+i.image+" alt=''>"+
                        "<div class='ri-text'>"+
                            "<h4>"+i.title+"</h4>"+
                            "<h3>"+i.price+"€<span>/Por noche</span></h3>"+
                            "<table>"+
                                "<tbody>"+
                                    "<tr>"+
                                        "<td class='r-o'>Ubicación:</td>"+
                                        "<td>"+i.place+"</td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<td class='r-o'>Capacidad:</td>"+
                                        "<td>"+i.capacity+"</td>"+
                                    "</tr>"+                                                               
                                "</tbody>"+
                            "</table>"+
                            "<a href='#' class='primary-btn' class='clickable' onclick='sendToDetalles("+ '"'+ i.id + '"' + ")'>Más detalles</a>"+
                        "</div></div></div>").join('');
                        
            
                
			document.getElementById("tablaAparment").innerHTML += output;
		}
    })
});	

