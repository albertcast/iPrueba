
function estrellas(rating){
	let i
	let output=""
	for(i=0;i<rating;i++){
		output+="<i class='icon_star'></i>";
	}
	if(i!=5){
		while(i<5){
			output+="<i class='icon_star' style='color: black'></i>";
			i++;
		}
	}
	return output
}

function botonBorrar(id,idComentario){
	if(id==sessionStorage.userId){
		let output="<button onclick='javascript:borrar(\""+idComentario+"\")' style='border: 2px solid black;color: black;background-color: orange;' ><b>Borrar comentario</b></button>"
		return output;
	}
	else return ""
}







$(document).ready(function() {
 
    $.ajax({
        type:"GET",
        url: sessionStorage.url+"/api/comment/commentsByApartment/",
		data:{
			id:sessionStorage.apartmentId
		},
        success: function(data){

            data.map(i => (
                $.ajax({
                    type:"GET",
                       url: sessionStorage.url+"/api/user/"+i.user,
                       success: function(datos){
                    let output = "<div class='review-item'>"+
                            "<div class='ri-pic'>"+
                                "<img src='"+ datos.image +"'>"+
                            "</div>"+
                            "<div class='ri-text'>"+
                                "<span>"+i.date+"</span>"+
                                "<div class='rating'>"+
                                    estrellas(i.rating)+
                                "</div>"+
                                "<h5>"+datos.nombre.toUpperCase()+" "+datos.apellidos.toUpperCase()+"</h5>"+
                                "<p>"+i.text+"</p>"+
                            "</div>"+
                            botonBorrar(i.user,i.id)+
                        "</div>"

                        document.getElementById("tablaComment").innerHTML += output;

                        }}
                        ))).join('');


        }
    })
});