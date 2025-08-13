
let inputs;

//---------------------- Area API -------------------

const addSubject = async (
	nreloj, 
	nombre, 
	apellidoPaterno, 
	apellidoMaterno, 
	fingerprintImage, 
	activado
) => {
	const query = {
		"opcion": "subjectAdd",
		"nreloj": nreloj,
		"nombre": nombre,
		"apellidoPaterno": apellidoPaterno, 
		"apellidoMaterno": apellidoMaterno,
		"fingerprintImage": fingerprintImage,
		"activado": activado
	}
	const response = await fetch('SvSubject', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

const addSubjectWithoutImage = async (
		nreloj, 
		nombre, 
		apellidoPaterno, 
		apellidoMaterno,  
		activado
	) => {
	const query = {
		"opcion": "subjectAddWithoutImage",
		"nreloj": nreloj,
		"nombre": nombre,
		"apellidoPaterno": apellidoPaterno, 
		"apellidoMaterno": apellidoMaterno,
		"activado": activado
	}
	const response = await fetch('SvSubject', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

//-------------------- Logica ---------------------

window.addEventListener("load", async () => {
	let subjectForm = document.getElementsByClassName("subject-form")[0];
	inputs = subjectForm.querySelectorAll("input");
	
	document.getElementById("aniadir").addEventListener("click", async (e) => {
			e.preventDefault();
			
			//En caso de que la imagen no se decidiera aniadir
			if (encodedImage == undefined) {
				let nreloj = inputs[0].value;
				let nombre = inputs[1].value;
				let apellidoPaterno = inputs[2].value;
				let apellidoMaterno = inputs[3].value;
				let activated = inputs[4].value;
				
				let response = await addSubjectWithoutImage(nreloj, nombre, apellidoPaterno, apellidoMaterno, activated == "true" ? 1 : 0);
				console.log(response);
				alert(response.message);
				
			//En caso de que se decidiera aniadir la imagen
			} else {
				let nreloj = inputs[0].value;
				let nombre = inputs[1].value;
				let apellidoPaterno = inputs[2].value;
				let apellidoMaterno = inputs[3].value;
				let activated = inputs[4].value;
				
				let response = await addSubject(nreloj, nombre, apellidoPaterno, apellidoMaterno, encodedImage, activated == "true" ? 1 : 0);
				console.log(response);
				alert(response.message);
			}
		})
})