
let inputs;
let updateSelector;

//---------------------- Area API -------------------

const fetchGetEvento = async (id) => {
	const response = await fetch('SvEvento?id=' + id, getParams);
	const json = await response.json();
	return json;
}

const fetchUpdateEvento = async (id, titulo, descripcion) => {
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

const fetchDeleteEvento = async (id) => {
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
	const data = await fetchGetEvento
(id);
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
		
		let response = await fetchUpdateEvento(updateSelector, titulo, descripcion);
		console.log(response);
		alert(response.message);
		location.href = "tablaEventos.html";
	});

	document.getElementById('eliminar').addEventListener("click", async () => {
		if (!window.confirm("¿Está seguro de eliminar este evento? Todas las asistencias incluidas serán eliminadas también")) {
			return;
		}

		const response = await fetchDeleteEvento(updateSelector);
		alert(response.message);
		location.href = tablaEventos.html;
	})

	insertData(eventoForm)
})