
const sendFingerprintImage = async (encodedImage) => {
	const query = {
		"option": "sendFingerprintImage",
		"data": encodedImage
	}
	
	console.log(query);
	const response = await fetch('SvAsistencia', getQueryParams('POST', query));
	const json = await response.json();
	return json;
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
			let response = await sendFingerprintImage(encodedImage);
			console.log(response);
			
			alert("Asistencia tomada para: " + response.message.nombre + " " + response.message.apellidoPaterno 
				+ " " + response.message.apellidoMaterno + ". Nreloj:  " + response.message.nreloj);
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
});