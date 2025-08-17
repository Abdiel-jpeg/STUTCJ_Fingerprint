
let inputs;
let updateSelector;

//---------------------- Area API -------------------

const getSubject = async (nreloj) => {
	const response = await fetch('SvSubject?nreloj=' + nreloj, getParams);
	const json = await response.json();
	return json;
}

const updateSubject = async (
	nreloj, 
	nombre, 
	apellidoPaterno, 
	apellidoMaterno, 
	fingerprintImage, 
	activado, 
	updateSelector
) => {
	const query = {
		"opcion": "subjectUpdate",
		"nreloj": nreloj,
		"nombre": nombre,
		"apellidoPaterno": apellidoPaterno, 
		"apellidoMaterno": apellidoMaterno,
		"fingerprintImage": fingerprintImage,
		"activado": activado,
		"updateSelector": updateSelector
	}
	const response = await fetch('SvSubject', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

const updateSubjectWithoutImage = async (
		nreloj, 
		nombre, 
		apellidoPaterno, 
		apellidoMaterno,  
		activado, 
		updateSelector
	) => {
	const query = {
		"opcion": "subjectUpdateWithoutImage",
		"nreloj": nreloj,
		"nombre": nombre,
		"apellidoPaterno": apellidoPaterno, 
		"apellidoMaterno": apellidoMaterno,
		"activado": activado,
		"updateSelector": updateSelector
	}
	const response = await fetch('SvSubject', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

//-------------------- Logica ---------------------

const insertData = async (subjectForm) => {
	const queryString = window.location.search;
	const params = new URLSearchParams(queryString);
	const nreloj = params.get("nreloj");
	const data = await getSubject(nreloj);
	inputs = subjectForm.querySelectorAll("input");
	let activatedOption = document.getElementById('activado').selectedOptions[0]
	let imageContainer = subjectForm.querySelector(".image-container");
	const base64String = "data:image/png;base64," + data.fingerprintImage;
	
	inputs[0].value = data.nreloj;
	updateSelector = data.nreloj;
	inputs[1].value = data.nombre;
	inputs[2].value = data.apellidoPaterno;
	inputs[3].value = data.apellidoMaterno;
	activatedOption = data.activated;
	
	imageContainer.innerHTML = "<img src=" + base64String + " />";
}

const buttonUpdate = async () => {
	let subjectForm = document.getElementsByClassName("subject-form")[0];
	
	document.getElementById("actualizar").addEventListener("click", (e) => {
		e.preventDefault();
		
		console.log(updateSelector);

		let nreloj = inputs[0].value;
		let nombre = inputs[1].value;
		let apellidoPaterno = inputs[2].value;
		let apellidoMaterno = inputs[3].value;
		let activated = document.getElementById('activado').selectedOptions[0].value == "Si" ? 1 : 0;
		
		//En caso de que la imagen no se decidiera actualizar
		if (encodedImage == undefined) {
			updateSubjectWithoutImage(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated, updateSelector);
			
		//En caso de que se decidiera actualizar la imagen
		} else {
			updateSubject(nreloj, nombre, apellidoPaterno, apellidoMaterno, encodedImage, activated, updateSelector);
		}

		alert("Imagen actualizada correctamente")
		location.href = "tabla.html";
		})

	insertData(subjectForm);
}

window.addEventListener("load", () => {
	buttonUpdate();
})