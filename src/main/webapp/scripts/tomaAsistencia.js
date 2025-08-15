
let selectedId;

const fetchEvento = async () => {
	const response = await fetch('SvEvento?offset=0');
	const json = await response.json();
	return json;
}

const fetchFingerprintImage = async (encodedImage, idEvento) => {
	const query = {
		"option": "fingerprintImage",
		"encodedImage": encodedImage,
		"idEvento": idEvento,
		"nreloj": 0
	}
	
	console.log(query);
	const response = await fetch('SvAsistencia', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

const addEventosToSelect = async () => {
    let data = await fetchEvento();
    let selectEvento = document.getElementById('selectEvento');

    //Add initial value
    let defaultValue = document.createElement("option");
    defaultValue.value = "Selecciona el evento";
    defaultValue.selected;
    defaultValue.disabled;

    selectEvento.appendChild(defaultValue);

    for (let i = 0; i < data.length; i++) {
		let titulo = data[i].titulo;
		let optionEvento = document.createElement('option');

		optionEvento.setAttribute('value', titulo);
		optionEvento.appendChild(document.createTextNode(titulo));

		selectEvento.appendChild(optionEvento);
	}
	
	selectEvento.addEventListener("change", (e) => {
		for (let i=0; i < data.length; i++) {
			evento = data[i];
			if (evento.titulo == e.target.selectedOptions[0].value) {
			  selectedId = evento.id;
			}
		  }
  });
}

window.addEventListener("load", () => {
	// Select the node that will be observed for mutations
	const targetNode = document.getElementById("content");

	// Options for the observer (which mutations to observe)
	const config = { attributes: true, childList: true, subtree: true };

	// Callback function to execute when mutations are observed
	const callback = async (mutationList, observer) => {
	  for (const mutation of mutationList) {
	    if (mutation.type === "childList") {
		  let fingerprintImage = document.getElementsByClassName("fingerprintImage")[0];
		  
		  if (fingerprintImage != undefined) {
			let response = await fetchFingerprintImage(encodedImage, selectedId);
			console.log(response);

			if (response.message == null) {
				alert("No se reconocío su huella dactilar, por favor intente de nuevo");

			} else {
				alert("Asistencia tomada para: " + response.message.nombre + " " + response.message.apellidoPaterno 
				+ " " + response.message.apellidoMaterno + ". Nreloj:  " + response.message.nreloj);
			}
		  }
		  
	    } else if (mutation.type === "attributes") {
	      //console.log(`The ${mutation.attributeName} attribute was modified.`);
	    }
	  }
	};

	// Create an observer instance linked to the callback function
	const observer = new MutationObserver(callback);

	// Start observing the target node for configured mutations
	observer.observe(targetNode, config);

	addEventosToSelect();
});