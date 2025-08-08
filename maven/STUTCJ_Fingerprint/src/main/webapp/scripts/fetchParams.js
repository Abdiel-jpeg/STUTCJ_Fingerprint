const getParams = {
	method: 'GET',
	mode: 'cors',
	cache: 'no-cache',
	headers: {
		'Access-Control-Allow-Origin': '*'
	}
}

const getQueryParams = (type, query) => {
	const queryParams = {
		method: type,
		mode: 'cors',
		cache: 'no-cache',
		headers: {
			'Access-Control-Allow-Origin': '*',
			'Access-Control-Allow-Headers': 'Content-Type, Content-Length, Authorization, Accept, X-Requested-Width',
			'Access-Control-Allow-Methods': 'POST, OPTIONS',
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(query)
	}

	return queryParams;
}
