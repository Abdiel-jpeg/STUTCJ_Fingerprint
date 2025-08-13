
let inputs;

//---------------------- Area API -------------------

const addEvento = async (
	titulo,
	descripcion
) => {
	const query = {
		"opcion": "eventoAdd",
		"titulo": titulo,
		"descripcion": descripcion
	}
	const response = await fetch('SvEvento', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

//-------------------- Logica ---------------------

window.addEventListener("load", async () => {
	let subjectForm = document.getElementsByClassName("evento-form")[0];
	inputs = subjectForm.querySelectorAll("input");
	
	document.getElementById("aniadir").addEventListener("click", async (e) => {
		e.preventDefault();
				
		//En caso de que se decidiera aniadir la imagen
		let titulo = inputs[0].value;
		let descripcion = inputs[1].value;
		
		let response = await addEvento(titulo, descripcion);
		console.log(response);
		alert(response.message);
			
		})
})