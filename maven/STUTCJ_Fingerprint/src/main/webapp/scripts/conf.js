
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



window.addEventListener("load", async () => {
	let inputThreshold = document.getElementById("threshold");
	
	document.getElementById("submitButton").addEventListener("click", async (e) => {
		e.preventDefault();
		
		await setConf(inputThreshold.value);
		
		window.location.reload();
	})
	
	let data = await getConf();
	
	console.log(data);
	
	inputThreshold.value = data.message.threshold
});
