
let text;

const getConf = async () => {
	const response = await fetch('SvConf', getParams);
	const json = await response.json();
	return json;
}

const setConf = async (threshold, limitAgremiado, limitEvento, limitAsistencia) => {
	const query = {
		"threshold": threshold,
		"limitAgremiado": limitAgremiado,
		"limitEvento": limitEvento,
		"limitAsistencia": limitAsistencia
	};
	
	const response = await fetch('SvConf', getQueryParams('POST', query));
	const json = await response.json();
	return json;
}

const specialConfiguration = async (option, data) => {
	const query = {
		"option": option,
		"data": data
	}
	
	const response = await fetch('SvTest', getQueryParams('POST', query));
	const json =await response.json();
	return json;
}

window.addEventListener("load", async () => {
	let inputThreshold = document.getElementById("threshold");
	let inputLimitAgremiado = document.getElementById("limitAgremiado");
	let inputLimitEvento = document.getElementById("limitEvento");
	let inputLimitAsistencia = document.getElementById("limitAsistencia");
	
	document.getElementById("submitButton").addEventListener("click", async (e) => {
		e.preventDefault();
		
		await setConf(inputThreshold.value, 
			inputLimitAgremiado.value,
			inputLimitEvento.value,
			inputLimitAsistencia.value
			);
		
		window.location.reload();
	})
	
	document.getElementById("inputFile").addEventListener("change", async (e) => {
		let file = e.target.files.item(0);
		text = await file.text();
	});
	
	document.getElementById("fileButton").addEventListener("click", async (e) => {
		e.preventDefault();
		
		const response = await specialConfiguration("addSubjectsFromCSV", text);
	
		console.log(response);
		alert(response.message);
	});
	
	
	document.getElementById("borrarTodo").addEventListener("click", async () => {
		if (!confirm("¿Está seguro de eliminar todo el agremiado? Esto eliminará sus huellas asociadas")) {
			return;
		}

		const response = await specialConfiguration("emptySubjectTable", "");
		
		console.log(response);
		alert(response.message);
	})
	
	let data = await getConf();
	
	console.log(data);
	
	inputThreshold.value = data.message.threshold;
	inputLimitAgremiado.value = data.message.limitAgremiado;
	inputLimitEvento.value = data.message.limitEvento;
	inputLimitAsistencia.value = data.message.limitAsistencia;
});
