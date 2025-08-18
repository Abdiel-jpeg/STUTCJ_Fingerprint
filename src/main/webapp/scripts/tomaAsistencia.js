
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
    let { eventos } = await fetchEvento();
	console.log(eventos)
    let selectEvento = document.getElementById('selectEvento');

    for (let i = 0; i < eventos.length; i++) {
		let titulo = eventos[i].titulo;
		selectedId = eventos[i].id;
		let optionEvento = document.createElement('option');

		optionEvento.setAttribute('value', titulo);
		optionEvento.setAttribute('id', selectedId)
		optionEvento.selected = true;
		optionEvento.appendChild(document.createTextNode(titulo));

		selectEvento.appendChild(optionEvento);
	}
	
	selectEvento.addEventListener("change", (e) => {
		selectedId = e.target.selectedOptions[0].id;
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

			if (response.status == "error") {
				alert(response.message);

			} else {
				//alert("Asistencia tomada para: " + response.message.nombre + " " + response.message.apellidoPaterno 
				//+ " " + response.message.apellidoMaterno + ". Nreloj:  " + response.message.nreloj);
        alert(response.message)
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
