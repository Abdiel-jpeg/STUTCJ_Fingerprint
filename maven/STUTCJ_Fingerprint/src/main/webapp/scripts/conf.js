
let text;

const getConf = async () => {
	const response = await fetch('SvConf', getParams);
	const json = await response.json();
	return json;
}

const setConf = async (threshold) => {
	const query = {
		"threshold": threshold
	}
	
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
	
	document.getElementById("submitButton").addEventListener("click", async (e) => {
		e.preventDefault();
		
		await setConf(inputThreshold.value);
		
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
	});
	
	
	document.getElementById("borrarTodo").addEventListener("click", async () => {
		const response = await specialConfiguration("emptySubjectTable", "");
		
		console.log(response); 
	})
	
	let data = await getConf();
	
	console.log(data);
	
	inputThreshold.value = data.message.threshold
});
