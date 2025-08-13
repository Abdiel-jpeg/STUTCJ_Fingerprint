
let inputs;
let updateSelector;

//---------------------- Area API -------------------

const getEvento = async (id) => {
	const response = await fetch('SvEvento?id=' + id, getParams);
	const json = await response.json();
	return json;
}

const updateEvento = async (id, titulo, descripcion) => {
	const query = {
		"opcion": "eventoUpdate",
		"id": id,
		"titulo": titulo,
		"descripcion": descripcion
	}
	const response = await fetch('SvEvento', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

const deleteEvento = async (id) => {
	const query = {
		"id": id
	};
	
	const response = await fetch('SvEvento', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

//-------------------- Logica ---------------------

const insertData = async (eventoForm) => {
	const queryString = window.location.search;
	const params = new URLSearchParams(queryString);
	const id = params.get("id");
	const data = await getEvento(id);
	inputs = eventoForm.querySelectorAll("input");

	updateSelector = data.id;
	inputs[0].value = data.titulo;
	inputs[1].value = data.descripcion;
}

window.addEventListener("load", async () => {
	let eventoForm = document.getElementsByClassName("evento-form")[0];
	
	document.getElementById("actualizar").addEventListener("click", async (e) => {
		e.preventDefault();
		
		console.log(updateSelector);
	
		let titulo = inputs[0].value;
		let descripcion = inputs[1].value;
		
		let response = await updateEvento(updateSelector, titulo, descripcion);
		console.log(response);
		alert(response.message);
		})

	insertData(eventoForm)
})