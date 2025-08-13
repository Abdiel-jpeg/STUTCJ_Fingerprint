
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
	let imageContainer = subjectForm.querySelector(".image-container");
	const base64String = "data:image/png;base64," + data.fingerprintImage;
	
	inputs[0].value = data.nreloj;
	updateSelector = data.nreloj;
	inputs[1].value = data.nombre;
	inputs[2].value = data.apellidoPaterno;
	inputs[3].value = data.apellidoMaterno;
	inputs[4].value = data.activated;
	
	imageContainer.innerHTML = "<img src=" + base64String + " />";
}

window.addEventListener("load", async () => {
	let subjectForm = document.getElementsByClassName("subject-form")[0];
	
	document.getElementById("actualizar").addEventListener("click", (e) => {
			e.preventDefault();
			
			console.log(updateSelector);
			
			//En caso de que la imagen no se decidiera actualizar
			if (encodedImage == undefined) {
				let nreloj = inputs[0].value;
				let nombre = inputs[1].value;
				let apellidoPaterno = inputs[2].value;
				let apellidoMaterno = inputs[3].value;
				let activated = inputs[4].value;
				
				updateSubjectWithoutImage(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated == "true" ? 1 : 0, updateSelector);
				
			//En caso de que se decidiera actualizar la imagen
			} else {
				let nreloj = inputs[0].value;
				let nombre = inputs[1].value;
				let apellidoPaterno = inputs[2].value;
				let apellidoMaterno = inputs[3].value;
				let activated = inputs[4].value;
				
				updateSubject(nreloj, nombre, apellidoPaterno, apellidoMaterno, encodedImage, activated == "true" ? 1 : 0, updateSelector);
			}
		})

	insertData(subjectForm)
})